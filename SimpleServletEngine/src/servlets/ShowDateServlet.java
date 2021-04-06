package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import engine.*;

public class ShowDateServlet extends HttpServlet {

// How to use: http://localhost:8080/ShowDateServlet

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html"); // this servlet produces html output
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>Current time</title></head>");
		out.println("<body>");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		String tmp = sdf.format(now);
		out.println("<h1>" + tmp + "</h1>");
		out.println("</body>");
		out.println("</html>");
	} // doGet

} // ShowDateServlet