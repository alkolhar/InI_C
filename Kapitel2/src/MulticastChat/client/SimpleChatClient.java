package MulticastChat.client;

import MulticastChat.message.*;

public class SimpleChatClient {

    private Thread receiver;
    private Communication  communication;    

    public SimpleChatClient() throws Exception {
        communication = new Communication();
        communication.open();
        
        GUI gui = new GUI(communication);

        receiver = new Receiver(communication, gui);
        receiver.start();
    } 


    public static void main (String argv[]) {
        try {
            new SimpleChatClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

} 