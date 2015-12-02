package kroki.app.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import kroki.app.utils.ImageResource;

public class KrokiMockupToolHelpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JEditorPane text;

	public KrokiMockupToolHelpDialog() {
		init();
	}

	public void init() {
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setTitle("KROKI help");
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);

		text = new JEditorPane();
		text.setContentType("text/html");
		text.setText("<br><br><font face=\"arial\" size=\"3\"> To get help on using textual commands, type \"help\" in command window." +
					"<br>For detailed help please visit KROKI <a href=\"https://github.com/KROKIteam/KROKI-mockup-tool/wiki\">Wiki pages</a>.</font>");

		StyledDocument doc = (StyledDocument) text.getDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		text.addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if(Desktop.isDesktopSupported()) {
						try {
							KrokiMockupToolHelpDialog.this.dispose();
							KrokiMockupToolHelpDialog.this.setVisible(false);
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});


		text.setEditable(false);

		add(text, BorderLayout.CENTER);
	}

}
