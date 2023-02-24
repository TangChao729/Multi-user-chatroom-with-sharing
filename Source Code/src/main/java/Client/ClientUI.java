package Client;

import src.Commands;
import src.PanelPainter;
import src.UIElements;

import javax.swing.*;
import javax.swing.JTextField;

import java.awt.Color;

public class ClientUI extends JFrame {

    public static ClientUIListener clientUIListener;
    public static JFrame frame;

    public int frameWidth = 1000;
    public int frameHeight = 600;
    public int canvasWidth = 650;
    public int canvasHeight = 450;

    public static String userName;
    public static PanelPainter canvas;
    public static JList currUsersList;
    public static JTextArea chatsDisplayArea;
    public static JTextField chatInput;

    public UserConnectionThread connection;

    /**
     * Launch the application.
     */
    public ClientUI() {
    }

    public ClientUI(UserConnectionThread connection) {
        this.connection = connection;
    }

    public ClientUI(String userName) {
        this.userName = userName;
        System.out.println("User interface initializing");
        initialize();
    }

    public ClientUI(UserConnectionThread connection, String userName) {
        this.connection = connection;
        this.userName = userName;
        initialize();
    }

    private void initialize() {
        try {
            frame = new JFrame();
            frame.setTitle("WhiteBoard: " + userName + " (User)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(100, 100, frameWidth, frameHeight);

            clientUIListener = new ClientUIListener(frame);

            frame.getContentPane().setLayout(null);

            canvas = new PanelPainter();
            canvas.setBorder(null);
            canvas.setBounds(150, 75, canvasWidth, canvasHeight);
            canvas.setBackground(Color.WHITE);
            canvas.setList(clientUIListener.getRecord());
            canvas.setLayout(null);
            canvas.addMouseListener(clientUIListener);
            canvas.addMouseMotionListener(clientUIListener);

            frame.getContentPane().add(canvas);
            frame.setVisible(true);
            frame.setResizable(true);
            clientUIListener.setG(canvas.getGraphics());

            currUsersList = UIElements.createList();
            frame.getContentPane().add(UIElements.convertToScrollPanel(currUsersList, 820, 25, 150, 120));

            chatsDisplayArea = UIElements.createTextArea(true, false);
            frame.getContentPane().add(UIElements.convertToScrollPanel(chatsDisplayArea, 820, 160, 150, 330));

            chatInput = UIElements.createTextField(820, 500, 95, 30);
            frame.getContentPane().add(chatInput);

            frame.getContentPane().add(UIElements.createJButoon(Commands.CLOSE, clientUIListener, 20, 25, 100, 30));
            frame.getContentPane().add(UIElements.createJButoon(Commands.SEND, clientUIListener, 916, 500, 60, 30));
            frame.getContentPane().add(UIElements.createJButoon(Commands.LINE, clientUIListener, 20, 100, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.FREE, clientUIListener, 20, 150, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.CIRCLE, clientUIListener, 20, 200, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.TRIANGLE, clientUIListener, 20, 250, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.RECTANGLE, clientUIListener, 20, 300, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.TEXT, clientUIListener, 20, 350, 100, 40));
            frame.getContentPane().add(UIElements.createJButoon(Commands.COLOR, clientUIListener, 20, 400, 100, 40));

            try {
                connection.dataOut.writeUTF(Commands.REQUESTUPDATE);
                connection.dataOut.flush();
            } catch (Exception e) {
                System.out.println("initialize error.");
            }
        } catch (Exception ex) {
            System.out.println("initialize error.");
        }

    }

    public static void updateMsg(String sender, String message) {
        try {
            chatsDisplayArea.setText(chatsDisplayArea.getText() + sender + ":\n" + message + "\n");
        } catch (Exception e) {
            System.out.println("update message error.");
        }

    }
}
