package MulticastChat;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;

public class MulticastClient extends JFrame {
	private static final long serialVersionUID = -1123248448207370195L;
	private static final int port = 9876;
	private static final String CRLF = "\r\n";
	private static final String groupAddress = "230.0.0.0";

	private JLabel nameLabel = new JLabel(" Name: ");
	private JLabel serverLabel = new JLabel(" Group: ");
	private JButton sendButton = new JButton("Send");
	private JButton registerButton = new JButton("Register");
	private JButton unregisterButton = new JButton("Unregister");
	private JTextArea messagesText = new JTextArea();
	private JTextField nameField = new JTextField();
	private JTextField groupField = new JTextField();
	private JTextField messageField = new JTextField();
	private JScrollPane scrollPane = new JScrollPane(messagesText);

	private Thread receiver;
	private DatagramSocket socket = null;
	private MulticastSocket multicastSocket = null;

	public MulticastClient() {
		super("NTB SimpleChatClient");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				quit();
			}
		});

		receiver = new Thread() {
			public void run() {
				byte[] buf = new byte[256];
				while (true) {
					try {
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						// Wait to receive a datagram, blocking call
						multicastSocket.receive(packet);
						// datagram was received
						final String message = new String(packet.getData(), 0, packet.getLength());
						System.out.println("Received: " + message);
						// add the message text to the messages textarea
						Runnable appendText = new Runnable() {
							public void run() {
								messagesText.insert(message + CRLF, 0);
								messagesText.setCaretPosition(0);
							} // run
						};
						SwingUtilities.invokeLater(appendText);
					} catch (Exception e) {
						// swallow all exceptions
					} // try
				} // while
			} // run
		};

		// setup communication
		try {
			setup();
		} catch (Exception e) {
			e.printStackTrace();
		} // try

		// setup ActionListeners for buttons
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			} // actionPerformed
		});
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			} // actionPerformed
		});
		unregisterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unregister();
			} // actionPerformed
		});

		// implementation of KeyListener interface
		messageField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send();
				} // if
			} // keyPressed
		});

		// create group panel
		JPanel groupPanel = new JPanel(new BorderLayout());
		serverLabel.setPreferredSize(new Dimension(50, 28));
		groupField.setPreferredSize(new Dimension(400, 28));
		groupField.setText(groupAddress);
		groupPanel.add(serverLabel, BorderLayout.WEST);
		groupPanel.add(groupField, BorderLayout.CENTER);
		groupPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		// create button panel
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(registerButton, BorderLayout.WEST);
		buttonPanel.add(unregisterButton, BorderLayout.EAST);

		// create name panel
		JPanel namePanel = new JPanel(new BorderLayout());
		nameLabel.setPreferredSize(new Dimension(50, 28));
		nameField.setPreferredSize(new Dimension(400, 28));
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameField, BorderLayout.CENTER);
		namePanel.add(buttonPanel, BorderLayout.EAST);
		namePanel.setBorder(new EmptyBorder(2, 2, 2, 2));

		// create configuration panel
		JPanel configPanel = new JPanel(new GridLayout(2, 1));
		configPanel.add(groupPanel);
		configPanel.add(namePanel);
		configPanel.setBorder(new TitledBorder(" Configuration "));

		// create messages panel
		JPanel messagesPanel = new JPanel(new BorderLayout());
		messagesPanel.add(scrollPane, BorderLayout.CENTER);
		messagesPanel.setBorder(new TitledBorder(" Received Messages "));

		// create message panel
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		messagePanel.setBorder(new TitledBorder(" Send Message "));

		// create panel with name, messages and message panel
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(configPanel, BorderLayout.NORTH);
		panel1.add(messagesPanel, BorderLayout.CENTER);
		panel1.add(messagePanel, BorderLayout.SOUTH);

		// create panel to hold all over panels
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPanel.add(panel1, BorderLayout.CENTER);

		// add content panel to the window
		setContentPane(contentPanel);
		setMinimumSize(new Dimension(400, 300));
		setPreferredSize(new Dimension(600, 400));

		pack();
		setVisible(true);
	} // MulticastClient

	private void setup() throws Exception {
		System.out.println("Setup Multicast Chat Client");
		// start a background thread to receive messages
		socket = new DatagramSocket();
		receiver.start();
	} // setup

	private void quit() {
		// quit the application
		if (socket != null)
			socket.close();
		if (multicastSocket != null)
			multicastSocket.close();
		System.out.println("Quit Chat Client");
		System.out.println("Done.");
		System.exit(0);
	} // quit

	private void sendMessage(String group, String message) throws Exception {
		byte[] buf = message.getBytes();
		InetAddress address = InetAddress.getByName(group);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		if ((socket != null) && (multicastSocket != null))
			socket.send(packet);
	} // sendMessage

	private void send() {
		try {
			// send the user message
			String user = nameField.getText();
			String text = messageField.getText();
			String group = groupField.getText();
			String message = user + ": " + text;
			System.out.println(message);
			sendMessage(group, message);
			messageField.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // send

	@SuppressWarnings("deprecation")
	private void register() {
		try {
			multicastSocket = new MulticastSocket(port);
			String group = groupField.getText();
			InetAddress groupAddress = InetAddress.getByName(group);
			multicastSocket.joinGroup(groupAddress);
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // register

	@SuppressWarnings("deprecation")
	private void unregister() {
		try {
			String group = groupField.getText();
			InetAddress groupAddress = InetAddress.getByName(group);
			multicastSocket.leaveGroup(groupAddress);
			multicastSocket.close();
			multicastSocket = null;
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // unregister

	public static void main(String argv[]) {
		new MulticastClient();
	} // main

} // MulticastClient

/* ----- End of File ----- */