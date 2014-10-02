/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.Button;
import kroki.mockup.model.components.TitledContainer;
import kroki.mockup.model.layout.BorderLayoutManager;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupLocation;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.std.StdDataSettings;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.persistent.PersistentClass;
import kroki.profil.utils.settings.SettingsPanel;
import kroki.profil.utils.settings.StandardPanelSettings;

/**
 * <code>StandardPanel</code> oznacava da se datoj perzistentnoj klasi (u
 * slucaju troslojnog resenja) ili odgovarajucoj tabeli nastaloj mapiranjem
 * objektnog na relacioni model (u slucaju dvoslojnog resenja) pridruzuje
 * standardni panel ciji su izgled i funkcionalnost definisani HCI standardom.
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
@SettingsPanel(StandardPanelSettings.class)
public class StandardPanel extends VisibleClass {

	private static final long serialVersionUID = 1L;
	
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
    /**Dozvoljena/zabranjena promena prikaza (iz tabelarnog u â€œjedan ekranâ€“jedan slogâ€� i obrnuto */
    protected boolean changeMode = true;
    /**Dozvoljeno/zabranjeno kretanje kroz redove (prelazak na prvi, sledeÄ‡i, prethodni i poslednji) */
    protected boolean dataNavigation = true;
    /**PodeÅ¡avanja standardnog panela*/
    protected StdPanelSettings stdPanelSettings = new StdPanelSettings();
    /**PodeÅ¡avanja vezana za podatke koje standardni panel prikazuje*/
    protected StdDataSettings stdDataSettings = new StdDataSettings();
    /**Perzistentna klasa koja je vezana za standardni panel*/
    protected PersistentClass persistentClass;

    /*mockup componente standardnih operacija*/
    private Button addButton;
    private Button updateButton;
    private Button copyButton;
    private Button deleteButton;
    private Button searchButton;
    private Button changeModeButton;
    private Button firstButton;
    private Button previuosButton;
    private Button nextButton;
    private Button lastButton;
    /*paneli koji sacinjavaju standardnu formu*/
    private ElementsGroup toolbarPanel;
    //panel sa komponentama
    private ElementsGroup propertiesPanel;
    //panel sa desne strane u koji se smestaju operacije (transakcije, izvestaji,...)
    private ElementsGroup operationsPanel;
    
    /*****************/
    /*Konstruktori   */
    /*****************/
    public StandardPanel() {
        super();
        component = new TitledContainer("StandardForm");
        component.getRelativePosition().setLocation(5, 5);
        component.getAbsolutePosition().setLocation(5, 5);
        component.getDimension().setSize(800, 500);
        component.getElementPainter().update();
        defaultGuiSettings();
        persistentClass = new PersistentClass();
    }

    public StandardPanel(String label, boolean visible, ComponentType componentType, boolean modal) {
        super(label, visible, componentType, modal);
        defaultGuiSettings();
        persistentClass = new PersistentClass();
    }

    public StandardPanel(boolean modal) {
        super(modal);
        defaultGuiSettings();
        persistentClass = new PersistentClass();
    }

      /********************/
     /*Private operations*/
    /********************/
    private void defaultGuiSettings() {
        //root komponenta i njen layout manager
        Composite root = ((Composite) component);
        root.setLayoutManager(new BorderLayoutManager());
        //OVO JE NOVO
        toolbarPanel = new ElementsGroup("toolbar", ComponentType.PANEL);
        toolbarPanel.setGroupOrientation(GroupOrientation.horizontal);
        toolbarPanel.setGroupAlignment(GroupAlignment.left);
        toolbarPanel.setGroupLocation(GroupLocation.toolbar);
        LayoutManager toolbarLayout = new FlowLayoutManager();
        toolbarLayout.setAlign(LayoutManager.LEFT);
        ((Composite) toolbarPanel.getComponent()).setLayoutManager(toolbarLayout);
        ((Composite) toolbarPanel.getComponent()).setLocked(true);

        propertiesPanel = new ElementsGroup("properties", ComponentType.PANEL);
        propertiesPanel.setGroupLocation(GroupLocation.componentPanel);
        propertiesPanel.setGroupOrientation(GroupOrientation.vertical);
        LayoutManager propertiesLayout = new VerticalLayoutManager(10, 10, VerticalLayoutManager.LEFT);
        ((Composite) propertiesPanel.getComponent()).setLayoutManager(propertiesLayout);
        ((Composite) propertiesPanel.getComponent()).setLocked(true);

        operationsPanel = new ElementsGroup("operations", ComponentType.PANEL);
        operationsPanel.setGroupLocation(GroupLocation.operationPanel);
        operationsPanel.setGroupOrientation(GroupOrientation.vertical);
        operationsPanel.setGroupAlignment(GroupAlignment.center);
        LayoutManager operationsLayout = new VerticalLayoutManager();
        operationsLayout.setAlign(LayoutManager.CENTER);
        ((Composite) operationsPanel.getComponent()).setLayoutManager(operationsLayout);
        ((Composite) operationsPanel.getComponent()).setLocked(true);

        addVisibleElement(toolbarPanel);
        addVisibleElement(propertiesPanel);
        addVisibleElement(operationsPanel);

        createMockupForStandardOperations();
        initializeStandardToolbar();

        root.addChild(toolbarPanel.getComponent(), BorderLayoutManager.NORTH);
        root.addChild(propertiesPanel.getComponent(), BorderLayoutManager.CENTER);
        root.addChild(operationsPanel.getComponent(), BorderLayoutManager.EAST);

        update();

    }

    /**
     * Kreira mockup komponente za operacije u standardnom toolbaru
     */
    private void createMockupForStandardOperations() {
        addButton = new Button();
        addButton.setImage(new SerializableBufferedImage("plus"));
        addButton.updateComponent();

        updateButton = new Button();
        updateButton.setImage(new SerializableBufferedImage("pencil"));
        updateButton.updateComponent();

        copyButton = new Button();
        copyButton.setImage(new SerializableBufferedImage("copy"));
        copyButton.updateComponent();

        deleteButton = new Button();
        deleteButton.setImage(new SerializableBufferedImage("minus"));
        deleteButton.updateComponent();

        searchButton = new Button();
        searchButton.setImage(new SerializableBufferedImage("zoom"));
        searchButton.updateComponent();

        changeModeButton = new Button();
        changeModeButton.setImage(new SerializableBufferedImage("reload-1"));
        changeModeButton.updateComponent();

        firstButton = new Button();
        firstButton.setImage(new SerializableBufferedImage("arrow-first"));
        firstButton.updateComponent();

        previuosButton = new Button();
        previuosButton.setImage(new SerializableBufferedImage("arrow-left"));
        previuosButton.updateComponent();

        nextButton = new Button();
        nextButton.setImage(new SerializableBufferedImage("arrow-right"));
        nextButton.updateComponent();

        lastButton = new Button();
        lastButton.setImage(new SerializableBufferedImage("arrow-last"));
        lastButton.updateComponent();
    }

    public void initializeStandardToolbar() {
        Composite composite = (Composite) toolbarPanel.getComponent();
        if (search) {
            composite.addChild(searchButton);
        }
        if (dataNavigation) {
            composite.addChild(firstButton);
            composite.addChild(previuosButton);
            composite.addChild(nextButton);
            composite.addChild(lastButton);
        }
        if (add) {
            composite.addChild(addButton);
        }
        if (update) {
            composite.addChild(updateButton);
        }
        if (delete) {
            composite.addChild(deleteButton);
        }
        if (copy) {
            composite.addChild(copyButton);
        }
        if (changeMode) {
            composite.addChild(changeModeButton);
        }
    }

    @Override
    public void update() {
        component.updateComponent();
        ((Composite) component).layout();
    }

    @Override
    public String toString() {
        return label;
    }

    /*****************/
    /*Geteri i seteri*/
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

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
        if (this.add) {
            ((Composite) toolbarPanel.getComponent()).addChild(addButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(addButton);
        }

    }

    public boolean isChangeMode() {
        return changeMode;
    }

    public void setChangeMode(boolean changeMode) {
        this.changeMode = changeMode;
        if (this.changeMode) {
            ((Composite) toolbarPanel.getComponent()).addChild(changeModeButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(changeModeButton);
        }
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
        if (this.copy) {
            ((Composite) toolbarPanel.getComponent()).addChild(copyButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(copyButton);
        }
    }

    public boolean isDataNavigation() {
        return dataNavigation;
    }

    public void setDataNavigation(boolean dataNavigation) {
        this.dataNavigation = dataNavigation;
        if (this.dataNavigation) {
            ((Composite) toolbarPanel.getComponent()).addChild(firstButton);
            ((Composite) toolbarPanel.getComponent()).addChild(previuosButton);
            ((Composite) toolbarPanel.getComponent()).addChild(nextButton);
            ((Composite) toolbarPanel.getComponent()).addChild(lastButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(firstButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(previuosButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(nextButton);
            ((Composite) toolbarPanel.getComponent()).removeChild(lastButton);
        }
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
        if (this.delete) {
            ((Composite) toolbarPanel.getComponent()).addChild(deleteButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(deleteButton);
        }
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
        if (this.search) {
            ((Composite) toolbarPanel.getComponent()).addChild(searchButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(searchButton);
        }
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
        if (this.update) {
            ((Composite) toolbarPanel.getComponent()).addChild(updateButton);
        } else {
            ((Composite) toolbarPanel.getComponent()).removeChild(updateButton);
        }
    }
    
    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public Button getChangeModeButton() {
        return changeModeButton;
    }

    public void setChangeModeButton(Button changeModeButton) {
        this.changeModeButton = changeModeButton;
    }

    public Button getCopyButton() {
        return copyButton;
    }

    public void setCopyButton(Button copyButton) {
        this.copyButton = copyButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getFirstButton() {
        return firstButton;
    }

    public void setFirstButton(Button firstButton) {
        this.firstButton = firstButton;
    }

    public Button getLastButton() {
        return lastButton;
    }

    public void setLastButton(Button lastButton) {
        this.lastButton = lastButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public void setNextButton(Button nextButton) {
        this.nextButton = nextButton;
    }

    public ElementsGroup getOperationsPanel() {
        return operationsPanel;
    }

    public void setOperationsPanel(ElementsGroup operationsPanel) {
        this.operationsPanel = operationsPanel;
    }

    public Button getPreviuosButton() {
        return previuosButton;
    }

    public void setPreviuosButton(Button previuosButton) {
        this.previuosButton = previuosButton;
    }

    public ElementsGroup getPropertiesPanel() {
        return propertiesPanel;
    }

    public void setPropertiesPanel(ElementsGroup propertiesPanel) {
        this.propertiesPanel = propertiesPanel;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public ElementsGroup getToolbarPanel() {
        return toolbarPanel;
    }

    public void setToolbarPanel(ElementsGroup toolbarPanel) {
        this.toolbarPanel = toolbarPanel;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public PersistentClass getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
    }
}
