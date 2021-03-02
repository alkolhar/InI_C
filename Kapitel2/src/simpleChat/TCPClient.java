package simpleChat;

import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String[] args) throws Exception {
		String sentence;
		String modifiedSentence;
		
		// Reader erzeugen
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		// Socket öffnen
		System.out.println("Server starting..");
		Socket clientSocket = new Socket("146.136.112.20", 4444);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		// Nachricht lesen & abschicken
		System.out.print("Nachricht: ");
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		
		// Antwort vom Server lesen & ausgeben
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
	}
}
