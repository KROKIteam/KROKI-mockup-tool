/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;
import kroki.app.KrokiMockupToolApp;
import kroki.app.action.CreatePresistentDiagramAction;
import kroki.app.action.CreateUIDiagramAction;
import kroki.app.action.DBConneectionSettingsAction;
import kroki.app.action.DeleteAction;
import kroki.app.action.ExportSwingAction;
import kroki.app.action.ExportWebAction;
import kroki.app.action.NewFileAction;
import kroki.app.action.NewPackageAction;
import kroki.app.action.NewProjectAction;
import kroki.app.action.RenameAction;
import kroki.app.model.Workspace;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class TreeController extends AbstractTreeController {

    JTree tree;
    Workspace workspace;

    public TreeController(JTree tree, Workspace workspace) {
        this.tree = tree;
        this.workspace = workspace;
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        super.treeNodesChanged(e);
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
        super.treeNodesInserted(e);
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
        super.treeNodesRemoved(e);
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
        super.treeStructureChanged(e);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath treePath = e.getNewLeadSelectionPath();
        if (treePath != null) {
			Object node = treePath.getLastPathComponent();
			if (node instanceof Workspace) {
				//System.out.println("WORKSPACE");
			} else if (node instanceof BussinesSubsystem) {
				//System.out.println("BUSSINESSUBSYSTEM");
			} else if (node instanceof VisibleClass) {
				VisibleClass v = (VisibleClass) node;
				if(node instanceof StandardPanel) {
				}
			}
		}
		super.valueChanged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getClickCount() == 1) {
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
                if (treePath == null) {
                    return;
                }
                Object node = treePath.getLastPathComponent();
                if (node instanceof VisibleClass) {
                    VisibleClass visibleClass = (VisibleClass) node;
                    TabbedPaneController tpc = KrokiMockupToolApp.getInstance().getTabbedPaneController();
                    if (!tpc.containsTab(visibleClass)) {
                        tpc.openTab(visibleClass);
                    } else {
                        int index = tpc.getTabIndex(visibleClass);
                        if (index != -1) {
                            tpc.setCurrentTabIndex(index);
                        }
                    }
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {

            TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
            tree.setSelectionPath(treePath);
            JPopupMenu popupMenu = new JPopupMenu();
            JMenu diagramMenu = new JMenu();
            diagramMenu.setText("View diagram...");
            diagramMenu.add(new CreateUIDiagramAction());
            diagramMenu.add(new CreatePresistentDiagramAction());
            if (treePath == null) {
                popupMenu.add(new NewProjectAction());
                popupMenu.add(new NewFileAction());
                popupMenu.show(tree, e.getX(), e.getY());
                return;
            }
            Object node = treePath.getLastPathComponent();
            if (node instanceof Workspace) {
                popupMenu.add(new NewProjectAction());
                popupMenu.add(new NewFileAction());
            } else if (node instanceof BussinesSubsystem) {
                popupMenu.add(new NewPackageAction((BussinesSubsystem) node));
                popupMenu.add(new NewFileAction((BussinesSubsystem) node));
                popupMenu.addSeparator();
                popupMenu.add(new RenameAction((BussinesSubsystem) node));
                popupMenu.add(new DeleteAction((BussinesSubsystem) node));
                popupMenu.addSeparator();
                popupMenu.add(new DBConneectionSettingsAction());
                popupMenu.add(diagramMenu);
            } else if (node instanceof VisibleClass) {
                popupMenu.add(new RenameAction((VisibleClass) node));
                popupMenu.add(new DeleteAction((VisibleClass) node));
            }
            popupMenu.show(tree, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }

    public JTree getTree() {
        return tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
}
