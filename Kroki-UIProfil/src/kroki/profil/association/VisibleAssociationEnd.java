/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.association;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.std.StdDataSettings;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.VisibleAssociationEndSettings;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(VisibleAssociationEndSettings.class)
public class VisibleAssociationEnd extends VisibleElement implements UmlProperty {

    /**Dozvoljen/zabranjen unos novih podataka */
    protected boolean add = true;
    /**Dozvoljena/zabranjena izmena podataka */
    protected boolean update = true;
    /**Dozvoljeno/zabranjeno kopiranje podataka */
    protected boolean copy = true;
    /**Dozvoljeno/zabranjeno brisanje podataka */
    protected boolean delete = true;
    /**Dozvoljena/zabranjena pretraga podataka */
    protected boolean search = true;
    /**Dozvoljena/zabranjena promena prikaza (iz tabelarnog u “jedan ekran–jedan slog” i obrnuto */
    protected boolean changeMode = true;
    /**Dozvoljeno/zabranjeno kretanje kroz redove (prelazak na prvi, sledeći, prethodni i poslednji) */
    protected boolean dataNavigation = true;
    /**Standardna podesavanja panela*/
    protected StdPanelSettings stdPanelSettings = new StdPanelSettings();
    /**Standardna podesavanja podataka*/
    protected StdDataSettings stdDataSettings = new StdDataSettings();
    /**Panel sa kojeg se aktivira*/
    protected VisibleClass activationPanel;
    /**Panel koji se aktivira*/
    protected VisibleClass targetPanel;

    /*OBELEŽJA METAKLASE PROPERTY*/
    protected boolean isComposite = false;
    protected boolean isDerived = false;
    protected boolean isReadOnly = false;
    protected UmlProperty opposite = null;
    protected UmlClass umlClass;
    /*OBELEŽJA METAKLASE TYPEDELEMENT*/
    protected UmlType umlType;
    /*OBELEŽJA METAKLASE MULTIPLICITYELEMENT*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    public VisibleAssociationEnd() {
    	
    }

    public VisibleAssociationEnd(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }
    
    @Override
    public String toString(){
    	return label;
    }

    /**************************************/
    /*IMPLEMENTIRANE METODE OD UmlProperty*/
    /**************************************/
    public String getDefault() {
        return "";
    }

    public boolean isComposite() {
        return isComposite;
    }

    public boolean isDerived() {
        return isDerived;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public UmlProperty opposite() {
        return opposite;
    }

    public void setComposite(boolean isComposite) {
        this.isComposite = isComposite;
    }

    public void setDefault(String def) {
        //this.defaultValue = def;
    }

    public void setDerived(boolean isDerived) {
        this.isDerived = isDerived;
    }

    public void setOpposite(UmlProperty opposite) {
        this.opposite = opposite;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void setUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass umlClass() {
        return umlClass;
    }

    public void setType(UmlType umlType) {
        this.umlType = umlType;
    }

    public UmlType type() {
        return umlType;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public int lower() {
        return lower;
    }

    public int upper() {
        return upper;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public void setOrdered(boolean ordered) {
        this.isOrdered = ordered;
    }

    public void setUnique(boolean unique) {
        this.isUnique = unique;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public StdDataSettings getStdDataSettings() {
        return stdDataSettings;
    }

    public void setStdDataSettings(StdDataSettings stdDataSettings) {
        this.stdDataSettings = stdDataSettings;
    }

    public StdPanelSettings getStdPanelSettings() {
        return stdPanelSettings;
    }

    public void setStdPanelSettings(StdPanelSettings stdPanelSettings) {
        this.stdPanelSettings = stdPanelSettings;
    }

    public VisibleClass getActivationPanel() {
        return activationPanel;
    }

    public void setActivationPanel(VisibleClass activationPanel) {
        this.activationPanel = activationPanel;
    }

    public VisibleClass getTargetPanel() {
        return targetPanel;
    }

    public void setTargetPanel(VisibleClass targetPanel) {
        this.targetPanel = targetPanel;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isDataNavigation() {
        return dataNavigation;
    }

    public void setDataNavigation(boolean dataNavigation) {
        this.dataNavigation = dataNavigation;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
