package MulticastChat.client;

import MulticastChat.message.*;
import javax.swing.*;

public class Receiver extends Thread {
    private Communication communication;
    private GUI gui;

    public Receiver(Communication communication, GUI gui) {
        this.communication = communication;
        this.gui = gui;
    }

    public void run() { 
        while (true) {
            try {
                // Wait to receive a datagram, blocking call
                communication.waitForMessage();
                // datagram was received
                Message message = communication.getMessage();
                // check message type
                if (message instanceof PostingMessage) {
                    // it is a posting message
                    final PostingMessage p = (PostingMessage) message;
                    // add the message text to the messages textarea
                    Runnable appendText = new Runnable() {
                            public void run() {
                                String user = p.getUser();
                                String text = p.getText();
                                JTextArea messagesText = gui.getMessagesText();
                                messagesText.setText(user + ": " + text + "\n" + messagesText.getText());
                                messagesText.setCaretPosition(0);
                            } 
                        };
                    JTextField filterField = gui.getFilterField();
                    if (filterField.isEnabled()) {
                        for (String u : filterField.getText().split(";")) {
                            if (p.getUser().equals(u)) {
                                SwingUtilities.invokeLater(appendText);
                            }
                        }
                    } else {
                        SwingUtilities.invokeLater(appendText);
                    }
                } else if (message instanceof RegisterMessage) {
                    final RegisterMessage r = (RegisterMessage) message;

                    Runnable appendName = new Runnable() {
                            public void run() {
                                if (r.getUser () == null) {
                                    gui.getNameField().setText("**********");
                                } 
                            } 
                        };
                    SwingUtilities.invokeLater(appendName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } 
        } 
    }   
}
