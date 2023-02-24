package Manager;

import java.net.ServerSocket;
import java.net.Socket;

public class ReadyToConnectThread extends Thread{

    public int portNumber;

    public void run () {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        CreateWhiteBoard.usernames.add(CreateWhiteBoard.userName);

        try {
            serverSocket = new ServerSocket(portNumber);

            while (true) {
                clientSocket = serverSocket.accept();
                ConnectionThread connectionThread = new ConnectionThread(clientSocket);
                CreateWhiteBoard.connections.add(connectionThread);
                connectionThread.start();
            }
        } catch (Exception e) {
            System.out.println("Connection failed, one or more errors happened during creating white board.");
            System.exit(1);
        }
    }
}
