package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UIElements {

    public static JButton createJButoon(String name, ActionListener actionListener, int x, int y, int width, int height) {

        JButton button = new JButton(name);
        button.setActionCommand(name);
        button.addActionListener(actionListener);
        button.setBounds(x, y, width, height);

        return button;
    }

    public static JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);

        return textField;
    }

    public static JList createList() {
        JList list = new JList();

        return list;
    }

    public static JTextArea createTextArea(boolean lineWrap, boolean editable) {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(lineWrap);
        textArea.setEditable(editable);

        return textArea;
    }

    public static JScrollPane convertToScrollPanel(JTextArea textArea, int x, int y, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(x, y, width, height);

        return scrollPane;
    }

    public static JScrollPane convertToScrollPanel(JList list, int x, int y, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(x, y, width, height);

        return scrollPane;
    }
}
