package graphedit.app.util;

import graphedit.app.MainFrame;
import graphedit.view.GraphEditView;

// samo za testiranje state automata
public class StatusRefresher extends Thread {
	
	@Override
	public void run() {
		MainFrame mainFrame = MainFrame.getInstance();
		while (mainFrame != null) {
			synchronized (mainFrame) {
				GraphEditView currentView = mainFrame.getCurrentView();
				if (currentView instanceof GraphEditView)
					mainFrame.setStatusTrack(currentView.getCurrentState().toString());
			}
		}
	}

}
