package MulticastChat.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.*;
import MulticastChat.message.*;

public class GUI {

	// private static final String CRLF = "\r\n";
	private static final int SERVER_PORT = 9876;
	private static final String SERVER_HOST = "230.0.0.0";

	private JTextArea messagesText = new JTextArea();
	private JTextField nameField = new JTextField();
	private JTextField serverField = new JTextField();
	private JTextField messageField = new JTextField();

	private JLabel filterLabel;
	private JTextField filterField;
	private JButton filterButton;

	private Communication communication;
	private MulticastSocket multiSocket = null; 

	public GUI(Communication communication) throws Exception {
		this.communication = communication;

		JFrame fenster = new JFrame("SimpleChatClient NTB Version");
		Container inhaltsFlaeche = fenster.getContentPane();
		inhaltsFlaeche.setPreferredSize(new Dimension(500, 400));

		/************* CONFIGURATION PANEL ************/
		// create server panel
		JPanel serverPanel = new JPanel(new BorderLayout());
		JLabel serverLabel = new JLabel(" Server: ");
		serverLabel.setPreferredSize(new Dimension(50, 28));
		serverField.setPreferredSize(new Dimension(400, 28));
		serverField.setText(SERVER_HOST);
		serverPanel.add(serverLabel, BorderLayout.WEST);
		serverPanel.add(serverField, BorderLayout.CENTER);
		serverPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		// create button panel
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JButton registerButton = new JButton("Register");
		buttonPanel.add(registerButton, BorderLayout.WEST);
		JButton unregisterButton = new JButton("Unregister");
		buttonPanel.add(unregisterButton, BorderLayout.EAST);
		unregisterButton.setEnabled(false);

		// create name panel
		JPanel namePanel = new JPanel(new BorderLayout());
		JLabel nameLabel = new JLabel(" Name: ");
		nameLabel.setPreferredSize(new Dimension(50, 28));
		nameField.setPreferredSize(new Dimension(400, 28));
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameField, BorderLayout.CENTER);
		namePanel.add(buttonPanel, BorderLayout.EAST);
		namePanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		// create configuration panel
		JPanel configPanel = new JPanel(new GridLayout(2, 1));
		configPanel.add(serverPanel);
		configPanel.add(namePanel);
		configPanel.setBorder(new TitledBorder(" Configuration "));

		/************* RECEIVED MESSAGES PANEL ************/
		// create messages panel
		JPanel messagesPanel = new JPanel(new BorderLayout());
		messagesPanel.add(new JScrollPane(messagesText), BorderLayout.CENTER);
		messagesPanel.setBorder(new TitledBorder(" Received Messages "));

		JPanel filterPanel = new JPanel(new BorderLayout());
		filterLabel = new JLabel("Filter: ");
		filterPanel.add(filterLabel, BorderLayout.WEST);
		filterField = new JTextField("*", 60);
		filterField.setEnabled(false);
		filterLabel.setEnabled(false);
		filterPanel.add(filterField, BorderLayout.CENTER);
		filterButton = new JButton("Filter einschalten");
		filterPanel.add(filterButton, BorderLayout.EAST);

		messagesPanel.add(filterPanel, BorderLayout.NORTH);

		/************* SEND MESSAGE PANEL ************/
		// create message panel
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messageField, BorderLayout.CENTER);
		messageField.setEnabled(false);
		JButton sendButton = new JButton("Send");
		messagePanel.add(sendButton, BorderLayout.EAST);
		sendButton.setEnabled(false);
		messagePanel.setBorder(new TitledBorder(" Send Message "));

		/************* ALLES ZUSAMMENFÜGEN ************/
		inhaltsFlaeche.add(configPanel, BorderLayout.NORTH);
		inhaltsFlaeche.add(messagesPanel, BorderLayout.CENTER);
		inhaltsFlaeche.add(messagePanel, BorderLayout.SOUTH);

		inhaltsFlaeche.setPreferredSize(new Dimension(600, 400));

		// setup ActionListeners for buttons. Use Lambda-Expressions:
		// use short methods in the lambda expressions. call the methods send(),
		// register(), unregister() below

		sendButton.addActionListener(e -> {
			send();
		});

		registerButton.addActionListener(e -> {
			register();
			unregisterButton.setEnabled(true);
			registerButton.setEnabled(false);
			messageField.setEnabled(true);
			sendButton.setEnabled(true);
		});

		unregisterButton.addActionListener(e -> {
			unregister();
			unregisterButton.setEnabled(false);
			registerButton.setEnabled(true);
			messageField.setEnabled(false);
			sendButton.setEnabled(false);
		});

		// filterButton.addActionListener(// Ihr Code );

		// add addKeyListener hat einen Parameter vom Typ KeyListener. Hier koennen wir
		// keine Lambda-Expression
		// verwenden, denn das funktioniert nur, wenn lediglich EINE einzige Methode zu
		// implementieren ist.
		// KeyListener ist aber ein Interface welches fuer drei verschiedene Key-Events
		// je
		// einen Handler spezifiziert: keyPressed, keyReleased, keyTyped.
		// https://docs.oracle.com/javase/7/docs/api/java/awt/Component.html#addKeyListener(java.awt.event.KeyListener)
		// Von den drei Events, interessiert uns nur das keyPressed event. Im Tutorial
		// "How to Write a Key Listener"
		// https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
		// ist beschrieben, wie man einen KeyAdapter verwenden kann, damit man nicht
		// alle drei Methoden implementieren muss.
		// Dazu verwendet man die Technik einer "anonymen inneren Klasse" und
		// ueberschreibt nur die gewuenschte
		// Methode (keyPressed in unserem Fall):
		messageField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				}
			}
		});

		fenster.pack();
		fenster.setVisible(true);
	}

	private  DatagramSocket  socket = new DatagramSocket(SERVER_PORT);
	
	private void send() {
		try {
			// send the user message
			String user = nameField.getText();
			String text = messageField.getText();
			String group = serverField.getText();
			
			String message = user + ": " + text;
			System.out.println(message);
			
			byte[] buf = message.getBytes();
			InetAddress address = InetAddress.getByName(group);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
			
			if (socket != null)
				socket.send(packet);
			
			messageField.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // send

	/*
	 * private void send() { // send the user message String user =
	 * nameField.getText(); String text = messageField.getText(); String serverHost
	 * = serverField.getText(); System.out.println(user + ": " + text);
	 * 
	 * communication.sendMessage(serverHost, SERVER_PORT, new PostingMessage(user,
	 * text)); messageField.setText(""); }
	 */
	@SuppressWarnings("deprecation")
	private void register() {
		// register the user	
		String user = nameField.getText();
		String serverHost = serverField.getText();
		
		try {
			multiSocket = new MulticastSocket(SERVER_PORT);
			multiSocket.joinGroup(InetAddress.getByName(serverHost));
			System.out.println("Register " + user);
			communication.sendMessage(serverHost, SERVER_PORT, new RegisterMessage(user));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void unregister() {
		// unregister the user
		String user = nameField.getText();
		String serverHost = serverField.getText();
		System.out.println("Unregister " + user);

		communication.sendMessage(serverHost, SERVER_PORT, new UnregisterMessage(user));
	}

	public JTextArea getMessagesText() {
		return messagesText;
	}

	public JTextField getFilterField() {
		return filterField;
	}

	public JTextField getNameField() {
		return nameField;
	}
}