/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.association;

import kroki.common.copy.DeepCopy;
import kroki.mockup.model.Composite;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.NullComponent;
import kroki.profil.ComponentType;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.settings.HierarchySettings;
import kroki.profil.utils.settings.SettingsPanel;

/**
 * Stereotip  Hierarchy  označava da odredišni panel ima ulogu
 * elementa hijerarhije u okviru “Parent-Child” aktivacionog panela
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(HierarchySettings.class)
public class Hierarchy extends VisibleAssociationEnd {

    /**Nivo panela u okviru hijerarhije.*/
    private int level;
    /**Kraj (obeležje) asocijacije preko koga se ostvaruje povezivanje panela*/
    private VisibleAssociationEnd viaAssociationEnd;
    /**
     * Standardni panel na koji se primenjuju tagovi
     * dataFilter,  add,  update,  copy,  delete,  search,
     * changeMode, dataNavigation, defaultViewMode i
     * defaultOperationMode. Ako je definisan, može
     * biti ili odredišni panel ili panel koji je sadržan
     * (direktno ili indirektno) u okviru odredišnog
     * panela.
     */
    private VisibleClass appliedToPanel;
    /*POMOCNI ATRIBUTI - NISU DEO UI PROFILA*/
    /**Roditelj hijerarhije. Na osnovu njega se vrsi odredjivanje viaAssociationEnd, level*/
    Hierarchy hierarchyParent;
    /**Ovaj atribut predstavlja klon targetPanela - potreban je za iscrtavanja i prikazivanja promena koje su nastale na njemu*/
    private VisibleClass targetPanelClone;

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

    public void forceUpdateComponent() {
        if (targetPanel != null) {
            if (component != null) {
                //kloniram ceo target panel zbog prikaza!!!
                targetPanelClone = (VisibleClass) DeepCopy.copy(targetPanel);
                //FIXME: ukoliko su neke od akcija u targetPanelu zabranjene one se ne mogu ododbriti od strane kraja asocijacije!!!!
//                if (targetPanelClone instanceof StandardPanel) {
//                    setAdd(add);
//                    setChangeMode(changeMode);
//                    setCopy(copy);
//                    setDataNavigation(dataNavigation);
//                    setDelete(delete);
//                    setSearch(search);
//                    setUpdate(update);
//                }

                int relX = component.getRelativePosition().x;
                int relY = component.getRelativePosition().y;
                int absX = component.getAbsolutePosition().x;
                int absY = component.getAbsolutePosition().y;
                ((Composite) this.getParentGroup().getComponent()).removeChild(component);
                this.component = targetPanelClone.getComponent();
                this.component.getAbsolutePosition().setLocation(absX, absY);
                this.component.getRelativePosition().setLocation(relX, relY);
                ((Composite) this.getParentGroup().getComponent()).addChild(component);
                label = targetPanel.getLabel();
            }
        }
    }

    /************************************************/
    /*REDEFINISANE METODE OD VISIBLE ASSOCIATION END*/
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
    /*GETERI I SETERI*/
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

    public void setHierarchyParent(Hierarchy hierarchyParent) {
        this.hierarchyParent = hierarchyParent;
        if (hierarchyParent != null) {
            this.level = this.hierarchyParent.getLevel() + 1;
        }
    }

    public VisibleClass getTargetPanelClone() {
        return targetPanelClone;
    }

    public void setTargetPanelClone(VisibleClass targetPanelClone) {
        this.targetPanelClone = targetPanelClone;
    }
}
