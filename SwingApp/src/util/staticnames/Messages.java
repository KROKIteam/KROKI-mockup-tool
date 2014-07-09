package util.staticnames;

import java.util.ResourceBundle;

/***
 * 
 * Informational messages container class 
 *
 */
public class Messages {
	
	// exceptions' messages
	public static final String OPERATION_NOT_FOUND = read("exception.operationnotfound");
	
	// Error and information dialog messages
	public static final String LOOKUP_NOT_FOUND = read("lookup_not_found");
	public static final String INCORRECT_DATA_INPUT = read("incorrect_data_input");
	public static final String ERROR = read("error");
	public static final String SEARCH_TITLE = read("search_title");
	public static final Object DELETE_QUESTION = read("delete_question");
	public static final String DELETE_TITLE = read("delete_title");
	public static final String LOOK_AND_FEEL = read("look_and_feel");
	public static final String NEXT_ERROR = read("next_error");
	public static final String BAD_LOGIN = read("bad_login");
	public static final Object SEARCH_NO_FIELDS_FILLED = read("search_no_fields_filled");
	
	public static String read(String name) {
		return ResourceBundle.getBundle("props.messagesform").getString(name);
	}
}
