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

import kroki.api.profil.group.ElementsGroupUtil;
import kroki.api.profil.panel.VisibleClassUtil;
import kroki.app.KrokiMockupToolApp;
import kroki.app.command.Command;
import kroki.app.command.CommandManager;
import kroki.app.command.PasteCommand;
import kroki.app.command.RemoveCommand;
import kroki.app.controller.TabbedPaneController;
import kroki.app.model.ClipboardContents;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.app.view.Handle;
import kroki.app.view.HandleManager;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SelectState extends State {

    private Point currentPosition;
    private static boolean cutAction = false;
    
    public SelectState(Context context) {
        super(context, "app.state.select");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentPosition = (Point) e.getPoint().clone();
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();
        VisibleElement visibleElement = VisibleClassUtil.getVisibleElementAtPoint(visibleClass, e.getPoint());
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
    	TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas c = tabbedPaneController.getCurrentTabContent();
        CommandManager commandManager = c.getCommandManager();
        SelectionModel selectionModel = c.getSelectionModel();
        
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteAction(tabbedPaneController, c, commandManager, selectionModel);
        }
        
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
			//cutAction(tabbedPaneController, c, commandManager, selectionModel);
		}
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
			//pasteAction(tabbedPaneController, c, commandManager, selectionModel);
		}
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
			//copy(tabbedPaneController, c, commandManager, selectionModel);
		}
    }

    //TO BE DELETED
    @Deprecated
	private void copy(TabbedPaneController tabbedPaneController, Canvas c,
			CommandManager commandManager, SelectionModel selectionModel) {
        //pravim listu elemenata za izbaciti iz selekcije
        List<VisibleElement> cutted = new ArrayList<VisibleElement>();
        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
            	cutted.add(visibleElement);
            }
        }
        KrokiMockupToolApp.getInstance().getClipboardManager().copySelectedElements();
        cutAction = false;
        c.repaint();		
	}

    //TO BE DELETED
    @Deprecated
	/**
	 * Potrebno je izvrsiti proveru da li se moze element koji se pastuje dodati na zeljeno mesto,
	 * ukoliko nije moguce zaustavlja se operacija pre brisanja clipboarda, inace bi se ispraznio
	 * iako paste nije izvrsen na odgovarajuce mesto
	 * @param tabbedPaneController
	 * @param c
	 * @param commandManager
	 * @param selectionModel
	 */
	private void pasteAction(TabbedPaneController tabbedPaneController,
			Canvas c, CommandManager commandManager,
			SelectionModel selectionModel) {
		
		@SuppressWarnings("unchecked")
		List<VisibleElement> clipboardElements = (List<VisibleElement>) KrokiMockupToolApp.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardVisibleElementsFlavor);
		if(clipboardElements == null || clipboardElements.isEmpty())
			return;
		
		//pravim listu elemenata za izbaciti iz selekcije
        List<VisibleElement> selected = new ArrayList<VisibleElement>();
        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            if (!(visibleElement instanceof VisibleClass)) {
            	selected.add(visibleElement);
            }
        }
        
        //ukoliko se u listi nalazi tacno jedan selektovani element i da je ElementsGroup
		if (selected.size() == 1 && selected.get(0) instanceof ElementsGroup) {
			ElementsGroup temp = (ElementsGroup) selected.get(0);
			
			for (VisibleElement el : clipboardElements) {
				if (!ElementsGroupUtil.checkIfCanAdd(temp, el)) {
					return;
				}
			}
			
			Command command = new PasteCommand(c.getVisibleClass(), temp, clipboardElements, null, cutAction);
			commandManager.addCommand(command);
			selectionModel.clearSelection();			
			if(cutAction)
				KrokiMockupToolApp.getInstance().getClipboardManager().clearClipboard();
		}
        c.repaint();
	}

    //TO BE DELETED
    @Deprecated
	private void cutAction(TabbedPaneController tabbedPaneController, Canvas c,
			CommandManager commandManager, SelectionModel selectionModel) {
		
        //pravim listu elemenata za izbaciti iz selekcije
        List<VisibleElement> cutted = new ArrayList<VisibleElement>();
        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
            	cutted.add(visibleElement);
            }
        }
        KrokiMockupToolApp.getInstance().getClipboardManager().cutSelectedElements();
        cutAction = true;

        if (cutted.size() > 0) {
        	RemoveCommand removeCommand = new RemoveCommand(cutted);
            commandManager.addCommand(removeCommand);
            selectionModel.removeFromSelection(cutted);
        } 

        c.repaint();	
	}

	private void deleteAction(TabbedPaneController tabbedPaneController, Canvas c, CommandManager commandManager, SelectionModel selectionModel) {
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
