/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.state;

import java.awt.Image;
import java.awt.event.MouseEvent;

import kroki.api.profil.group.ElementsGroupUtil;
import kroki.api.profil.panel.VisibleClassUtil;
import kroki.app.command.AddCommand;
import kroki.app.command.CommandManager;
import kroki.app.controller.TabbedPaneController;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;

/**
 * Klasa koja predstavlja stanje dodavanja elementa.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddState extends State {

	private VisibleElement element;
	private Image addEnabledIcon;
	private Image addDisabledIcon;

	public AddState(Context context) {
		super(context, "app.state.add");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		TabbedPaneController tabbedPaneController = context.getTabbedPaneController();
		Canvas canvas = tabbedPaneController.getCurrentTabContent();
		VisibleClass visibleClass = canvas.getVisibleClass();
		VisibleElement visibleElement = VisibleClassUtil.getVisibleElementAtPoint(visibleClass, e.getPoint());
		boolean flag = false;
		if (visibleElement == null) {
			flag = false;
		} else if (visibleElement instanceof ElementsGroup) {
			ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
			flag = ElementsGroupUtil.checkIfCanAdd(elementsGroup, element);
		}
		if (flag) {
			tabbedPaneController.changeCursorImage(addEnabledIcon);
		} else {
			tabbedPaneController.changeCursorImage(addDisabledIcon);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		TabbedPaneController tabbedPaneController = context.getTabbedPaneController();
		Canvas canvas = context.getTabbedPaneController().getCurrentTabContent();
		CommandManager commandManager = canvas.getCommandManager();
		if (e.getButton() == MouseEvent.BUTTON1) {
			VisibleClass visibleClass = canvas.getVisibleClass();
			ElementsGroup elementsGroup = VisibleClassUtil.getElementsGroupAtPoint(visibleClass, e.getPoint());
			if (elementsGroup != null) {
				if (!ElementsGroupUtil.checkIfCanAdd(elementsGroup, element)) {
					return;
				}
				AddCommand addCommand = new AddCommand(visibleClass, elementsGroup, element, e.getPoint());
				if (element.getComponentType() != null){
					VisibleClassUtil.incrementCount(visibleClass, element.getComponentType());
					//set label so it contains updated count
					String newLabel = element.getComponentType().toString() + "_" + VisibleClassUtil.getComponentCount(visibleClass, element.getComponentType()); 
					element.setLabel(newLabel);
					element.update();
				}
				commandManager.addCommand(addCommand);
			} else {
				return;
			}
			element = null;
			tabbedPaneController.changeCursorImage(null);
			canvas.repaint();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			tabbedPaneController.changeCursorImage(null);
		} else {
			return;
		}
		context.goNext(SELECT_STATE);
	}

	public VisibleElement getElement() {
		return element;
	}

	public void setElement(VisibleElement element) {
		this.element = element;
	}

	public Image getAddDisabledIcon() {
		return addDisabledIcon;
	}

	public void setAddDisabledIcon(Image addDisabledIcon) {
		this.addDisabledIcon = addDisabledIcon;
	}

	public Image getAddEnabledIcon() {
		return addEnabledIcon;
	}

	public void setAddEnabledIcon(Image addEnabledIcon) {
		this.addEnabledIcon = addEnabledIcon;
	}
}