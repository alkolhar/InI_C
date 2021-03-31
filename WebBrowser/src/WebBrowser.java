import java.io.*;
import java.net.*;

public class WebBrowser {
	private static final int PORT = 80;
	private String hostName = "www.ost.ch";

	public static void main(String[] args) {
		try {
			new WebBrowser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebBrowser() throws Exception {
		// TCP Socket erstellen
		Socket clientSocket = new Socket(hostName, PORT);

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		
		// Anfrage schicken
		String request = "GET " + "/index.html" + " HTTP/1.1\r\nHost: " + hostName + "\r\n\r\n";
		System.out.println("Request: \r\n" + request);
		out.print(request);
		out.flush();

		// Antwort ausgeben
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String text = in.readLine();
		while (text != null) {
			System.out.println(text);
			text = in.readLine();
		}

		out.close();
		in.close();
		clientSocket.close();
	}
}
