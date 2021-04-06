package engine;

import java.io.IOException;
import java.net.*;

public class HttpRequest implements Runnable {

	// this class provides objects for processing http requests

	private Socket socket;

	public HttpRequest(Socket socket) {
		this.socket = socket;
	} // HttpRequest

	public void run() {

		String servletName = null;
		HttpServlet servlet = null;
		HttpServletRequest request = null;
		HttpServletResponse response = null;

		try {
			System.out.println();
			System.out.println("Processing Http request...");
			// create a HttpServletRequest to represent the request (input)
			request = new HttpServletRequest(socket);
			// create a HttpServletResponse to represent the response (output)
			response = new HttpServletResponse(socket);
			try {
				// retrieve the servlet name from the request
				servletName = request.getServletName();
				// try to load the servlet class
				Class<?> c = Class.forName(servletName);
				System.out.println("Servlet class '" + servletName + "' loaded.");
				// create a new servlet object
				servlet = (HttpServlet) c.newInstance();
				System.out.println("Servlet instantiated.");
				// initialize the servlet object
				servlet.init();
				System.out.println("Servlet initialized.");
				// call the doGet method of the servlet to produce the html response
				servlet.doGet(request, response);
				System.out.println("Servlet doGet called.");
			} catch (Exception e) {
				// unable to retrieve the servlet name
				System.out.println("Servlet class '" + servletName + "' not found.");
				// send an html error message
				response.sendError();
				System.out.println("Error message sent.");
			} // try

		} catch (Exception e) {
			// swallow the exception
		} finally {
			if (response != null)
				response.close();
			if (request != null)
				request.close();
			if (socket != null)
				try {
					socket.close();
				} catch (Exception e) {
				}
			;
			System.out.println("Http request processed.");
			System.out.flush();
		} // try

	} // run

} // HttpRequest
