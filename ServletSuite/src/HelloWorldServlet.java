import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

	private static final long serialVersionUID = 572523312628900266L;

	// How to use: http://localhost:8080/ServletSuite/HelloWorld

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html"); // this servlet produces html output
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Hello World!</title></head>");
		out.println("<body>");
		out.println("<h1>Hello World!</h1>");
		out.println("</body>");
		out.println("</html>");
	} // doGet

} // HelloWorldServlet

