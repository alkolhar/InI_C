package simpleChat;

import java.net.*;

public class UDPServer {

	public static void main(String[] args) {
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		System.out.println("Server starting...");
		
		
		try {
			DatagramSocket serverSocket = new DatagramSocket(9876);
			System.out.println("Server gestartet!");
			
			while (true) {
				// empfange UDP Paket vom Client
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				
				// Paket lesen, in String umwandeln & warten
				String sentence = new String(receivePacket.getData());
				String capitalizedSentence = sentence.toUpperCase();
				Thread.sleep(2000);
				sendData = capitalizedSentence.getBytes();
				
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
