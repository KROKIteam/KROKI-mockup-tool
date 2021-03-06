package kroki.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import kroki.app.utils.ImageResource;

/**
 * Splash screen
 * @author Kroki Team
 */
public class KrokiMockupToolSplashScreen extends JWindow {

	private static final long serialVersionUID = 7466160430401561548L;
	ImageIcon splashImg = null;
	
	public KrokiMockupToolSplashScreen() {
		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.white);
		setSize(new Dimension(600,200));
		setLocationRelativeTo(null);

		Image ico = ImageResource.getImageResource("splash.icon");
		JLabel label = new JLabel(new ImageIcon(ico));

		content.add(label, BorderLayout.CENTER);
		showSplash();
	}
	
	public void showSplash() {
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void turnOffSplash(){
		setAlwaysOnTop(false);
		setVisible(false);
		dispose();
	}

}
