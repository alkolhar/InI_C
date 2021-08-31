/* -------------------
     MailClient.java
   ------------------- */

package mailClientAPI;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.awt.*;
import java.awt.event.*;

/* $Id: MailClient.java,v 1.7 1999/07/22 12:07:30 kangasha Exp $ */

/**
 * A simple mail client with a GUI for sending mail.
 * 
 * @author Jussi Kangasharju
 */
public class MailClient extends Frame {

	private static final long serialVersionUID = -348952122826158236L;

	/* The stuff for the GUI. */
	private Button btSend = new Button("Send");
	private Button btClear = new Button("Clear");
	private Button btQuit = new Button("Quit");
	private Label serverLabel = new Label("Local mailserver:");
	private TextField serverField = new TextField("", 40);
	private Label fromLabel = new Label("From:");
	private TextField fromField = new TextField("", 40);
	private Label toLabel = new Label("To:");
	private TextField toField = new TextField("", 40);
	private Label subjectLabel = new Label("Subject:");
	private TextField subjectField = new TextField("", 40);
	private Label messageLabel = new Label("Message:");
	private TextArea messageText = new TextArea(10, 40);

	/**
	 * Create a new MailClient window with fields for entering all the relevant
	 * information (From, To, Subject, and message).
	 */
	public MailClient() {
		super("Java Mailclient");

		/*
		 * Create panels for holding the fields. To make it look nice, create an extra
		 * panel for holding all the child panels.
		 */
		Panel serverPanel = new Panel(new BorderLayout());
		Panel fromPanel = new Panel(new BorderLayout());
		Panel toPanel = new Panel(new BorderLayout());
		Panel subjectPanel = new Panel(new BorderLayout());
		Panel messagePanel = new Panel(new BorderLayout());
		serverPanel.add(serverLabel, BorderLayout.WEST);
		serverPanel.add(serverField, BorderLayout.CENTER);
		fromPanel.add(fromLabel, BorderLayout.WEST);
		fromPanel.add(fromField, BorderLayout.CENTER);
		toPanel.add(toLabel, BorderLayout.WEST);
		toPanel.add(toField, BorderLayout.CENTER);
		subjectPanel.add(subjectLabel, BorderLayout.WEST);
		subjectPanel.add(subjectField, BorderLayout.CENTER);
		messagePanel.add(messageLabel, BorderLayout.NORTH);
		messagePanel.add(messageText, BorderLayout.CENTER);
		Panel fieldPanel = new Panel(new GridLayout(0, 1));
		fieldPanel.add(serverPanel);
		fieldPanel.add(fromPanel);
		fieldPanel.add(toPanel);
		fieldPanel.add(subjectPanel);

		/*
		 * Create a panel for the buttons and add listeners to the buttons.
		 */
		Panel buttonPanel = new Panel(new GridLayout(1, 0));
		btSend.addActionListener(new SendListener());
		btClear.addActionListener(new ClearListener());
		btQuit.addActionListener(new QuitListener());
		buttonPanel.add(btSend);
		buttonPanel.add(btClear);
		buttonPanel.add(btQuit);

		/* Add, pack, and show. */
		add(fieldPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		setVisible(true);

	} // MailClient

	static public void main(String argv[]) {
		new MailClient();
	} // main

	/* Handler for the Send-button. */
	class SendListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			System.out.println("Sending mail");

			/* Check that we have the local mailserver */
			if ((serverField.getText()).equals("")) {
				System.out.println("Need name of local mailserver!");
				return;
			} // if

			/* Check that we have the sender and recipient. */
			if ((fromField.getText()).equals("")) {
				System.out.println("Need sender!");
				return;
			} // if

			if ((toField.getText()).equals("")) {
				System.out.println("Need recipient!");
				return;
			} // if

			/* Create the message */
			Properties props = new Properties();
			props.put("mail.smtp.host", serverField.getText());
			Session session = Session.getDefaultInstance(props);

			try {
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(fromField.getText()));
				msg.setRecipients(Message.RecipientType.TO, toField.getText());
				msg.setSubject(subjectField.getText());
				msg.setSentDate(new Date());
				msg.setText(messageText.getText());
				Transport.send(msg);
			} catch (MessagingException mex) {
				System.out.println("send failed, exception:");
				mex.printStackTrace();
			}
			System.out.println("mail sent successfully!");
		} // actionPerformed

	} // SendListener

	/* Clear the fields on the GUI. */
	class ClearListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.out.println("Clearing fields");
			fromField.setText("");
			toField.setText("");
			subjectField.setText("");
			messageText.setText("");
		} // actionPerformed

	} // ClearListener

	/* Quit. */
	class QuitListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		} // actionPerformed

	} // QuitListener

} // MailClient

/* ----- End of File ----- */
