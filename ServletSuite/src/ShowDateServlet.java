import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowDateServlet extends HttpServlet {

	private static final long serialVersionUID = 572523312628900266L;

	// How to use: http://localhost:8080/ServletSuite/ShowDate

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain"); // this servlet produces plain text output
		PrintWriter out = response.getWriter();
		Date curDate = new Date();
		out.println(curDate.toString());
	} // doGet

} // ShowDateServlet

/* ----- End of File ----- */
