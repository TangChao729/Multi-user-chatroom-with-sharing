package Client;

import src.Commands;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class JoinWhiteBoard {

    public static String ipAddress;
    public static int portNumber;
    public static String userName;

    public static ClientUI userInterface;
    public static Socket socket;
    public static UserConnectionThread userConnection;
    public static UserMessageConnectionThread userMessageConnection;

    public static int messagePortNumber;
    public static Socket messageSocket;

    public static void main(String[] args) {

        if (args.length >= 3) {
            try {
                ipAddress = args[0];
                portNumber = Integer.parseInt(args[1]);
                messagePortNumber = portNumber + 1;
                userName = args[2];
            } catch (Exception e){
                System.out.println("Invalid input, please add ip, port, username only");
                System.exit(1);
            }
        } else {
            ipAddress = "localhost";
            portNumber = 3000;
            messagePortNumber = 3001;
            userName = "Client";
//            userName = "dlsa 23 $#@>43,2>#@> 32";
            System.out.println("Using default setting localhost 3000 Client");
        }

        if (!isValid(userName)) {
            System.out.println("Invalid input, name should only contain letter or number");
            System.exit(1);
        }


        try {
            socket = new Socket(ipAddress, portNumber);
        } catch (IOException ioe) {
            System.out.println("Connection failed");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("join error.");
        }

        try {
            messageSocket = new Socket(ipAddress, messagePortNumber);
        } catch (IOException ioe) {
            System.out.println("ioe error.");
            System.out.println("message socket failed start");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("join whiteboard error.");
        }

        userConnection = new UserConnectionThread(socket);
        userMessageConnection = new UserMessageConnectionThread(messageSocket);

        EventQueue.invokeLater(()->{
            try {
                new JoinWhiteBoard();
//                userConnection.dataOut.writeUTF(Commands.NEWUSER + "," + userName);
            } catch (Exception e) {
                System.out.println("join error.");
            }
        });
        userConnection.start();
        userMessageConnection.start();

    }

    public JoinWhiteBoard() {
        try {
            userConnection.dataOut.writeUTF(Commands.NEWUSER + "," + userName);
            userMessageConnection.dataOut.writeUTF(Commands.NEWUSER + "," + userName);
            int time = 0;
            while (userConnection.getCurrentStatus().equals(Commands.PENDING) && time < 30000) {
                TimeUnit.MILLISECONDS.sleep(100);
                time += 100;
            }

            String status = userConnection.getCurrentStatus();

            if (status.equals(Commands.INVALID)) {
                System.out.println("User name exist, please try another.");
                socket.close();
                messageSocket.close();
                System.exit(1);
            }

            else if (status.equals(Commands.REJECT)) {
                System.out.println("Manager rejected your request.");
                socket.close();
                messageSocket.close();
                System.exit(1);
            }

            else if (status.equals(Commands.ALLOW)) {
                userInterface = new ClientUI(userConnection, userName);
            }

            else {
                System.out.println("Connection timed out");
                socket.close();
                messageSocket.close();
                System.exit(1);
            }

        } catch (Exception e) {
            System.out.println("join whiteboard error.");
        }
    }

    private static boolean isValid(String userName) {
        char[] userNamesCharArray = userName.toCharArray();
        for (char c : userNamesCharArray) {
            if (!Character.isLetter(c) && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
