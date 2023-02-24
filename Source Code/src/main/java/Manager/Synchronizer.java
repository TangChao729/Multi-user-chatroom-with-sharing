package Manager;

import src.Commands;

import java.io.IOException;
import java.util.ArrayList;

public class Synchronizer {
	
	public static void syncMessage(String message) throws IOException {
		for (int i = 0; i < CreateWhiteBoard.connections.size(); i++) {
			ConnectionThread connectionThread = CreateWhiteBoard.connections.get(i);
			connectionThread.dataOut.writeUTF(message);
			connectionThread.dataOut.flush();
		}
	}

	public static void syncWhiteBoard(ArrayList<String> recordList) throws IOException {
		String[] recordArray = recordList.toArray(new String[recordList.size()]);
		for (String drawRecord : recordArray) {
			for (int i = 0; i < CreateWhiteBoard.connections.size(); i++) {
				ConnectionThread connectionThread = CreateWhiteBoard.connections.get(i);
				connectionThread.dataOut.writeUTF(Commands.DRAW + "," + drawRecord);
				connectionThread.dataOut.flush();
			}
		}
	}

	public static void syncKickUser(String selectedUser) {
		for (ConnectionThread connectionThread : CreateWhiteBoard.connections) {
			if (selectedUser.equals(connectionThread.name)) {
				connectionThread.kick = true;
				try {
					connectionThread.dataOut.writeUTF(Commands.KICK + "," + connectionThread.name);
					connectionThread.dataOut.flush();
					connectionThread.socket.close();

				} catch (IOException ioe) {
					System.out.println("ioe error.");
				} catch (Exception e) {
					System.out.println("syncro error.");
				}

				CreateWhiteBoard.connections.remove(connectionThread);
				CreateWhiteBoard.usernames.remove(selectedUser);
				boolean removed = removeUserFromChatThread(selectedUser);
//				System.out.println("Successful " + removed);
				break;
			}
		}

		for (String userName : CreateWhiteBoard.usernames) {
			selectedUser += "," + userName;
		}

		for (ConnectionThread connectionThread : CreateWhiteBoard.connections) {;
			try {
				connectionThread.dataOut.writeUTF(Commands.REMOVEUSER + "," + selectedUser);
				connectionThread.dataOut.flush();
			} catch (IOException ioe) {
				System.out.println("ioe error.");
			} catch (Exception e) {
				System.out.println("unknown error.");
			}
		}
	}

	public static void syncUserList() throws IOException {
		String toSyncUserList = Commands.ALLUSERNAMES;
		for (String userName : CreateWhiteBoard.usernames) {
			toSyncUserList += "," + userName;
		}
		Synchronizer.syncMessage(toSyncUserList);

		String[] localUserList = new String[CreateWhiteBoard.usernames.size()];
		localUserList = CreateWhiteBoard.usernames.toArray(localUserList);
		CreateWhiteBoard.managerUI.currUsersList.setListData(localUserList);
	}

	public static void syncClientLeft(ConnectionThread connectionThread) {
		CreateWhiteBoard.connections.remove(connectionThread);
		CreateWhiteBoard.usernames.remove(connectionThread.name);
		String message = Commands.REMOVEUSER + "," + connectionThread.name;
		for (String userName : CreateWhiteBoard.usernames) {
			message += "," + userName;
		}
		System.out.println("message: " + message);
		for (ConnectionThread otherConnectThread : CreateWhiteBoard.connections) {
			try {
				otherConnectThread.dataOut.writeUTF(message);
				otherConnectThread.dataOut.flush();
			} catch (IOException e) {
				System.out.println("ioe error.");
			}catch (Exception e) {
				System.out.println("sync client error.");
			}
		}

		String[] localUserList = new String[CreateWhiteBoard.usernames.size()];
		localUserList = CreateWhiteBoard.usernames.toArray(localUserList);
		CreateWhiteBoard.managerUI.currUsersList.setListData(localUserList);
//		System.out.println(connectionThread.name + "left");
	}

	public static boolean removeUserFromChatThread(String selectedUser) {
		boolean removed = false;
		if (selectedUser == null) {
			return removed;
		}
//		System.out.println("Seleceted name " + selectedUser);
		try {
			for (ChatMessageThread chatMessageThread : CreateWhiteBoard.messageConnections) {
//			System.out.println("This thread name: " + chatMessageThread.name);
				if (selectedUser.equals(chatMessageThread.name)) {
					removed = CreateWhiteBoard.messageConnections.remove(chatMessageThread);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("remove user error.");
		}


		return removed;
	}
}
