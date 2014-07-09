package kroki.app.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import kroki.app.utils.ImageResource;

public class KrokiMockupToolAboutDialog extends JDialog {
	
	private JLabel picLabel;
	
	public KrokiMockupToolAboutDialog() {
		init();
	}

	public void init() {
		setSize(420, 335);
		setLocationRelativeTo(null);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setTitle("About");
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
        setIconImage(headerIcon);
		
		Image about = ImageResource.getImageResource("about.text");
		picLabel = new JLabel(new ImageIcon(about));
		
		add(picLabel, BorderLayout.CENTER);
	}
	
}
