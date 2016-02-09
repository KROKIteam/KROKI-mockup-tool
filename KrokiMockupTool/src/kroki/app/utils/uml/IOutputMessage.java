package kroki.app.utils.uml;

/**
 * Defining methods used to publish output messages
 * on the progress of current activity or
 * process. Three types of messages covered
 * Information, Error and Warning message.
 * @author Zeljko Ivkovic {ivkovicszeljko@gmail.com}
 *
 */
public interface IOutputMessage {
	/**
	 * Information message to be shown to user.
	 * Message is the current progress of activity.
	 * @param text information message
	 */
	public void publishInfoMessage(String text);
	/**
	 * Error message to be shown to user.
	 * Error message containing the explanation of the
	 * error that happened during execution
	 * of the activity. 
	 * @param text error message
	 */
	public void publishErrorMessage(String text);
	/**
	 * Warning message to be shown to user.
	 * @param text warning message
	 */
	public void publishWarningMessage(String text);
}
