package simpleChat;

import java.io.*;
import java.net.*;

public class UDPClient {

	public static void main(String[] args) {
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			// Socket für Kommunikation erzeugen
			DatagramSocket clientSocket = new DatagramSocket();
			
			// Benutzereingabe lesen
			System.out.print("Input: ");
			String sentence = inFromUser.readLine();
			sendData = sentence.getBytes();
			
			// UDP Paket erzeugen und absenden
			InetAddress IPAddress = InetAddress.getByName("146.136.112.65");
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			
			// UDP Paket vom Server empfangen
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			
			// Antwort vom Server ausgeben
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
