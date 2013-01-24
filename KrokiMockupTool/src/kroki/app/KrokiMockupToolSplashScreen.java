package kroki.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import kroki.app.utils.ImageResource;

public class KrokiMockupToolSplashScreen extends JWindow {

	private static final long serialVersionUID = 7466160430401561548L;

	public KrokiMockupToolSplashScreen() {

	}

	public void showSplash() {

		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.white);
		setSize(new Dimension(400,350));
		setLocationRelativeTo(null);

		Image ico = ImageResource.getImageResource("splash.icon");
		JLabel label = new JLabel(new ImageIcon(ico));

		JPanel pnlUcitavanje = new JPanel();
		pnlUcitavanje.setBackground(Color.WHITE);

		content.add(label, BorderLayout.CENTER);
		content.add(pnlUcitavanje, BorderLayout.SOUTH);

		content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 7));
	}

	public void showSplashAndExit() {
		showSplash();
		setAlwaysOnTop(true);
		setVisible(true);
		setAlwaysOnTop(false);
	}

	public void turnOffSplash(){
		setAlwaysOnTop(false);
		setVisible(false);
		dispose();
	}

}
