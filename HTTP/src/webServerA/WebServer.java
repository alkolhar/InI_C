/* ------------------
     WebServer.java
   ------------------ */

/*  Computerkommunikation & Verteilte Systeme 2012/2013, Rene Pawlitzek, NTB  */

/*   
    Unvollständiger WebServer mit mehreren Threads. Jede Anfrage von einem
    Client wird in einem eigenen Thread verarbeitet. Die Verarbeitung ist
    noch nicht implementiert. Es werden lediglich die Anfrage Parameter 
    ausgegeben.
    
    Gebrauch:  http://localhost:6789/index.html
 
 */

package webServerA;

import java.net.*;

public class WebServer {
	private final static int port = 6790;

	public static void main(String argv[]) throws Exception {

		System.out.println("WebServer starting ...");

		// Erzeuge den Server Socket, der das Anklopfen der Clients hört
		ServerSocket socket = new ServerSocket(port);

		System.out.println("WebServer is ready to receive requests. Listening on port: " + port);

		// Verarbeite die HTTP Anfragen in einer Endlosschleife
		while (true) {

			// Erzeuge einen NEUEN Socket, sobald ein Client anklopft
			Socket newClientSocket = socket.accept();

			// Erzeuge ein neues HttpRequest Objekt, um die HTTP Anfrage zu repräsentieren /
			// modellieren
			HttpRequest request = new HttpRequest(newClientSocket);

			// Erzeuge ein neuen Thread, um die HTTP Anfrage zu verarbeiten
			Thread thread = new Thread(request);

			// Starte den Thread, um die Verarbeitung der HTTP Anfrage einzuleiten
			thread.start();

		} // while

	} // main

} // WebServer

/* ----- End of File ----- */
