/* ----------------------------
     SimpleWebMailClient.java
   ---------------------------- */

/*  Computerkommunikation & Verteilte Systeme 2012/2013, Rene Pawlitzek, NTB  */

package ch.ntb.inf.coko.webmail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.ntb.inf.coko.util.Utilities;

public class SimpleWebMailClient extends HttpServlet {

	private static final String htmlInputMask = "<html>" + "  <head><title>Simple Web Mail Client</title></head>"
			+ "  <body>" + "    <h1>Simple Web Mail Client</h1>"
			+ "      <form action=\"SimpleWebMailClient\" method=\"POST\">" + "      <table cellspacing=\"10\">"
			+ "        <tr>" + "          <th><p>Mail Server:</p></th>"
			+ "          <td><input type=\"TEXT\" name=\"MailServer\" size=\"60\" /></td>" + "        </tr>"
			+ "        <tr>" + "          <th><p>From:</p></th>"
			+ "          <td><input type=\"TEXT\" name=\"FromAddress\" size=\"60\" /></td>" + "        </tr>"
			+ "        <tr>" + "          <th><p>To:</p></th>"
			+ "          <td><input type=\"TEXT\" name=\"ToAddress\" size=\"60\" /></td>" + "        </tr>"
			+ "        <tr>" + "          <th><p>Subject:</p></th>"
			+ "          <td><input type=\"TEXT\" name=\"Subject\" size=\"60\" /></td>" + "        </tr>"
			+ "        <tr>" + "          <th><p>Text:</p></th>"
			+ "          <td><textarea name=\"Text\" rows=\"15\" cols=\"60\">type here</textarea></td>"
			+ "        </tr>" + "        <tr>" + "          <td colspan=\"2\">" + "            <p>"
			+ "              <input type=\"SUBMIT\" value=\" Send \" />"
			+ "              <input type=\"RESET\" value=\" Clear \" />" + "            </p>" + "          </td>"
			+ "        </tr>" + "      </table>" + "    </form>" + "  </body>" + "</html>";

	private static final long serialVersionUID = -32487988113154308L;

	public void init() {
		System.out.println("init");
	} // init

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(htmlInputMask);
		out.flush();
		out.close();
	} // doGet

	private String getHtmlSendMailOk(String toAddress) {
		return "<html>" + "  <head><title>Simple Web Mail Client</title></head>" + "  <body>" + "    <h1>Success</h1>"
				+ "      Message to '" + toAddress + "' was sent successfully!" + "  </body>" + "</html>";
	} // getHtmlSendMailOk

	private String getHtmlSendMailFail(String toAddress) {
		return "<html>" + "  <head><title>Simple Web Mail Client</title></head>" + "  <body>" + "    <h1>Error</h1>"
				+ "      Message to '" + toAddress
				+ "' could not be sent successfully! Check your mail server entry or contact your system administrator."
				+ "  </body>" + "</html>";
	} // getHtmlSendMailFail

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost");
		// query parameters
		String mailServer = request.getParameter("MailServer");
		String from = request.getParameter("FromAddress");
		String to = request.getParameter("ToAddress");
		String subject = request.getParameter("Subject");
		String message = request.getParameter("Text");
		// log input parameters
		System.out.println("Mail Server: " + mailServer);
		System.out.println("From Address: " + from);
		System.out.println("To Address: " + to);
		System.out.println("Subject: " + subject);
		System.out.println("Message: " + message);
		// send mail
		String html = getHtmlSendMailOk(to);
		try {
			Utilities.sendMail(from, to, subject, message, mailServer);
		} catch (Exception e) {
			System.out.println(e.toString());
			html = getHtmlSendMailFail(to);
		} // try
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(html);
		out.flush();
		out.close();
	} // doPost

	public void destroy() {
		System.out.println("destroy");
	} // destroy

} // SimpleWebMailClient

/* ----- End of File ----- */
