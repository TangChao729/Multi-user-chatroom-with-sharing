package Manager;

import src.Commands;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class ManagerUIListener implements ActionListener, MouseListener, MouseMotionListener {

	Graphics2D g;
	public JFrame frame;
	int startX, startY, endX, endY;
	final int t = 1;
	String type = Commands.LINE;
	static Color color = Color.BLACK;
	String rgb = "0,0,0";
	String record;
	ArrayList<String> recordList = new ArrayList<>();

	private String file;

	public ManagerUI managerUI;

	public ManagerUIListener(JFrame frame, ManagerUI managerUI) {
		this.frame = frame;
		this.managerUI = managerUI;
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		try {
			type = e.getActionCommand();
			switch (type) {
				case Commands.NEW:
					newFile();
					break;
				case Commands.SAVE:
					saveFile(managerUI.file, true);
					break;
				case Commands.CLOSE:
					System.exit(1);
					break;
				case Commands.OPEN:
					if (managerUI.openFileNameField.getText().length() > 0) {
						openFile(ManagerUI.openFileNameField.getText());
					}
					break;
				case Commands.SAVEAS:
					if (managerUI.saveAsNameField.getText().length() > 0) {
						saveFile(managerUI.saveAsNameField.getText(), false);
					}
					break;
				case Commands.KICK:
					kickUser();
					break;
				case Commands.COLOR:
					getColor();
					break;
				case Commands.SEND:
					sendMsg();
					break;
				default:
					break;
			}
		} catch (Exception ex) {
			System.out.println("action performed error.");
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		try	{
			startX = e.getX();
			startY = e.getY();
			if (!g.getColor().equals(color)) {
				g.setColor(color);
			}

			if (type.equals(Commands.FREE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));
				g.drawLine(startX, startY, startX, startY);
				record = Commands.LINE + "," + startX + "," + startY + "," + startX + "," + startY + "," + rgb;
				recordList.add(record);
			}
		} catch (Exception ex) {
			System.out.println("mouse pressed error.");
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			endX = e.getX();
			endY = e.getY();
			if (this.type.equals(Commands.LINE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));
				g.drawLine(startX,  startY,  endX,  endY);
				record = Commands.LINE + "," + startX + "," + startY + "," + endX + "," + endY + "," + rgb;
				recordList.add(record);
			} else if (type.equals(Commands.CIRCLE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));
				int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
				g.drawOval(Math.min(startX, endX), Math.min(startY,  endY), diameter, diameter);
				record = Commands.CIRCLE + "," + startX + "," + startY + "," + endX + "," + endY + "," + rgb;
				recordList.add(record);
			} else if (type.equals(Commands.TRIANGLE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));

				g.drawLine(startX,  startY,  endX,  endY);
				g.drawLine(startX,  endY,  endX,  endY);
				g.drawLine(startX,  startY,  startX,  endY);

				record = Commands.TRIANGLE + "," + startX + "," + startY + "," + endX + "," + endY + "," + rgb;
				recordList.add(record);
			} else if (type.equals(Commands.TEXT)) {
				String text = JOptionPane.showInputDialog("Please Enter Input");
				if (text != null) {
					Font f = new Font(null, Font.PLAIN, this.t + 10);
					g.setFont(f);
					g.drawString(text,  endX,  endY);
					rgb = getRGB(color);

					record = Commands.TEXT + "," + endX + "," + endY + "," + rgb + ",:)," + text;
					recordList.add(record);
				}
			} else if (type.equals(Commands.RECTANGLE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));
				g.drawRect(Math.min(startX,  endX),  Math.min(startY,  endY),  Math.abs(startX - endX),  Math.abs(startY - endY));

				record = Commands.RECTANGLE + "," + startX + "," + startY + "," + endX + "," + endY + "," + rgb;
				recordList.add(record);
			} else {
				return;
			}

			try {
				Synchronizer.syncMessage(Commands.DRAW + "," + record);
			} catch (IOException ioe) {
				System.out.println("ioe error.");
			}
		} catch (Exception ex) {
			System.out.println("mouse released error.");
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		try {
			endX = e.getX();
			endY = e.getY();
			if (type.equals(Commands.FREE)) {
				rgb = getRGB(color);
				g.setStroke(new BasicStroke(t));
				g.drawLine(startX,  startY,  endX,  endY);

				record = Commands.LINE + "," + startX + "," + startY + "," + endX + "," + endY + "," + rgb;
				recordList.add(record);
				startX = endX;
				startY = endY;
			} else {
				return;
			}

			try {
				Synchronizer.syncMessage(Commands.DRAW + "," + record);
			} catch (IOException e1) {
				System.out.println("ioe error.");
			}
		} catch (Exception ex) {
			System.out.println("mouse dragged error.");
		}
	}


	public void setG(Graphics g) {
		this.g = (Graphics2D) g;
	}

	public ArrayList<String> getRecord() {
		return recordList;
	}

	public void updateRecord(String line) {
		recordList.add(line);
	}

	public void clearRecord() {
		recordList.clear();
	}

	private String getRGB(Color color) {
		return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
	}

	public void newFile() {
		try {
			int ans = JOptionPane.showConfirmDialog(null, "Do you want to save image before creating a new one?", "Creating new whiteboard",
					JOptionPane.INFORMATION_MESSAGE);

			if (ans == JOptionPane.YES_OPTION) {
				saveFile(managerUI.file, true);
			}
			else if (ans == JOptionPane.CANCEL_OPTION) {
				return;
			}
			Random random = new Random();
			int r = random.nextInt(999999);
			this.file = "untitled" + r;
			managerUI.file = this.file;
			frame.setTitle("WhiteBoard: " + CreateWhiteBoard.userName + " (Manager) - " + managerUI.file);

			managerUI.canvas.removeAll();
			managerUI.canvas.updateUI();
			clearRecord();
			try {
				Synchronizer.syncMessage(Commands.NEW);
			} catch (IOException e1) {
				System.out.println("ioe error.");
			}
//		System.out.println("New White Board");
		} catch (Exception ex) {
			System.out.println("new file error.");
		}

	}

	// save actions record
	public void saveFile(String filename, boolean overWrite) {
		try	{
			if (filename.startsWith("./") || filename.startsWith("../") || filename.startsWith("/") || filename.startsWith("~")){
				return;
			}

			Path path = Paths.get("./" + filename);
			if (!overWrite) {
				while (Files.exists(path)) {
//				System.out.println("adding a _copy");
					filename = filename + "_copy";
					path = Paths.get("./" + filename);
				}
			}
			PrintWriter outputStream = null;
			try {
				saveImage(filename, overWrite);
				outputStream = new PrintWriter(new FileOutputStream("./" + filename));
			} catch (IOException ioe) {
//			System.out.println("Error opening the file " + filename + ".");
				return;
			}
			ArrayList<String> recordList = getRecord();
			for (String record : recordList) {
				outputStream.println(record);
			}
			outputStream.flush();
			outputStream.close();
//		System.out.println("Saved");
		} catch (Exception ex) {
			System.out.println("save file error.");
		}

	}

	// save png format
	public void saveImage(String filename, boolean overWrite) {
		try {
			if (filename.startsWith("./") || filename.startsWith("../") || filename.startsWith("/") || filename.startsWith("~")){
				return;
			}

			Path path = Paths.get("./" + filename);
			if (!overWrite) {
				while (Files.exists(path)) {
//				System.out.println("adding a _copy");
					filename = filename + "_copy";
					path = Paths.get("./" + filename);
				}
			}
			int width = managerUI.canvasWidth;
			int height = managerUI.canvasHeight;


			BufferedImage targetImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = targetImg.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			ArrayList<String> recordList = getRecord();
			managerUI.canvas.draw(g, recordList);

			try {
				ImageIO.write(targetImg, "png", new File("./" + filename + ".png"));
			} catch (IOException ioe) {
//			System.out.println("Wrong file");

			}
//		System.out.println("Image Saved");
		} catch (Exception ex) {
			System.out.println("save image error.");
		}

	}

	public void openFile(String filename) {
		try {
			String[] splited = filename.split("\\.");
			if (!splited[splited.length - 1].equals("png") && splited.length != 1) {
				return;
			}

			this.file = filename;
			managerUI.file = this.file;

			file = "./" + filename.split(".png")[0];
			Scanner inputStream = null;

			try {
				inputStream = new Scanner(new FileInputStream(file));
				frame.setTitle("WhiteBoard: " + managerUI.userName + " (Manager) - " + managerUI.file);
			} catch (FileNotFoundException fnfe) {
//			System.out.println("Problem opening files.");
				return;
			}

			clearRecord();
			while (inputStream.hasNextLine()) {
				String line = inputStream.nextLine();
				updateRecord(line);
			}

			try {
				Synchronizer.syncMessage(Commands.NEW);
			} catch (IOException ioe) {
				System.out.println("ioe error.");
			}

			ArrayList<String> rl = getRecord();
			try {
				Synchronizer.syncWhiteBoard(rl);
			} catch (IOException ioe) {
				System.out.println("ioe error.");
			}

			ManagerUI.canvas.repaint();
			inputStream.close();
		} catch (Exception ex) {
			System.out.println("save as error.");
		}

	}

	public void sendMsg() {

		String aMessage = CreateWhiteBoard.managerUI.chatInput.getText();
		String sender = ManagerUI.userName;
		managerUI.chatsDisplayArea.setText(managerUI.chatsDisplayArea.getText() + managerUI.userName + ":\n" + aMessage + "\n");
		try {
			for (ChatMessageThread chatMessageThread : CreateWhiteBoard.messageConnections) {
				chatMessageThread.dataOut.writeUTF(Commands.CHAT + "," + sender + "," + aMessage);
				chatMessageThread.dataOut.flush();
			}
		} catch (IOException ioe) {
//			ioe.printStackTrace();
			System.out.println("IOexception, socket connection unstable");
		} catch (Exception e) {
			System.out.println("save message error");
		}
	}

	private void getColor() {
		try {
			final JFrame jf = new JFrame("Color Chooser");
			jf.setSize(300, 300);
			jf.setLocationRelativeTo(null);
			jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			Color currColor = JColorChooser.showDialog(jf, "Choose color", null, false);
			if (currColor != null) {
				color = currColor;
			}
		} catch (Exception e) {
			System.out.println("get color error.");
		}

	}


	// todo to combine
	public void kickUser() {
		try {
			String selectedUser = ManagerUI.currUsersList.getSelectedValue().toString();
//		System.out.println("Seleceted user is" + selectedUser);
			if (ManagerUI.userName.equals(selectedUser)) {
				return;
			}
			Synchronizer.syncKickUser(selectedUser);

			String[] localUserList = new String[CreateWhiteBoard.usernames.size()];
			localUserList = CreateWhiteBoard.usernames.toArray(localUserList);
			CreateWhiteBoard.managerUI.currUsersList.setListData(localUserList);
		} catch (Exception e) {
			System.out.println("kick user error.");
		}

	}



	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}


	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
}