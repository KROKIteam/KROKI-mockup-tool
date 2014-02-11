package kroki.app.utils.uml;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
	 * Dialog that is used for showing the current progress
	 * of the background worker thread.
	 */
	private ProgressWorkerFrame frame;

	/**
	 * Creates a background worker thread and a
	 * dialog for showing progress of the
	 * background worker thread.
	 * Shows the dialog to the user.
	 * Dialog should start this background worker
	 * thread.
	 * @param title  title of the dialog that shows
	 * the progress of the background worker thread
	 */
	public ProgressWorker(String title){
		frame=new ProgressWorkerFrame(this, title);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
	
	/**
	 * Shows a message of the current progress on the dialog.
	 * @param text  message of the current progress
	 */
	public void publishText(String text){
		frame.appendText(text);
	}
	
	/**
	 * Shows an error message in the dialog.
	 * @param text  error message
	 */
	public void publishErrorText(String text){
		frame.appendErrorText(text);
	}
	
	/**
	 * When the background worker thread finishes
	 * the {@link ProgressWorkerFrame#cancelToCloseButton()}
	 * method is called that turns the button for canceling
	 * the background worker thread to a button for closing
	 * the dialog that shows the current progress.
	 */
	@Override	
	public void done(){
		frame.cancelToCloseButton();
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
		frame.addIndentation();
	}

	/**
	 * Removes a specified amount of indentation from the
	 * added indentations with the {@link #addIndentation()}
	 * method.
	 * @param indentations  amount of indentations to remove
	 */
	public void removeIndentation(int indentations){
		frame.removeIndentation(indentations);
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
	 * Gets the dialog that is used to show the current progress
	 * of this background worker thread. 
	 * @return dialog  showing the current progress
	 */
	public ProgressWorkerFrame getFrame(){
		return frame;
	}
}
