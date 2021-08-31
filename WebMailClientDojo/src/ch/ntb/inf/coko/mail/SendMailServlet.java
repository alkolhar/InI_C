/* ------------------------
     SendMailServlet.java
   ------------------------ */


/*  Computerkommunikation & Verteilte Systeme 2012/2013, Rene Pawlitzek, NTB  */


package ch.ntb.inf.coko.mail;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.ntb.inf.coko.util.Utilities;


public class SendMailServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 7570779405271906918L;


	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println ("doGet");
		// query parameters
		String mailServer = request.getParameter ("MailServer");
		String from = request.getParameter ("FromAddress");
		String to = request.getParameter ("ToAddress");
		String subject = request.getParameter ("Subject");
		String message = request.getParameter ("Text");
		// log input parameters
		System.out.println ("Mail Server: " + mailServer);
		System.out.println ("From Address: " + from);
		System.out.println ("To Address: " + to);
		System.out.println ("Subject: " + subject);
		System.out.println ("Message: " + message);
		// send mail
		String errorMessage = "Mail was successfully sent.";
		try {
		  Utilities.sendMail (from, to, subject, message, mailServer);
		} catch (Exception e) {
			System.out.println (e.toString ());
			errorMessage = "Unable to send mail. Check mail server or contact administrator.";
		} // try
		response.setContentType ("text/plain");
		PrintWriter out = response.getWriter ();
		out.println (errorMessage);
		out.flush ();
		out.close ();
	} // doGet

	
} // SendMailServlet


/* ----- End of File ----- */
