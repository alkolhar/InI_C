/* ------------------
     Utilities.java
   ------------------ */


/*  Computerkommunikation & Verteilte Systeme 2012/2013, Rene Pawlitzek, NTB  */


package ch.ntb.inf.coko.util;


import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;


public class Utilities {

	public static void sendMail (String from, String to, String subject,
			String text, String mailServer) throws Exception {

		// Get system properties
		Properties properties = System.getProperties ();

		// Setup mail server
		properties.setProperty ("mail.smtp.host", mailServer);
		
		// Get the default Session object
		Session session = Session.getDefaultInstance (properties);

		// Create a default MimeMessage object
		MimeMessage message = new MimeMessage (session);

		// Set From: header field
		message.setFrom (new InternetAddress (from));

		// Set To: header field
		message.addRecipient (Message.RecipientType.TO, new InternetAddress (to));

		// Set Subject: header field
		message.setSubject (subject);
		
		// Now set the actual message
		message.setContent (text, "text/plain");

		// Send message
		Transport.send (message);

	} // sendMail
	
	
} // Utilities


/* ----- End of File ----- */
