package Client;

import src.Commands;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UserMessageConnectionThread extends Thread{

    private Socket socket;
    public DataInputStream dataIn = null;
    public DataOutputStream dataOut = null;

    public void run() {
        try {
            while (true) {
                String request = dataIn.readUTF();
//                System.out.println("Whole piece info: " + request);
                String[] splitedRequest = request.split(",", 2);
//                System.out.println("separated: " + splitedRequest);

                if (splitedRequest[0].equals(Commands.CHAT)) {

                    String[] content = splitedRequest[1].split(",", 2);
//                    System.out.println("receved username: " + content[0]);
//                    System.out.println("Received msg: " + content[1]);
                    ClientUI.updateMsg(content[0], content[1]);
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(JoinWhiteBoard.userInterface.frame, "Disconnected with server.");
            System.exit(0);
        }
    }

    public UserMessageConnectionThread(Socket socket) {
        try {
            this.socket = socket;
            dataIn = new DataInputStream(this.socket.getInputStream());
            dataOut = new DataOutputStream(this.socket.getOutputStream());

//            System.out.println("Setting up data output stream, " + this.socket.getInetAddress() + " " + this.socket.getPort());
        } catch (Exception e) {}
    }
}
