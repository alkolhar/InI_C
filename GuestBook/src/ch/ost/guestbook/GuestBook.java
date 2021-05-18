/* ------------------
     GuestBook.java
   ------------------ */

/*  Computerkommunikation & Verteilte Systeme 2012/2013, Rene Pawlitzek, NTB  */
package ch.ost.guestbook;

import java.io.*;

import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.lang.*;
import org.apache.log4j.*;

public class GuestBook extends HttpServlet {

	private static final long serialVersionUID = 4078583581499466847L;

	private static final String htmlHeader = "<HTML>\n" + "  <HEAD>\n" + "    <TITLE>Guestbook</TITLE>\n"
			+ "    <LINK REL=\"stylesheet\" TYPE=\"text/css\" HREF=\"GuestBook.css\" MEDIA=\"all\" />\n" + "  </HEAD>\n"
			+ "  <BODY>\n" + "    <FORM ACTION=\"GuestBook.html\" METHOD=\"POST\">\n"
			+ "      <DIV CLASS=\"Title\">Gästebuch</DIV>\n" + "      <DIV CLASS=\"Text\">Tragen Sie sich ein</DIV>\n"
			+ "      <DIV CLASS=\"Text\"><TEXTAREA CLASS=\"entryfield\" NAME=\"Comment\"></TEXTAREA></DIV>\n"
			+ "      <DIV CLASS=\"Text\"><INPUT CLASS=\"button\" TYPE=\"SUBMIT\" VALUE=\"Add\" /></DIV>\n"
			+ "    </FORM>";

	private static final String fileName = "Comments.dat";

	private static Logger logger = Logger.getLogger(GuestBook.class.getName());

	private static String filePath;
	private static Vector<Comment> comments;

	private static class Comment implements Serializable {

		private static final long serialVersionUID = -5358417895633204136L;

		public Date date;
		public String text;

		public Comment(Date date, String text) {
			this.date = date;
			this.text = text;
		} // Comment

	} // Comment

	@SuppressWarnings("unchecked")
	public void init() {
		BasicConfigurator.configure();
		logger.debug("init");
		filePath = getServletContext().getRealPath("/");
		logger.debug(filePath);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(filePath + fileName));
			comments = (Vector<Comment>) in.readObject();
		} catch (Exception e) {
			comments = new Vector<Comment>();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
				}
		} // try
	} // init

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try {
			logger.debug("doPost");
			String text = req.getParameter("Comment");
			if (text != null) {
				text = StringEscapeUtils.escapeHtml(text);
				Comment comment = new Comment(new Date(), text);
				comments.add(0, comment);
				ObjectOutputStream out = null;
				try {
					out = new ObjectOutputStream(new FileOutputStream(filePath + fileName));
					out.writeObject(comments);
				} finally {
					if (out != null)
						try {
							out.close();
						} catch (Exception e) {
						}
				} // try
			} // if
			doGet(req, res);
		} catch (Exception e) {
			logger.error("", e);
		} // try
	} // doPost

	private void printComments(PrintWriter out) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		out.println("");
		out.println("<!-- Comments begin -->");
		out.println("<DIV CLASS=\"Text\">");
		out.println("");
		for (int i = 0; i < comments.size(); i++) {
			Comment curComment = (Comment) comments.elementAt(i);
			String curDate = format.format(curComment.date);
			out.println("  <DIV CLASS=\"Text\">Date: " + curDate + "</DIV>");
			out.println("  <DIV CLASS=\"Text\">" + curComment.text + "</DIV>");
			out.println("  <BR/>");
			out.println("");
		} // for
		out.println("</DIV>");
		out.println("<!-- Comments end -->");
		out.println("");
	} // printComments

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try {
			logger.debug("doGet");
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.println(htmlHeader);
			printComments(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("", e);
		} // try
	} // doGet

} // GuestBook

/* ----- End of File ----- */