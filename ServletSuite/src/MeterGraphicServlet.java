import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MeterGraphicServlet extends HttpServlet {

	private static final long serialVersionUID = 5747079797391838775L;

	// defaults
	private static final int defWidth = 75;
	private static final int defHeight = 15;
	private static final int defPosition = 50;
	private static final int defLabel = 1;
	private static final int numTicks = 5;
	private static final int tickLength = 5;

	private static final Color green = new Color(55, 200, 55);
	private static final Color yellow = new Color(255, 255, 0);
	private static final Color orange = new Color(255, 128, 0);
	private static final Color red = new Color(255, 0, 0);

	private Color getFgColor(int aPosition) {
		if (aPosition <= 25)
			return Color.black;
		if (aPosition > 25 && aPosition <= 50)
			return Color.black;
		if (aPosition > 50 && aPosition <= 75)
			return Color.black;
		if (aPosition > 75)
			return Color.white;
		return Color.white;
	} // getFgColor

	private Color getBgColor(int aPosition) {
		if (aPosition <= 25)
			return green;
		if (aPosition > 25 && aPosition <= 50)
			return yellow;
		if (aPosition > 50 && aPosition <= 75)
			return orange;
		if (aPosition > 75)
			return red;
		return Color.white;
	} // getBgColor

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		Graphics g = null;
		try {
			res.setContentType("image/png");
			// get parameters
			int width = defWidth;
			int height = defHeight;
			int position = defPosition;
			int label = defLabel;
			// adjust position
			position = (position / 25) * 25;
			// prepare drawing
			// can't use BufferedImage.TYPE_INT_RGB because encoder is limited
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
			g = (Graphics) image.getGraphics();
			// draw chart
			// int h = height / 2;
			// int xOff = h / 2;
			int h = height - 1;
			int xOff = 0;
			int yOff = 0;
			int w = (width - 2 * xOff - 1);
			// clear background
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			// draw bar
			g.setColor(Color.black);
			g.drawRect(xOff, yOff, w, h);
			Color color = getBgColor(position);
			g.setColor(color);
			position = Math.max(0, Math.min(position, 100));
			int wbar = (position * (w - 1)) / 100;
			g.fillRect(xOff + 1, yOff + 1, wbar, h - 1);
			// draw ticks and labels
			/*
			 * g.setColor (Color.black); FontMetrics fm = g.getFontMetrics (); for (int i =
			 * 0; i < numTicks; i++) { int xPos = xOff + 1 + (i * (w - 2) / (numTicks - 1));
			 * g.drawLine (xPos, yOff + h, xPos, yOff + h + tickLength); String str = "" +
			 * (i * 25); Rectangle2D bounds = fm.getStringBounds (str, g); int lw = (int)
			 * (bounds.getWidth () + 0.5); int lh = (int) (bounds.getHeight () + 0.5);
			 * g.drawString (str, xPos - lw / 2, yOff + h + tickLength + lh); } // for
			 */
			// draw value
			if (label != 0) {
				String str = "" + position;
				color = getFgColor(position);
				g.setColor(color);
				FontMetrics fm = g.getFontMetrics();
				Rectangle2D bounds = fm.getStringBounds(str, g);
				int lw = (int) (bounds.getWidth() + 0.5);
				int lh = fm.getAscent();
				g.drawString(str, (defWidth - lw) / 2, ((defHeight - 2 - lh) / 2) + lh - 1);
			} // if

			// output chart
			ServletOutputStream out = res.getOutputStream();
			ImageIO.write(image, "png", out);

		} finally {
			if (g != null)
				g.dispose();
		} // try

	} // doGet

} // MeterGraphicsServlet

/* ----- End of File ----- */
