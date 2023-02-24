package Client;

import Manager.ManagerUI;
import src.Commands;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClientUIListener implements ActionListener, MouseListener, MouseMotionListener {

	Graphics2D g;
	public JFrame frame;
	int startX, startY, endX, endY;
	int t = 1;
	String type = Commands.LINE;
	static Color color = Color.BLACK;
	String rgb = "0,0,0";
	String record;
	ArrayList<String> recordList = new ArrayList<>();
	private String draw;
	private String chatMsg;

	public ClientUIListener(JFrame frame) {
		this.frame = frame;
	}

	public ClientUIListener() {

	}

	@Override
	public void actionPerformed (ActionEvent e) {
		type = e.getActionCommand();
		try {
			switch (type) {
				case Commands.CLOSE:
					System.exit(1);
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
			System.out.println("action performed error");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
		try {
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
			sendDraw();
		} catch (Exception ex) {
			System.out.println("mouse pressed error.");
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		try {
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
			sendDraw();
		} catch (Exception ex) {
			System.out.println("mouse released error.");
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		try {
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
			sendDraw();
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

	public void update(String line) {
		recordList.add(line);
	}

	public void clearRecord() {
		recordList.clear();
	}

	private String getRGB(Color color) {
		return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
	}

	private void sendDraw() {
		if (record == null) {
			return;
		}
		try {
			draw = Commands.DRAW + "," + record;
			JoinWhiteBoard.userConnection.dataOut.writeUTF(draw);
			JoinWhiteBoard.userConnection.dataOut.flush();
		} catch (IOException e1) {
			System.out.println("ioe error.");
		} catch (Exception e) {
			System.out.println("send draw error.");
		}
	}

	public void sendMsg() {
		String aMessage = ClientUI.chatInput.getText();
		String sender = ClientUI.userName;
		try {
			chatMsg = Commands.CHAT + "," + sender + "," + aMessage;
//			JoinWhiteBoard.userConnection.dataOut.writeUTF(chatMsg);
//			JoinWhiteBoard.userConnection.dataOut.flush();

			JoinWhiteBoard.userMessageConnection.dataOut.writeUTF(chatMsg);
			JoinWhiteBoard.userMessageConnection.dataOut.flush();
		} catch (IOException e1) {
			System.out.println("ioe error.");
		} catch (Exception e) {
			System.out.println("send msg error.");
		}
	}

	private void getColor() {
		final JFrame jf = new JFrame("Color Chooser");
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Color currColor = JColorChooser.showDialog(jf, "Choose color", null, false);
		if (currColor != null) {
			color = currColor;
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