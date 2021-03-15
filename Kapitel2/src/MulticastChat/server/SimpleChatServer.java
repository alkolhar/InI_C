package MulticastChat.server;

import java.net.*;
import java.util.*;
import java.util.Observable;
import MulticastChat.message.*;

@SuppressWarnings("deprecation")
public class SimpleChatServer extends Observable {
    public static final int PORT = 5555;
    private  Map<String, InetSocketAddress>  clients;
    private  Communication  communication;  

    public SimpleChatServer() {
        clients = new HashMap<String, InetSocketAddress> ();

        GUI gui = new GUI(this);
        this.addObserver(gui);

        try {
            communication = new Communication ();  
            communication.open(PORT);

            System.out.println ("SimpleChatServer starting...");
            InetAddress localhost = InetAddress.getLocalHost ();
            System.out.println ("IP Address: " + localhost + ", Port: " + PORT);

            while (true) {     
                System.out.println("Waiting for message...");
                System.out.println();
                setChanged(); notifyObservers();
                communication.waitForMessage(); // Wait to receive a datagram, blocking call

                Message message = communication.getMessage ();
                InetSocketAddress socketAddress = (InetSocketAddress) communication.getSocketAddress ();

                if (message instanceof PostingMessage) {
                    // an alle clients weiterleiten
                    for (Map.Entry<String, InetSocketAddress> entry : clients.entrySet()) { // über alle clients
                        communication.sendMessage(entry.getValue(), message);
                    }
                } else if (message instanceof RegisterMessage) {
                    message = (RegisterMessage) message;
                    if (!clients.containsKey(message.getUser())) {
                    	clients.put(message.getUser(), socketAddress);
                    } else {
                    	System.out.println("User rejected");
                    }
                } else { // UnregisterMessage
                    message = (UnregisterMessage) message;
                    if (clients.containsKey(message.getUser())) { clients.remove(message.getUser()); };
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public static void main(String[] args) {
        try { 
            new SimpleChatServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public Map<String, InetSocketAddress> getClients() {
        return clients;
    }
} 