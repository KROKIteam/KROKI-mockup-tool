/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.controller;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class AbstractTreeController extends AbstractController implements TreeModelListener, TreeSelectionListener {

    public void treeNodesChanged(TreeModelEvent e) {
    }

    public void treeNodesInserted(TreeModelEvent e) {
    }

    public void treeNodesRemoved(TreeModelEvent e) {
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    public void valueChanged(TreeSelectionEvent e) {
    }
}
