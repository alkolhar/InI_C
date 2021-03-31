import java.io.*;
import java.net.*;

public class WebBrowser3 {
	private static final int HTTP_PORT = 80;
	private static final String HTTP_PROTOCOL = "http";

	private String hostName = "localhost";
	private String page = "/index.html";

	public static void main(String[] args) {
		try {
			new WebBrowser3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebBrowser3() throws Exception {
		URL url = new URL(HTTP_PROTOCOL, hostName, HTTP_PORT, page);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = in.readLine();
		while (line != null) {
			System.out.println(line);
			line = in.readLine();
		}
		
		in.close();
		conn.disconnect();
	}
}
