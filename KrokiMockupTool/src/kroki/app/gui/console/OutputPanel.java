package kroki.app.gui.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class OutputPanel extends JPanel {

	public static int KROKI_RESPONSE = 0;
	public static int KROKI_USER_ECHO = 1;
	public static int KROKI_WARNING = 2;
	public static int KROKI_ERROR = 3;
	public static int KROKI_FINISHED = 4;
	
	private JScrollPane scrollPane;
	private JTextPane outputPane;
	
	public OutputPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		initGUI();
	}
	
	private void initGUI() {
		outputPane = new JTextPane();
		outputPane.setFont(new Font("Monospaced",Font.PLAIN,12));
		outputPane.setEditable(false);
		
		scrollPane = new JScrollPane(outputPane);
		
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Display text in the text pane
	 * @param text text that needs to be displayed
	 * @param type indicates message type 
	 */
	public void displayText(String text, int type) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss");
		String d = formatter.format(now);
		//previousLines.append(text);
		StyledDocument document = outputPane.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		outputPane.setCharacterAttributes(set, true);
		String prefix = "[KROKI] ";
		switch (type) {
			case 0:
				StyleConstants.setForeground(set, Color.blue);
				prefix = "[" + d + "] ";
				break;
			case 1:
				StyleConstants.setForeground(set, Color.black);
				prefix = ">> ";
				break;
			case 2:
				StyleConstants.setForeground(set, Color.blue);
				prefix = "[" + d + "] " + "WARNING: ";
				break;
			case 3:
				StyleConstants.setForeground(set, Color.red);
				prefix =  "[" + d + "] " + "ERROR: ";
				break;
			case 4:
				StyleConstants.setForeground(set, new Color(44,99,49));
				prefix = "[" + d + "] ";
				break;
		}
		try {
			document.insertString(document.getLength(), prefix + text + "\n", set);
			outputPane.setCaretPosition(outputPane.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
