package Client;

import src.Commands;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserConnectionThread extends Thread{

    private Socket socket;
    public DataInputStream dataIn = null;
    public DataOutputStream dataOut = null;

    String status;
    boolean kick = false;

    public void run() {
        try {
            while (true) {
                String request = dataIn.readUTF();
//                System.out.println("Whole piece info: " + request);
                String[] splitedRequest = request.split(",", 2);
//                System.out.println("separated: " + splitedRequest);
                if (splitedRequest[0].equals(Commands.DRAW)) {
                    ClientUI.clientUIListener.update(splitedRequest[1]);
                    ClientUI.canvas.repaint();
                }

                if (splitedRequest[0].equals(Commands.ALLUSERNAMES) && JoinWhiteBoard.userInterface != null) {
                    JoinWhiteBoard.userInterface.currUsersList.setListData(splitedRequest[1].split(","));
                }

                if (splitedRequest[0].equals(Commands.REMOVEUSER)) {
                    String[] users = splitedRequest[1].split(",", 2);
                    String[] userList = users[1].split(",");
                    JoinWhiteBoard.userInterface.currUsersList.setListData(userList);
                }

                if (splitedRequest[0].equals(Commands.KICK)) {
                    kick = true;
                    JOptionPane.showMessageDialog(JoinWhiteBoard.userInterface.frame, "You have been kicked-out by mananger.");
                }

                if (splitedRequest[0].equals(Commands.INVALID)) {
                    status = Commands.INVALID;
                }

                if (splitedRequest[0].equals(Commands.ALLOW)) {
                    status = Commands.ALLOW;
                }

                if (splitedRequest[0].equals(Commands.REJECT)) {
                    status = Commands.REJECT;
                }

                if (splitedRequest[0].equals(Commands.NEW)) {
                    ClientUI.canvas.removeAll();
                    ClientUI.canvas.updateUI();
                    ClientUI.clientUIListener.clearRecord();

                }
            }
        }
        catch (IOException ioe) {
            JOptionPane.showMessageDialog(JoinWhiteBoard.userInterface.frame, "Disconnected with server.");
            System.exit(0);
        }
        catch (Exception e) {
            System.exit(0);
        }
    }

    public UserConnectionThread(Socket socket) {
        resetStatus();
        try {
            this.socket = socket;
            dataIn = new DataInputStream(this.socket.getInputStream());
            dataOut = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {}
    }

    String getCurrentStatus() {return status;}

    public void resetStatus() {
        status = Commands.PENDING;
        return;
    }
}
