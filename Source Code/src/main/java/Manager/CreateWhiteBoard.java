package Manager;

import java.awt.EventQueue;
import java.util.ArrayList;

public class CreateWhiteBoard {
	
	public static String ipAddress;
	public static int portNumber;
	public static String userName;
	public static ManagerUI managerUI;

	public static ArrayList<ConnectionThread> connections = new ArrayList<>();
	public static ArrayList<String> usernames = new ArrayList<>();

	public static int messagePortNumber;
	public static ArrayList<ChatMessageThread> messageConnections = new ArrayList<>();
	public static ArrayList<String> messageUsernames = new ArrayList<>();

	public static void main(String[] args) {

		try {
			if (args.length >= 3) {

				// user modified parameters
				try {
					ipAddress = args[0];
					portNumber = Integer.parseInt(args[1]);
					messagePortNumber = portNumber + 1;
					userName = args[2];
				} catch (Exception e){
					System.out.println("Invalid input");
					System.exit(1);
				}
			}

			// default parameters
			else {
				ipAddress = "localhost";
				portNumber = 3000;
				messagePortNumber = 3001;
				userName = "Manager";
//			userName = "dlsjaf ,23423#@$2dsalf jdl a";
				System.out.println("NO CUSTOM PARAMETER DETECTED, USING DEFAULT MANAGER SETTING.");
			}

			if (!isValid(userName)) {
				System.out.println("Invalid input, name should only contain letter or number");
				System.exit(1);
			}

			EventQueue.invokeLater(()->{
				try {
					new CreateWhiteBoard();
				} catch (Exception e) {
					System.out.println("create white board error.");
				}
			});

//		readyToConnect(portNumber);
			ReadyToConnectThread readyToConnectThread = new ReadyToConnectThread();
			readyToConnectThread.portNumber = portNumber;
			readyToConnectThread.start();

//		messageReadyToConnect(messagePortNumber);
			MessageReadyToConnectThread messageReadyToConnectThread = new MessageReadyToConnectThread();
			messageReadyToConnectThread.messagePortNumber = messagePortNumber;
			messageReadyToConnectThread.start();
		} catch (Exception e) {
			System.out.println("create whiteboard error.");
		}


	}
	
	public CreateWhiteBoard() {
		try {
			System.out.println("INITIATING MANAGER UI");
			managerUI = new ManagerUI(CreateWhiteBoard.userName);
//			managerUI.setFrame(managerUI);
		} catch (Exception e) {
			System.out.println("create whiteboard error.");
		}
	}

	private static boolean isValid(String userName) {
		try {
			char[] userNamesCharArray = userName.toCharArray();
			for (char c : userNamesCharArray) {
				if (!Character.isLetter(c) && !Character.isDigit(c)) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("name validation error.");
			return false;
		}

	}
}
