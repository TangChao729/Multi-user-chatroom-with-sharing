package Manager;

import src.Commands;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class ConnectionThread extends Thread {
	
	public Socket socket;
	public String name;
	public DataInputStream dataIn;
	public DataOutputStream dataOut;
	
	public boolean kick = false;
	public boolean running = true;

	
	// manage the manager and one user
	public ConnectionThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		System.out.println("Server connection running");

		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			dataIn = new DataInputStream(is);
			dataOut = new DataOutputStream(os);
			String utfReceived;

			while ((utfReceived = dataIn.readUTF()) != null && running) {
//				System.out.println("---DATA IN: " + utfReceived + "---");
				String[] receivedCommand = utfReceived.split(",", 2);

//				for (String s : receivedCommand) {
//					System.out.println(s);
//				}

				switch (receivedCommand[0]) {
					case Commands.NEWUSER:
						clientRequestToJoin(receivedCommand);
						break;

					case Commands.REQUESTUPDATE:
						Synchronizer.syncWhiteBoard(CreateWhiteBoard.managerUI.managerUIListener.getRecord());
						Synchronizer.syncUserList();
						break;

					case Commands.DRAW:
						Synchronizer.syncMessage(utfReceived);
						ManagerUI.managerUIListener.updateRecord(receivedCommand[1]);
						ManagerUI.canvas.repaint();
						break;

//					case Commands.CHAT:
//						Synchronizer.syncMessage(utfReceived);
//						String[] content = receivedCommand[1].split(",", 2);
//						ManagerUI.chatsDisplayArea.setText(ManagerUI.chatsDisplayArea.getText() + content[0] + ":\n" + content[1] + "\n");
//						break;


					case Commands.NEW:
						ManagerUI.canvas.removeAll();
						ManagerUI.canvas.updateUI();
						ManagerUI.managerUIListener.clearRecord();
						break;

					case Commands.DISCONNECT:
						socket.close();
						break;

					default:
						break;

				}
			}
			
		} catch (EOFException eofe) {
//			System.out.println("Client " + this.name + " Connection interruption.");
			if (!this.kick) {
				System.out.println("I shouldn't be run");
				Synchronizer.syncClientLeft(this);
				System.out.println("Broadcasting a user is left");
				boolean removed = Synchronizer.removeUserFromChatThread(this.name);
				System.out.println("when leave Successful " + removed);

			}
			else {
				System.out.println("I want to be run");
				CreateWhiteBoard.connections.remove(this);
				CreateWhiteBoard.usernames.remove(this.name);
			}
		} catch (SocketException se) {
			System.out.println("One client disconnected.");
		} catch (Exception e) {
			System.out.println("connection thread error.");
		}
	}

	private void clientRequestToJoin(String[] receivedCommand) throws IOException {
		String curName = receivedCommand[1];
//		System.out.println("Out 0:" + receivedCommand[0]);
//		System.out.println("Out 1:" + receivedCommand[1]);
		this.name = curName;
		try {
			if (CreateWhiteBoard.usernames.contains(curName)) {
				dataOut.writeUTF(Commands.INVALID);
//			System.out.println("user exist: " + CreateWhiteBoard.usernames.contains(curName));
//			System.out.println("Feedback: " + "feedback no");
				dataOut.flush();
				this.name = null;
				this.kick = true;
				this.running = false;

			} else {
				int feedback = CreateWhiteBoard.managerUI.clientConnectionRequest(curName);
				if (feedback == JOptionPane.YES_OPTION) {
//				System.out.println("Allow to share");
					CreateWhiteBoard.usernames.add(curName);
//				System.out.println("adding user: " + curName);
					dataOut.writeUTF(Commands.ALLOW);
					dataOut.flush();
				} else {
					dataOut.writeUTF(Commands.REJECT);
					dataOut.flush();
					CreateWhiteBoard.connections.remove(this);
				}
			}
		} catch (Exception e) {
			System.out.println("client join error.");
		}
	}
}
