package kroki.app.utils.uml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.RootPaneContainer;

import kroki.app.KrokiMockupToolApp;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Dialog used to show the current progress of the background worker thread used 
 * for the import and export functionality. 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class ProgressWorkerFrame extends JDialog implements ActionListener,WindowListener{

	/**
	 * Background worker thread that implements import and export
	 * functionality.
	 */
	private ProgressWorker thread;
	/**
	 * Component that shows all the progress messages from the
	 * background worker thread.
	 */
	private JTextPane text;
	/**
	 * Used for adding new text to be shown for the current progress.
	 */
	private StyledDocument document;
	/**
	 * Used to change the color of the text being displayed as
	 * the current progress.
	 */
	private SimpleAttributeSet info;
	/**
	 * Used to change the color of the text being displayed as
	 * an error.
	 */
	private SimpleAttributeSet error;
	/**
	 * Button that is used to Cancel the background worker thread or to close
	 * the dialog if the background worker thread has finished or has been canceled.
	 */
	private JButton cancelCloseBtn;
	/**
	 * Command set to the button when it is used for canceling the background
	 * worker thread.
	 */
	private String cancelCommand="Cancel";
	/**
	 * Command set to the button when it is used for closing this dialog.
	 */
	private String closeCommand="Close";
	
	/**
	 * Creates a dialog with a button for canceling the background worker thread or 
	 * closing this dialog, depending on the current state of the background worker thread.
	 * This dialog also contains a text area for showing all the messages from the background
	 * worker thread.
	 * @param thread  background worker thread for which to show progress messages
	 * @param title   title of the dialog
	 */
	public ProgressWorkerFrame(ProgressWorker thread,String title){
		setModal(true);
		this.thread=thread;
		setTitle(title);
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Current progress:"),c);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.weightx=1;
		c.weighty=1;
		text=new JTextPane();
		
		((DefaultCaret)text.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		text.setFont(new Font("Monospaced",Font.PLAIN,15));
		
		JPanel noWrapPanel = new JPanel( new BorderLayout() );
		noWrapPanel.add( text );

		add(new JScrollPane(noWrapPanel),c);
		text.setEditable(false);
		
		document=text.getStyledDocument();
		info=new SimpleAttributeSet();
		text.setCharacterAttributes(info, true);
		StyleConstants.setForeground(info, Color.blue);
		error=new SimpleAttributeSet();
		text.setCharacterAttributes(error, true);
		StyleConstants.setForeground(error, Color.red);
		
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridy=2;
		c.weightx=0;
		c.weighty=0;
		cancelCloseBtn=new JButton("Cancel");
		cancelCloseBtn.setActionCommand(cancelCommand);
		cancelCloseBtn.addActionListener(this);
		add(cancelCloseBtn,c);
		Dimension size=KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getSize();
		setMinimumSize(new Dimension(400, 400));
		setSize(size.width*2/3, size.height*2/3);
		setLocationRelativeTo(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
		indentation="";
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
		addWindowListener(this);
	}
	
	/**
	 * Saves the current indentation used when showing messages from the
	 * background worker thread. 
	 */
	private String indentation;
	/**
	 * Adds more indentation to the currently set indentation. Added 
	 * indentation is a double empty space character. 
	 */
	public void addIndentation(){
		indentation+="  ";
	}
	
	/**
	 * Removes the indentation from the currently set indentation.
	 * Removes the specified amount of double empty space characters.
	 * @param indentations  amount of indentations to remove
	 */
	public void removeIndentation(int indentations){
		indentation=indentation.substring(0, indentation.length()-(indentations*2));
	}
	
	/**
	 * Adds a new message to the text area showing all the messages from the
	 * background worker thread.
	 * @param textToAppend  message to add to the text area
	 */
	public void appendText(String textToAppend){
		//text.append(indentation+ textToAppend+"\n");
		try {
			document.insertString(document.getLength(), indentation+ textToAppend+"\n", info);
		} catch (BadLocationException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new error message to the text area showing all the messages from the
	 * background worker thread.
	 * @param textToAppend  error message to add to the text area
	 */
	public void appendErrorText(String textToAppend){
		//text.append(indentation+ textToAppend+"\n");
		try {
			document.insertString(document.getLength(), indentation+ textToAppend+"\n", error);
		} catch (BadLocationException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Change the button from being used to cancel
	 * the background worker thread to being used to
	 * close this dialog.
	 */
	public void cancelToCloseButton(){
		cancelCloseBtn.setText("Close");
		cancelCloseBtn.setActionCommand(closeCommand);
	}
	
	/**
	 * Checks if the button is supposed to cancel
	 * the background worker thread or if it should 
	 * close this dialog, and does the corresponding 
	 * action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(cancelCommand.equals(e.getActionCommand()))
		{
			thread.cancel(true);
			cancelToCloseButton();
		}else if(closeCommand.equals(e.getActionCommand()))
		{
			dispose();
		}
	}
	@Override
	public void windowActivated(WindowEvent e) {
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(false);
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		thread.cancel(true);
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(false);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		
	}
	/**
	 * When the dialog is shown to the user the
	 * background worker thread is started.
	 */
	@Override
	public void windowOpened(WindowEvent e) {
		thread.execute();
		
	}
	
	
}
