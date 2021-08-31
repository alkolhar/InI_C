package engine;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class HttpServletRequest {

	// this class handles the http request (input)

	private String servletName;
	private BufferedReader br;
	private InputStreamReader is;

	public HttpServletRequest(Socket socket) throws IOException {

		// setup an input stream reader to read the input
		is = new InputStreamReader(socket.getInputStream());
		br = new BufferedReader(is);

		// read the request line
		String requestLine = br.readLine();
		System.out.println("Request: " + requestLine);

		// bogus request line
		if (requestLine == null)
			throw new IOException();

		// read all header lines
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		} // while

		// find the method and servlet name
		StringTokenizer tokens = new StringTokenizer(requestLine);
		String method = tokens.nextToken(); // GET or POST
		servletName = tokens.nextToken();
		servletName = servletName.substring(1); // remove the leading '/'

		System.out.println("Method: " + method);
		System.out.println("Servlet name: " + servletName);

	} // HttpServletRequest

	public String getServletName() {
		// construct the complete class name from the servlet name
		return "servlets." + servletName;
	} // getServletName

	public void close() {
		if (br != null)
			try {
				br.close();
			} catch (Exception e) {
			}
		;
		if (is != null)
			try {
				is.close();
			} catch (Exception e) {
			}
		;
	} // close

}
