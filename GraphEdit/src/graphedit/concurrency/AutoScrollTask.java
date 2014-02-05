package graphedit.concurrency;

import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

public class AutoScrollTask implements Runnable {

	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	
	public static final int AUTOSCROLL_THRESHOLD = 20;
	
	private static final int SLEEP_TIME = 50;
	
	private GraphEditView view;
	private GraphEditController controller;
	
	private boolean running = false;
	
	public AutoScrollTask(GraphEditView view, GraphEditController controller) {
		this.view = view;
		this.controller = controller;
	}

	public void setView(GraphEditView view) {
		this.view = view;
	}
	
	public void setController(GraphEditController controller) {
		this.controller = controller;
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			synchronized (view) {
				controller.scroll();
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
