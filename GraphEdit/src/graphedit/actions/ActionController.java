package graphedit.actions;

import java.util.Observable;
import java.util.Observer;

import graphedit.app.MainFrame;
import graphedit.command.CommandManager;
import graphedit.model.diagram.GraphEditModel;
import graphedit.view.SelectionModel;

public class ActionController implements Observer {

	private SelectionModel selectionModel;
	
	private GraphEditModel model;
	
	private CommandManager manager;
	
	public ActionController() { }
	
	public ActionController(SelectionModel selectionModel) {
		setSelectionModel(selectionModel);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		mainFrame = MainFrame.getInstance();
		
		if (o instanceof SelectionModel) {
			SelectionModel model = (SelectionModel)o;
			
			if (model.getSelectedElements().size() == 0){
				MainFrame.getInstance().getCopyDiagramAction().setEnabled(false);
				MainFrame.getInstance().getCutDiagramAction().setEnabled(false);
				MainFrame.getInstance().getPrepareShortcutAction().setEnabled(false);
			}
			else{
				if (!model.hasShortcut()){
					MainFrame.getInstance().getCopyDiagramAction().setEnabled(true);
					MainFrame.getInstance().getCutDiagramAction().setEnabled(true);
				}
				else{
					MainFrame.getInstance().getCopyDiagramAction().setEnabled(false);
					MainFrame.getInstance().getCutDiagramAction().setEnabled(false);
				}
				if (!model.hasShortcut() && !model.hasPackage())
					MainFrame.getInstance().getPrepareShortcutAction().setEnabled(true);
				else
					MainFrame.getInstance().getPrepareShortcutAction().setEnabled(false);
			}
		
		} else if (o instanceof GraphEditModel) {
			
			if (model.getDiagramElements().size() > 0) {
				mainFrame.getSelectAllAction().setEnabled(true);
				mainFrame.getSelectInverseAction().setEnabled(true);				
			} else {
				mainFrame.getSelectAllAction().setEnabled(false);
				mainFrame.getSelectInverseAction().setEnabled(false);
			}
		} else if (o instanceof CommandManager) {
			mainFrame.getUndoAction().setEnabled(manager.isUndoable());
			mainFrame.getRedoAction().setEnabled(manager.isRedoable());
		}
	}
	
	public void setSelectionModel(SelectionModel selectionModel) {
		this.selectionModel = selectionModel;
		this.selectionModel.addObserver(this);
	}

	public void setModel(GraphEditModel model) {
		this.model = model;
		this.model.addObserver(this);
		this.manager = model.getCommandManager();
		this.manager.addObserver(this);
	}
	
	private MainFrame mainFrame;

}
