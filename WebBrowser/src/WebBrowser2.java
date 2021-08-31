import java.io.*;
import java.net.*;

public class WebBrowser2 {
	private static final int HTTP_PORT = 80;
	private static final String HTTP_PROTOCOL = "http";

	private int port = HTTP_PORT;
	private String hostName = "localhost";
	private String page = "/index.html";

	public int getPort() {
		return port;
	} // getPort

	public void setPort(int port) {
		this.port = port;
	} // setPort

	public String getHostName() {
		return hostName;
	} // getHostName

	public void setHostName(String hostName) {
		this.hostName = hostName;
	} // setHostName

	public String getPage() {
		return page;
	} // getPage

	public void setPage(String page) {
		this.page = page;
	} // setPage

	public void displayPage() throws Exception {

		// send the request for the page
		URL url = new URL(HTTP_PROTOCOL, hostName, port, page);
		URLConnection con = url.openConnection();

		// receive and display the page
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line = in.readLine();
		while (line != null) {
			System.out.println(line);
			line = in.readLine();
		} // while

		// close reader
		in.close();

	} // displayPage

	public static void main(String[] args) {
		try {
			WebBrowser2 webBrowser = new WebBrowser2();
			webBrowser.setHostName("www.ntb.ch");
			webBrowser.setPort(HTTP_PORT);
			webBrowser.setPage("/ntb-homepage.html");
			webBrowser.displayPage();
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // main

}
