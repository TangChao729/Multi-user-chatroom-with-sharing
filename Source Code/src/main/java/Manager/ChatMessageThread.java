package Manager;

import src.Commands;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ChatMessageThread extends Thread{

    public Socket socket;
    public String name;
    public DataInputStream dataIn;
    public DataOutputStream dataOut;

    public ChatMessageThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            dataIn = new DataInputStream(is);
            dataOut = new DataOutputStream(os);
            String utfReceived;

            while ((utfReceived = dataIn.readUTF()) != null) {
//                System.out.println("---Message DATA IN: " + utfReceived + "---");
                String[] receivedCommand = utfReceived.split(",", 2);

//                for (String s : receivedCommand) {
//                    System.out.println(s);
//                }

                switch (receivedCommand[0]) {
                    case Commands.NEWUSER:
                        this.name = receivedCommand[1];
                        break;

                    case Commands.CHAT:
                        for (ChatMessageThread chatMessageThread: CreateWhiteBoard.messageConnections) {
                            chatMessageThread.dataOut.writeUTF(utfReceived);
                            chatMessageThread.dataOut.flush();
                        }
                        String[] content = receivedCommand[1].split(",", 2);
                        ManagerUI.chatsDisplayArea.setText(ManagerUI.chatsDisplayArea.getText() + content[0] + ":\n" + content[1] + "\n");
                        this.name = content[0];
//                        System.out.println(this.name);
                        break;
                    default:
                        break;
                }

            }

        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            System.out.println("IOException happen, message connection lost.");
            CreateWhiteBoard.messageConnections.remove(this);
        } catch (Exception e) {
            System.out.println("chat message thread error.");
        }
    }
}
