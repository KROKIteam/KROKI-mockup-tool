package kroki.app.utils.uml;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import kroki.app.KrokiMockupToolApp;

/**
 * Background worker thread for tasks that take a longer time
 * to finish like the import and export functionality. 
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public abstract class ProgressWorker extends SwingWorker<Void, WorkerPublishModel> {

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
		/*
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				indentation+text, 0);
		*/
		publish(new WorkerPublishModel(indentation+text, 0));
	}
	
	/**
	 * Shows a message of the current progress in the KROKI console as a WARNING.
	 * @param text  message of the current progress
	 */
	public void publishWarning(String text){
		/*
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				indentation+text, 2);
		*/
		publish(new WorkerPublishModel(indentation+text, 2));
	}
	
	/**
	 * Shows an error message in the KROKI console.
	 * @param text  error message
	 */
	public void publishErrorText(String text){
		/*
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(
				text, 3);
		*/
		publish(new WorkerPublishModel(text, 3));
	}
	
	@Override
    protected void process(List<WorkerPublishModel> chunks) {
        for (WorkerPublishModel chunk : chunks) {
        	KrokiMockupToolApp.getInstance().displayTextOutput(chunk.getText(), chunk.getTypeOfMessage());
        }
    }
	
	/**
	 * Method that removes the exception class from the Exception 
	 * message and returns only the message left.
	 * @param e Exception for which to remove the class of the
	 * Exception 
	 * @return message from the Exception with out the class of
	 * the Exception
	 */
	public String exceptionMessage(Exception e){
		String message=e.getMessage();
		if(message!=null)
		{
			int index=message.indexOf(':');
			if(index>-1&&(index+1)<message.length())
			{
				message=message.substring(index+1);
				message=message.trim();
				if(!message.isEmpty())
					return message;
			}
		}
		return "Unspecified error ocured";
	}

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
