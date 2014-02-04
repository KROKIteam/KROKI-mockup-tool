package graphedit.app;

import graphedit.model.GraphEditWorkspace;
import graphedit.util.WorkspaceUtility;


public class Application {

	public static void main(String argv[]) {
		
		MainFrame mainFrame = MainFrame.getInstance();
		mainFrame.setTitle(mainFrame.getTitle() + " - " + GraphEditWorkspace.getInstance().getFile().getAbsolutePath());
		WorkspaceUtility.load();
	}
}
