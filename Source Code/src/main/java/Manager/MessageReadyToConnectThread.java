package Manager;

import java.net.ServerSocket;
import java.net.Socket;

public class MessageReadyToConnectThread extends Thread{

    public int messagePortNumber;

    public void run () {
        ServerSocket messageServerSocket = null;
        Socket clientMessageSocket = null;

        CreateWhiteBoard.messageUsernames.add(CreateWhiteBoard.userName);

        try {
            messageServerSocket = new ServerSocket(messagePortNumber);

            while (true) {
                clientMessageSocket = messageServerSocket.accept();
                ChatMessageThread chatMessageThread = new ChatMessageThread(clientMessageSocket);
                CreateWhiteBoard.messageConnections.add(chatMessageThread);
                chatMessageThread.start();
            }
        } catch (Exception e) {
            System.out.println("Connection failed, one or more errors happened during creating white board.");
            System.exit(1);
        }
    }
}
