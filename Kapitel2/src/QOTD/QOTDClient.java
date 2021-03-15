package QOTD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class QOTDClient {
	private static final int port = 17;

	public static void main(String[] args) throws Exception {
		String sentence = "";

		// Socket öffnen
		System.out.println("Contacting Serverg..");
		Socket clientSocket = new Socket("146.136.112.22", port);
		
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromServer.readLine();
		while (sentence != null) {
			System.out.println(sentence);
			sentence = inFromServer.readLine();
		}
		clientSocket.close();
	}
}

