/**
*
* © Copyright International Business Machines Corporation 2007, 2009.
* All rights reserved.
*
* The 'GuestBook' hamlet implements a simple guest book.
*
* File : GuestBook.java
* Created : 2007/08/27
*
* @author Rene Pawlitzek (rpa@zurich.ibm.com)
* @version 1.00, 2009/02/13
* @since JDK 1.5
*
* History : 2007/08/27, rpa, new file
* 2009/02/13, rpa, code review
*
*/
package ch.ost.webui;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.ibm.hamlet.*;
import org.apache.commons.lang.*;
import org.apache.log4j.*;
import org.xml.sax.*;

public class GuestBook extends Hamlet {
	private static final long serialVersionUID = 5264892082158101750L;

	private static final String fileName = "Comments.dat";

	private static Logger logger = Logger.getLogger(GuestBook.class.getName());

	private static String filePath;
	private static Vector<Comment> comments;

	private static class Comment implements Serializable {
		private static final long serialVersionUID = -1199467372679306131L;

		public Date date;
		public String text;

		public Comment(Date date, String text) {
			this.date = date;
			this.text = text;
		} // Comment
	} // Comment

	private static class GuestBookHandler extends HamletHandler {
		private int i = 0;
		private Comment curComment;
		private Vector<Comment> comments;
		private SimpleDateFormat format;

		public GuestBookHandler(Hamlet hamlet, Vector<Comment> comments) {
			super(hamlet);
			this.comments = comments;
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} // GuestBookHandler

		public int getElementRepeatCount(String id, String name, Attributes atts) {
			return comments.size();
		} // getElementRepeatCount

		public String getElementReplacement(String id, String name, Attributes atts) throws Exception {
			if (id.equals("Date")) {
				curComment = (Comment) comments.elementAt(i);
				return format.format(curComment.date);
			} else if (id.equals("Comment")) {
				i++;
				return curComment.text;
			} // if
			return "?";
		} // getElementReplacement
	} // GuestBookHandler

	@SuppressWarnings("unchecked")
	public void init() {
		BasicConfigurator.configure();
		logger.debug("init");
		filePath = getServletContext().getRealPath("/");
		logger.debug(filePath);
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath + fileName));
			comments = (Vector<Comment>) in.readObject();
		} catch (Exception e) {
			comments = new Vector<Comment>();
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
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath + fileName));
				out.writeObject(comments);
			} // if
			doGet(req, res);
		} catch (Exception e) {
			logger.error("", e);
		} // try
	} // doPost

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try {
			logger.debug("doGet");
			HamletHandler handler = new GuestBookHandler(this, comments);
			serveDoc(req, res, "GuestBookTemplate.html", handler);
		} catch (Exception e) {
			logger.error("", e);
		} // try
	} // doGet
} // Guestbook
/* ----- End of File ----- */