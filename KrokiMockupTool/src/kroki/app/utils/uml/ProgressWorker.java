package kroki.app.utils.uml;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import kroki.app.KrokiMockupToolApp;

/**
 * Background worker thread for tasks that take a longer time
 * to finish like the import and export functionality. 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public abstract class ProgressWorker extends SwingWorker<Void, String> {

	/**
	 * Saves the current indentation used when showing messages from the
	 * background worker thread. 
	 */
	private String indentation;
	
	/**
	 * Creates a background worker thread.
	 */
	public ProgressWorker(){
		indentation="";
	}
	
	/**
	 * Shows a message of the current progress in the KROKI console.
	 * @param text  message of the current progress
	 */
	public void publishText(String text){
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				indentation+text, 0);
	}
	
	/**
	 * Shows a message of the current progress in the KROKI console as a WARNING.
	 * @param text  message of the current progress
	 */
	public void publishWarning(String text){
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				indentation+text, 2);
	}
	
	/**
	 * Shows an error message in the KROKI console.
	 * @param text  error message
	 */
	public void publishErrorText(String text){
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				indentation+text, 3);
	}
	
/*Should not use publish because there is a problem of synchronizing
 * the message with the added indentations. 
	@Override
	public void process(List<String> texts){
		for(String text:texts)
		{
			System.out.println(text);
			frame.appendText(text);
		}
	}
*/	
	/**
	 * Adds indentation to the message that should be displayed.
	 */
	public void addIndentation(){
		indentation+="  ";
	}

	/**
	 * Removes a specified amount of indentation from the
	 * added indentations with the {@link #addIndentation()}
	 * method.
	 * @param indentations  amount of indentations to remove
	 */
	public void removeIndentation(int indentations){
		indentation=indentation.substring(0, indentation.length()-(indentations*2));
	}
	
	/**
	 * Shows a dialog with an error message and a title specified.
	 * @param message   error message to show
	 * @param title     title of the dialog
	 */
	public void showError(String message,String title){
		JOptionPane.showOptionDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
	}
	
	/**
	 * Gets the Frame that is used to show the current progress
	 * of this background worker thread. 
	 * @return dialog  showing the current progress
	 */
	public JFrame getFrame(){
		return KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame();
	}
}
