package engine;

import java.net.*;

public class SimpleServlet {

	private static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		System.out.println("Servlet starting..");
		ServerSocket welcomeSocket = new ServerSocket(PORT);
		System.out.println("Servlet started!");
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			
			HttpRequest request = new HttpRequest(connectionSocket);
			
			Thread thread = new Thread(request);
			thread.start();
		}
	}
}
