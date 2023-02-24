package Manager;

import src.Commands;
import src.PanelPainter;
import src.UIElements;

import javax.swing.*;
import javax.swing.JTextField;

import java.awt.Color;
import java.util.Random;

public class ManagerUI extends  JFrame{

	public static ManagerUIListener managerUIListener;
	public static JFrame frame;
	public int frameWidth = 1000;
	public int frameHeight = 600;
	public int canvasWidth = 650;
	public int canvasHeight = 450;
	public String file;
	public static String userName;
	public static PanelPainter canvas;
	public static JTextField saveAsNameField;
	public static JTextField openFileNameField;
	public static JList currUsersList;
	public static JTextArea chatsDisplayArea;
	public static JTextField chatInput;

	public ManagerUI() {

	}

	/**
	 * Non-Default Constructor
	 */
	public ManagerUI(String userName) {
		try {
			Random random = new Random();
			int r = random.nextInt(999999);
			file = "untitled" + r;

			this.userName = userName;
			initialize();
			System.out.println("Manager initializing");
		} catch (Exception e) {
			System.out.println("manage ui error.");
		}
	}

	private void initialize() {
		try {
			frame = new JFrame();
			frame.setTitle("WhiteBoard: " + userName + " (Manager) - " + file);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(100, 100, frameWidth, frameHeight);
			frame.getContentPane().setLayout(null);
			managerUIListener = new ManagerUIListener(frame, this);
			loadUIElements();
			frame.setVisible(true);
			frame.setResizable(true);
			managerUIListener.setG(canvas.getGraphics());
		} catch (Exception e) {
			System.out.println("manage ui initialize error.");
		}
	}

	public int clientConnectionRequest(String name) {
		try {
			int option = JOptionPane.showConfirmDialog(null, name + " wants to share your white board", "Confirm",
					JOptionPane.INFORMATION_MESSAGE);
			return option;
		} catch (Exception e) {
			System.out.println("client connection request error.");
			return 0;
		}
	}

	public void loadUIElements() {
		try {
			frame.getContentPane().add(UIElements.createJButoon(Commands.NEW, managerUIListener, 20, 25, 80, 30));
			frame.getContentPane().add(UIElements.createJButoon(Commands.SAVE, managerUIListener, 110, 25, 80, 30));
			frame.getContentPane().add(UIElements.createJButoon(Commands.SAVEAS, managerUIListener, 525, 25, 80, 30));
			frame.getContentPane().add(UIElements.createJButoon(Commands.OPEN, managerUIListener, 300, 25, 80, 30));
			frame.getContentPane().add(UIElements.createJButoon(Commands.CLOSE, managerUIListener, 200, 25, 80, 30));
			currUsersList = UIElements.createList();
			frame.getContentPane().add(UIElements.convertToScrollPanel(currUsersList, 820, 25, 150, 120));
			frame.getContentPane().add(UIElements.createJButoon(Commands.KICK, managerUIListener, 740, 25, 80, 30));
			chatsDisplayArea = UIElements.createTextArea(true, false);
			frame.getContentPane().add(UIElements.convertToScrollPanel(chatsDisplayArea, 820, 160, 150, 330));
			chatInput = UIElements.createTextField(820, 500, 95, 30);
			frame.getContentPane().add(chatInput);
			frame.getContentPane().add(UIElements.createJButoon(Commands.SEND, managerUIListener, 916, 500, 60, 30));
			frame.getContentPane().add(UIElements.createJButoon(Commands.LINE, managerUIListener, 20, 100, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.FREE, managerUIListener, 20, 150, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.CIRCLE, managerUIListener, 20, 200, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.TRIANGLE, managerUIListener, 20, 250, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.RECTANGLE, managerUIListener, 20, 300, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.TEXT, managerUIListener, 20, 350, 100, 40));
			frame.getContentPane().add(UIElements.createJButoon(Commands.COLOR, managerUIListener, 20, 400, 100, 40));
			openFileNameField = UIElements.createTextField(375, 27, 130, 26);
			frame.getContentPane().add(openFileNameField);
			saveAsNameField = UIElements.createTextField(600, 27, 130, 26);
			frame.getContentPane().add(saveAsNameField);
			String[] innovative = {userName};
			currUsersList.setListData(innovative);

			canvas = new PanelPainter();
			canvas.setBorder(null);
			canvas.setBounds(150, 75, canvasWidth, canvasHeight);
			canvas.setBackground(Color.WHITE);
			canvas.setList(managerUIListener.getRecord());
			canvas.setLayout(null);
			canvas.addMouseListener(managerUIListener);
			canvas.addMouseMotionListener(managerUIListener);
			frame.getContentPane().add(canvas);
		} catch (Exception e) {
			System.out.println("load ui error.");
		}
	}
}
