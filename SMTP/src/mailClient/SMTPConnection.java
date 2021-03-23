/* -----------------------
     SMTPConnection.java
   ----------------------- */

/*   Comments:
 
     0) Computernetzwerke, Kurose & Ross, Kapitel 2: Anwendungsschicht, Paragraph 2.4.1: SMTP, S.149
     1) http://de.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol
     2) Example:
     
					S: 220 mail-server.ntb.ch Microsoft ESMTP MAIL Service ready at Thu, 16 Aug 2012 17:01:18 +0200
					C: HELO NBINF008.ntb.ch
					S: 250 mail-server.ntb.ch Hello [146.136.36.71]
					C: MAIL FROM:<rene.pawlitzek@ntb.ch>
					S: 250 2.1.0 Sender OK
					C: RCPT TO:<rene.pawlitzek@ntb.ch>
					S: 250 2.1.5 Recipient OK
					C: DATA
					S: 354 Start mail input; end with <CRLF>.<CRLF>
					C: From: rene.pawlitzek@ntb.ch
					To: rene.pawlitzek@ntb.ch
					Subject: greeting
					Date: Do, 16 Aug 2012 17:01:18 GMT
					
					Hello world!
					.
					S: 250 2.6.0 <159cb975-6ecb-483c-897b-084c09774965@MAIL-SERVER.ntb.ch> [InternalId=569436] Queued mail for delivery
					C: QUIT
					S: 221 2.0.0 Service closing transmission channel
					Mail sent succesfully!

 */

package mailClient;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 * 
 */
public class SMTPConnection {

	/* The socket to the server */
	private Socket connection;

	/* Streams for reading and writing the socket */
	private BufferedReader fromServer;
	private DataOutputStream toServer;

	private static final int SMTP_PORT = 25;
	private static final String CRLF = "\r\n";

	/* Are we connected? Used in close() to determine what to do. */
	private boolean isConnected = false;

	/*
	 * Create an SMTPConnection object. Create the socket and the associated
	 * streams. Initialize SMTP connection.
	 */
	public SMTPConnection(Envelope envelope) throws IOException {

		connection = new Socket(envelope.DestAddr, SMTP_PORT);
		fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		toServer = new DataOutputStream(connection.getOutputStream());

		/*
		 * Read a line from server and check that the reply code is 220. If not, throw
		 * an IOException.
		 */
		String readLine = fromServer.readLine();
		System.out.println(readLine);
		int rc = parseReply(readLine);
		if (rc != 220)
			throw new IOException();

		/*
		 * SMTP handshake. We need the name of the local machine. Send the appropriate
		 * SMTP handshake command.
		 */
		String localhost = InetAddress.getLocalHost().getCanonicalHostName();
		sendCommand("HELO " + "ntb.ch", 250);
		isConnected = true;

	} // SMTPConnection

	/*
	 * Send the message. Write the correct SMTP-commands in the correct order. No
	 * checking for errors, just throw them to the caller.
	 */
	public void send(Envelope envelope) throws IOException {
		/*
		 * Send all the necessary commands to send a message. Call sendCommand() to do
		 * the dirty work. Do _not_ catch the exception thrown from sendCommand().
		 */
		sendCommand("MAIL FROM:<" + envelope.Sender + ">", 250);
		sendCommand("RCPT TO:<" + envelope.Recipient + ">", 250);
		sendCommand("DATA", 354);
		sendCommand(envelope.Message.toString() + CRLF + ".", 250);
	} // send

	/*
	 * Close the connection. First, terminate on SMTP level, then close the socket.
	 */
	public void close() {
		isConnected = false;
		try {
			sendCommand("QUIT", 221);
			connection.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection: " + e);
			isConnected = true;
		} // try
	} // close

	/*
	 * Send an SMTP command to the server. Check that the reply code is what is is
	 * supposed to be according to RFC 821.
	 */
	private void sendCommand(String command, int rc) throws IOException {
		/* Write command to server and read reply from server. */
		toServer.writeBytes(command + CRLF);
		System.out.println("C: " + command);

		String reply = fromServer.readLine();
		System.out.println("S: " + reply);
		/*
		 * Check that the server's reply code is the same as the parameter rc. If not,
		 * throw an IOException.
		 */
		int serverRC = parseReply(reply);
		if (serverRC != rc)
			throw new IOException();
	} // sendCommand

	/* Parse the reply line from the server. Returns the reply code. */
	private int parseReply(String reply) {
		StringTokenizer tokens = new StringTokenizer(reply);
		String token = tokens.nextToken();

		return Integer.parseInt(token);
	} // parseReply

	/* Destructor. Closes the connection if something bad happens. */
	protected void finalize() throws Throwable {
		if (isConnected) {
			close();
		} // if
		super.finalize();
	} // finalize

} // SMTPConnection

/* ----- End of File ----- */