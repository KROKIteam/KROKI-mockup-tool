/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.state;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import kroki.app.KrokiMockupToolApp;
import kroki.app.command.CommandManager;
import kroki.app.command.RemoveCommand;
import kroki.app.controller.TabbedPaneController;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.app.view.Handle;
import kroki.app.view.HandleManager;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SelectState extends State {

    private Point currentPosition;

    public SelectState(Context context) {
        super(context, "app.state.select");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentPosition = (Point) e.getPoint().clone();
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();
        VisibleElement visibleElement = visibleClass.getVisibleElementAtPoint(e.getPoint());
        SelectionModel selectionModel = c.getSelectionModel();
        Handle h = null;
        //ukoliko je samo jedan selektovan pokazi handlove.
        if (selectionModel.getSelectionNum() == 1) {
            VisibleElement el = selectionModel.getVisibleElementAt(0);
            h = HandleManager.getHandleForPoint(el, e.getPoint());
        }
        if (h == null) {
            if (visibleElement != null && e.isControlDown() && !selectionModel.isSelected(visibleElement)) {
                selectionModel.addToSelection(visibleElement);
            } else if (visibleElement != null && e.isControlDown() && selectionModel.isSelected(visibleElement)) {
                selectionModel.removeFromSelection(visibleElement);
            } else if (visibleElement != null && !e.isControlDown() && !selectionModel.isSelected(visibleElement)) {
                selectionModel.clearSelection();
                selectionModel.addToSelection(visibleElement);
            } else if (visibleElement == null) {
                selectionModel.clearSelection();
            }
        }
        c.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        SelectionModel selectionModel = c.getSelectionModel();
        //necu ni prelaziti u move ako nista nije selektovano!
        if (selectionModel.getSelectionNum() == 0) {
            return;
        }
        Handle h = null;
        VisibleElement el = null;
        //ukoliko je samo jedan selektovan pokazi handlove.
        if (selectionModel.getSelectionNum() == 1) {
            el = selectionModel.getVisibleElementAt(0);
            //h = HandleManager.getHandleForPoint(el, e.getPoint());
            h = HandleManager.getHandleForPoint(el, currentPosition);
        }
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
            if (h != null) {
                selectionModel.clearSelection();
                selectionModel.addToSelection(el);
                ResizeState resizeState = (ResizeState) context.getState(State.RESIZE_STATE);
                resizeState.setHandle(h);
                resizeState.setVisibleElement(el);
                context.goNext(State.RESIZE_STATE);
            } else {
                MoveState moveState = (MoveState) context.getState(State.MOVE_STATE);
                moveState.setPreviousPosition(currentPosition);
                context.goNext(State.MOVE_STATE);
            }
        }
        c.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas c = tabbedPaneController.getCurrentTabContent();
        SelectionModel selectionModel = c.getSelectionModel();
        if (c != null && selectionModel.getSelectionNum() == 1) {
            c.changeCursorImageAt(e.getPoint());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
            Canvas c = tabbedPaneController.getCurrentTabContent();
            CommandManager commandManager = c.getCommandManager();
            SelectionModel selectionModel = c.getSelectionModel();
            //pravim listu elemenata za izbaciti iz selekcije
            List<VisibleElement> deleted = new ArrayList<VisibleElement>();
            for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
                if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
                    deleted.add(visibleElement);
                }
            }
            //ukoliko se u listi nalazi vise od 0 elemenata onda kreiram objekat klase RemoveCommand
            if (deleted.size() > 0) {
                RemoveCommand removeCommand = new RemoveCommand(deleted);
                commandManager.addCommand(removeCommand);
                selectionModel.removeFromSelection(deleted);
            }
            c.repaint();
        }
    }
}
