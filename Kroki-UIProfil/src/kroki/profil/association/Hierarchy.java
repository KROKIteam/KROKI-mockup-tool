package kroki.profil.association;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.NullComponent;
import kroki.profil.ComponentType;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;

/**
 * Stereotype Hierarchy specifies that the target panel is
 * an element of the hierarchy contained by the activation panel 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Hierarchy extends VisibleAssociationEnd {

	private static final long serialVersionUID = 1L;
	
	/**Panel's level inside the hierarchy*/
	private int level;
	/**The association end through which panel is connected*/
	private VisibleAssociationEnd viaAssociationEnd;
	/**
	 * Standard panel which tags dataFilter,  add,  update,  copy,  delete,  search,
	 * changeMode, dataNavigation, defaultViewMode and defaultOperationMode are applied to.
	 * If it is defined, it can either be a target panel or a panel which is
	 * directly or indirectly contained by the target panel
	 */
	private VisibleClass appliedToPanel;
	
	/*ADDITIONL ATTRIBUTES WHICH ARE NOT PART OF THE PROFILE*/
	/**Hierarchy parent. Used to determine viaAssociationEnd and level attributes*/ 
	private Hierarchy hierarchyParent;
	/**Clone of the target panel - needed to show the panel and changes made to it*/
	transient private VisibleClass targetPanelClone;

	public Hierarchy(String label, boolean visible, ComponentType componentType) {
		super(label, visible, componentType);
	}

	public Hierarchy() {
		super();
		label = "Hierarchy";
		this.component = new NullComponent(label);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public String toString() {
		String ret = label;
		ret += "[ level = " +level + "]"; 
		return ret;
	}


	/************************************************/
	/*VISIBLE ASSOCIATION END METHODS*/
	/************************************************/
	@Override
	public void setAdd(boolean add) {
		this.add = add;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getAddButton();
			if (this.add) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	@Override
	public void setChangeMode(boolean changeMode) {
		this.changeMode = changeMode;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getChangeModeButton();
			if (this.changeMode) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	@Override
	public void setCopy(boolean copy) {
		this.copy = copy;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getCopyButton();
			if (this.copy) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	@Override
	public void setDataNavigation(boolean dataNavigation) {
		this.dataNavigation = dataNavigation;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			this.dataNavigation = dataNavigation;
			Button firstButton = ((StandardPanel) targetPanelClone).getFirstButton();
			Button previousButton = ((StandardPanel) targetPanelClone).getPreviuosButton();
			Button nextButton = ((StandardPanel) targetPanelClone).getNextButton();
			Button lastButton = ((StandardPanel) targetPanelClone).getLastButton();
			if (this.dataNavigation) {
				toolbarPanel.addChild(firstButton);
				toolbarPanel.addChild(previousButton);
				toolbarPanel.addChild(nextButton);
				toolbarPanel.addChild(lastButton);
			} else {
				toolbarPanel.removeChild(firstButton);
				toolbarPanel.removeChild(previousButton);
				toolbarPanel.removeChild(nextButton);
				toolbarPanel.removeChild(lastButton);
			}
		}
	}

	@Override
	public void setDelete(boolean delete) {
		this.delete = delete;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getDeleteButton();
			if (this.delete) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	@Override
	public void setSearch(boolean search) {
		this.search = search;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getSearchButton();
			if (this.search) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	@Override
	public void setUpdate(boolean update) {
		this.update = update;
		if (targetPanelClone instanceof StandardPanel) {
			Composite toolbarPanel = (Composite) ((StandardPanel) targetPanelClone).getToolbarPanel().getComponent();
			Button btn = ((StandardPanel) targetPanelClone).getUpdateButton();
			if (this.update) {
				toolbarPanel.addChild(btn);
			} else {
				toolbarPanel.removeChild(btn);
			}
		}
	}

	/*****************/
	/*GETTERS AND SETTERS*/
	/*****************/
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public VisibleAssociationEnd getViaAssociationEnd() {
		return viaAssociationEnd;
	}

	public void setViaAssociationEnd(VisibleAssociationEnd viaAssociationEnd) {
		this.viaAssociationEnd = viaAssociationEnd;
	}

	public VisibleClass getAppliedToPanel() {
		return appliedToPanel;
	}

	public void setAppliedToPanel(VisibleClass appliedToPanel) {
		this.appliedToPanel = appliedToPanel;
	}

	public Hierarchy getHierarchyParent() {
		return hierarchyParent;
	}

	public void setHierarchyParent(Hierarchy hierarchyParent){
		this.hierarchyParent = hierarchyParent;
	}

	
	public VisibleClass getTargetPanelClone() {
		return targetPanelClone;
	}

	public void setTargetPanelClone(VisibleClass targetPanelClone) {
		this.targetPanelClone = targetPanelClone;
	}
}
