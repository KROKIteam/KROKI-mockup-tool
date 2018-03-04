package kroki.app.model;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Tree model used to show workspace content within
 * a JTree
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ProjectHierarchyModel implements TreeModel {

    private Workspace workspace;
    private JTree tree;

    public ProjectHierarchyModel(JTree tree, Workspace workspace) {
        this.tree = tree;
        this.workspace = workspace;

    }

    public Object getRoot() {
        return workspace;
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof Workspace) {
            return workspace.getPackageAt(index);
        } else if (parent instanceof BussinesSubsystem) {
            return ((BussinesSubsystem) parent).getOwnedElementAt(index);
        } else {
            return null;
        }
    }

    public int getChildCount(Object parent) {
        if (parent instanceof Workspace) {
            return workspace.getPackageCount();
        } else if (parent instanceof BussinesSubsystem) {
            return ((BussinesSubsystem) parent).ownedElementCount();
        } else {
            return 0;
        }
    }

    public boolean isLeaf(Object node) {
        return node instanceof VisibleClass;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("value for path changed!");
        Object obj = path.getLastPathComponent();
        if (obj instanceof Workspace) {
            //Nothing happens
        } else {
            VisibleElement visibleElement = (VisibleElement) obj;
            visibleElement.setLabel(newValue.toString());
            //TODO: ukoliko je BussinesSubsystem puca...
//            visibleClass.getComponent().setName(newValue.toString());
//            visibleClass.update();

            tree.updateUI();
            if (visibleElement instanceof VisibleClass) {
                int index = KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabIndex((VisibleClass) visibleElement);
                KrokiMockupToolApp.getInstance().getTabbedPaneController().setTitleAt(index, newValue.toString());
                KrokiMockupToolApp.getInstance().getTabbedPaneController().updateTabbedPane();
            }
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof Workspace) {
            return workspace.getIndexOf((BussinesSubsystem) child);
        } else if (parent instanceof BussinesSubsystem) {
            return ((BussinesSubsystem) parent).indexOf((VisibleElement) child);
        } else {
            return 0;
        }
    }

    public void addTreeModelListener(TreeModelListener l) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeTreeModelListener(TreeModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
