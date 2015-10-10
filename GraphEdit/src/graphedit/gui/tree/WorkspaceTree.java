package graphedit.gui.tree;

import graphedit.model.GraphEditWorkspace;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

@SuppressWarnings("serial")
public class WorkspaceTree extends JTree implements Observer, TreeSelectionListener {
	
	WorkspaceModel model;
	
	GraphEditWorkspace workspace;
	
	public WorkspaceTree(GraphEditWorkspace root) {
		super();
		workspace = root;
		model = new WorkspaceModel(workspace);
		setModel(model);
		addTreeSelectionListener(this);
		workspace.addObserver(this);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object node = e.getPath().getLastPathComponent();
		
		if (!(node instanceof GraphElement) && !(node instanceof Link) && node instanceof Observable) {
			((Observable)node).addObserver(this);
		}
		
		if (e.getPath().getPathCount() > 1) {
			Object parent = e.getPath().getParentPath().getLastPathComponent();
			((Observable)parent).addObserver(this);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SwingUtilities.updateComponentTreeUI(WorkspaceTree.this);
			}
		});
	}
	
	public void setWorkspace(GraphEditWorkspace root) {
		model.setRoot(root);
	}
	
	public GraphEditWorkspace getWorkspace() {
		return (GraphEditWorkspace)model.getRoot();
	}
}
