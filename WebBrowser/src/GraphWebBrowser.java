import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GraphWebBrowser extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private String urlToVisit = "http://www.schaft.com/endofit.html";

	private JLabel statusBar;
	private JTextField urlField;
	private JEditorPane editor;

	public GraphWebBrowser() {
		super("Mini Web Browser");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		cp.add(buildUrl(), BorderLayout.NORTH);
		cp.add(buildStatusBar(), BorderLayout.SOUTH);
		cp.add(buildEditor());
		cp.add(buildScroll(), BorderLayout.CENTER);
	}

	private JScrollPane buildScroll() {
		JScrollPane scroll = new JScrollPane(editor);
		scroll.setBorder(new EmptyBorder(5, 5, 5, 5));
		return scroll;
	}

	private JEditorPane buildEditor() {
		editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setEditable(false);
		try {
			editor.setPage(urlToVisit);
		} catch (Exception e) {
			statusBar.setText("Unable to display page, using blank page instead.");
		}
		return editor;
	}

	private JLabel buildStatusBar() {
		statusBar = new JLabel(" ");
		statusBar.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		return statusBar;
	}

	private JPanel buildUrl() {
		JPanel urlPanel = new JPanel();
		urlPanel.setLayout(new BorderLayout());
		urlField = new JTextField(urlToVisit);
		urlField.addActionListener(this);

		JLabel label = new JLabel("Site: ");
		urlPanel.add(label, BorderLayout.WEST);
		urlPanel.add(urlField, BorderLayout.CENTER);
		urlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		return urlPanel;
	}

	public void actionPerformed(ActionEvent event) {
		try {
			editor.setPage(event.getActionCommand());
		} catch (Exception e) {
			statusBar.setText("Error: " + e.getMessage());
		}
	}

	public static void main(String args[]) {
		GraphWebBrowser browser = new GraphWebBrowser();
		browser.setVisible(true);
	}

}
