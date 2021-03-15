package MulticastChat.server;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

@SuppressWarnings("deprecation")
public class GUI implements Observer {
    JTextArea registeredClients; 
    JTextArea console;
    ByteArrayOutputStream baos;

    SimpleChatServer server;

    public GUI(SimpleChatServer server) {
        this.server = server;

        JFrame fenster = new JFrame("SimpleChatServer on port " + SimpleChatServer.PORT + " NTB Version");
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container inhaltsFlaeche = fenster.getContentPane();
        inhaltsFlaeche.setPreferredSize(new Dimension(500, 400));

        registeredClients = new JTextArea(5, 0);
        inhaltsFlaeche.add(new JScrollPane(registeredClients), BorderLayout.NORTH);
        registeredClients.setBorder (new TitledBorder ("Registrierte Klienten"));

        console = new JTextArea(); console.setEditable(false);
        inhaltsFlaeche.add(new JScrollPane(console), BorderLayout.CENTER);
        console.setBorder (new TitledBorder("Meldungen"));

        //redirect System.out to console
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        DefaultCaret caret = (DefaultCaret)console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        fenster.pack();
        fenster.setVisible(true);
    }

    public void update(Observable obs, Object obj) {
        SimpleChatServer server = (SimpleChatServer) obs;
        registeredClients.setText(server.getClients().toString());
        console.append(baos.toString());
        baos.reset();
    }
}
