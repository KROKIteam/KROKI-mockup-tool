package util.staticnames;

import java.util.ResourceBundle;

public class Buttons {
	
	public static final String SEARCH = read("search");
	public static final String ZOOM_PICK = read("zoom_pick");
	public static final String VIEW_MODE = read("view_mode");
	public static final String REFRESH = read("refresh");
	public static final String HELP = read("help");
	
	// Navigation
	
	public static final String FIRST = read("first");
	public static final String NEXT = read("next");
	public static final String PREVIOUS = read("previous");
	public static final String LAST = read("last");
	
	// Actions
	
	public static final String ADD = read("add");
	public static final String UPDATE = read("update");
	public static final String COPY = read("copy");
	public static final String REMOVE = read("remove");
	
	public static final String NEXT_MENU = read("next_menu");
	
	
	public static String read(String name) {
		return ResourceBundle.getBundle("props.toolbar").getString(name);
	}
}
