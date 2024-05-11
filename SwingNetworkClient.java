import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SwingNetworkClient {
    private BufferedReader reader;
    private PrintWriter writer;
    private JTextArea chatArea;
    private JTextField messageField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingNetworkClient().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Swing Network Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage(messageField.getText()));

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage(messageField.getText()));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        try {
            Socket socket = new Socket("localhost", 5001); // Connect to the server
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            writer.println(message);
            messageField.setText("");
        }
    }

    private void receiveMessages() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    chatArea.append(message + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
