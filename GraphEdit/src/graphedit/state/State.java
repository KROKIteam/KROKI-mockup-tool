package graphedit.state;

import graphedit.app.MainFrame;
import graphedit.model.diagram.GraphEditModel;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import kroki.commons.camelcase.NamingUtil;

public abstract class State implements MouseListener, MouseMotionListener,
		MouseWheelListener, KeyListener {
	
	//sadrzi atribut klase GraphEditModel, ali se postavljanje istog izbegava 
	//tako da onda treba izbaciti odavde ako ga vec necemo koristiti...
	
	// Context for state design pattern
	protected GraphEditController controller; 
	protected GraphEditModel model;
	protected GraphEditView view;
	protected NamingUtil namer = new NamingUtil();

	public State(GraphEditController controller) {
		this.controller = controller;
	}
	
	public State() { }

	public GraphEditController getController() {
		return controller;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	public abstract boolean isAutoScrollOnDragEnabled();
	
	public abstract boolean isAutoScrollOnMoveEnabled();

	public GraphEditModel getModel() {
		return model;
	}

	public void setModel(GraphEditModel model) {
		this.model = model;
	}

	public GraphEditView getView() {
		return view;
	}

	public void setView(GraphEditView view) {
		this.view = view;
	}

	public void setController(GraphEditController controller) {
		this.controller = controller;
	}
	
	public void setCursor(Cursor cursor) {
		view.setCursor(cursor);
	}

	@Override
	public String toString() {
		return namer.transformClassName(getClass().getSimpleName());
	}

	protected void switchToDefaultState() {
		State state = MainFrame.getInstance().getCurrentView().getModel().getSelectionState();
		state.setController(controller);
		state.setView(view);
		state.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		controller.setCurrentState(state);
		view.setSelectedTool(MainFrame.ToolSelected.SELECTION);
		
		MainFrame.getInstance().setStatusTrack(state.toString());
		MainFrame.getInstance().getSelectButton().setSelected(true);
	}
	
	protected void switchToState(State state) {
		state.setController(controller);
		state.setView(view);
		controller.setCurrentState(state);
		
		MainFrame.getInstance().setStatusTrack(state.toString());
	}

	public void clearEverything() {
	}
}
