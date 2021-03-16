package guiTest;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LookAndFeel extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JButton lfButton;
	private JTextArea area;
	private JScrollPane pane;
	private JMenuItem exit;
	private int lAF = 1;

	public LookAndFeel() {
		initComponents();

		updateLAF();

		this.setTitle("Look and Feel-Demo");
		this.setSize(240, 180);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void initComponents() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Datei");
		exit = new JMenuItem("exit");
		exit.addActionListener(this);
		menu.add(exit);
		menuBar.add(menu);
		lfButton = new JButton("L & F");
		lfButton.addActionListener(this);
		label = new JLabel();
		area = new JTextArea();
		pane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel buttPanel = new JPanel(new FlowLayout());
		buttPanel.add(lfButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

		mainPanel.add(label, BorderLayout.NORTH);
		mainPanel.add(pane, BorderLayout.CENTER);
		mainPanel.add(buttPanel, BorderLayout.SOUTH);

		this.add(menuBar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
	}

	private void updateLAF() {
		try {
			String plaf = "";
			if (lAF == 1) {
				plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
			} else if (lAF == 2) {
				plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} else if (lAF == 3) {
				plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			} else if (lAF == 4) {
				plaf = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} else if (lAF == 5) {
				plaf = UIManager.getSystemLookAndFeelClassName();
			}
			setText(plaf);
			label.setText(" L & F Nr.: " + new Integer(lAF).toString());

			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(this);

		} catch (UnsupportedLookAndFeelException ue) {
			System.err.println(ue.toString());
		} catch (ClassNotFoundException ce) {
			System.err.println(ce.toString());
		} catch (InstantiationException ie) {
			System.err.println(ie.toString());
		} catch (IllegalAccessException iae) {
			System.err.println(iae.toString());
		}
	}

	private void setText(String text) {
		area.setText(text);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == lfButton) {
			lAF++;
			if (lAF > 5)
				lAF = 1;
			updateLAF();
		}
		if (e.getSource() == exit) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new LookAndFeel();
	}
}