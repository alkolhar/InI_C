package engine;

import java.io.*;
import java.net.Socket;

public class HttpServletResponse {
	private static final String CRLF = "\r\n";

	private PrintWriter printWriter;
	private StringWriter output;
	private DataOutputStream os;

	public HttpServletResponse(Socket socket) throws IOException {

		// Ausgabe
		os = new DataOutputStream(socket.getOutputStream());

		// Ausgabe des Servlets in printwriter schreiben
		output = new StringWriter();
		printWriter = new PrintWriter(output);
		
		// HTTP Antwort senden
		String statusLine = "HTTP/1.0 200 OK";
		os.writeBytes(statusLine);

	}

	public PrintWriter getWriter() {
		return printWriter;
	}
	
	public void setContentType(String contentType) throws IOException {
		String contentTypeLine = "Content-type: " + contentType + CRLF;
		os.writeBytes(contentTypeLine);
		os.writeBytes(CRLF);
	}

	public void sendError() throws IOException {
		String contentTypeLine = "Content-type: text/html" + CRLF;
		os.writeBytes(contentTypeLine);
		os.writeBytes(CRLF);
		printWriter.print("<HTML><HEAD><TITLE>Servlet Not Found</TITLE></HEAD><BODY>Servlet Not Found</BODY></HTML>");
	}

	public void close() {
		if (os != null) {
			try {
				System.out.println("Output: " + output.toString());
				// add the collected servlet output to the response
				os.writeBytes(output.toString());
				os.flush();
				os.close();
			} catch (Exception e) {
			}
		}
	}

}
