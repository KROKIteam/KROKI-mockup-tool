package kroki.profil.utils;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.Button;
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
import kroki.profil.panel.StandardPanel;

/**
* Class contains <code>StandardPanel</code> util methods 
* @author Kroki Team
*/
public class StandardPanelUtil {

	  
	/**
	 * Sets default gui settings of the given panel.
	 * Creates toolbar, properties panel, option panel etc.
	 * @param panel Panel
	 */
   public static void defaultGuiSettings(StandardPanel panel) {
       
       Composite root = ((Composite) panel.getComponent());
       root.setLayoutManager(new BorderLayoutManager());
       
       panel.setToolbarPanel(new ElementsGroup("toolbar", ComponentType.PANEL));
       panel.getToolbarPanel().setGroupOrientation(GroupOrientation.horizontal);
       panel.getToolbarPanel().setGroupAlignment(GroupAlignment.left);
       panel.getToolbarPanel().setGroupLocation(GroupLocation.toolbar);
       LayoutManager toolbarLayout = new FlowLayoutManager();
       toolbarLayout.setAlign(LayoutManager.LEFT);
       ((Composite) panel.getToolbarPanel().getComponent()).setLayoutManager(toolbarLayout);
       ((Composite) panel.getToolbarPanel().getComponent()).setLocked(true);

       panel.setPropertiesPanel(new ElementsGroup("properties", ComponentType.PANEL));
       panel.getPropertiesPanel().setGroupLocation(GroupLocation.componentPanel);
       panel.getPropertiesPanel().setGroupOrientation(GroupOrientation.vertical);
       LayoutManager propertiesLayout = new VerticalLayoutManager(10, 10, VerticalLayoutManager.LEFT);
       ((Composite)  panel.getPropertiesPanel().getComponent()).setLayoutManager(propertiesLayout);
       ((Composite)  panel.getPropertiesPanel().getComponent()).setLocked(true);

       panel.setOperationsPanel(new ElementsGroup("operations", ComponentType.PANEL));
       panel.getOperationsPanel().setGroupLocation(GroupLocation.operationPanel);
       panel.getOperationsPanel().setGroupOrientation(GroupOrientation.vertical);
       panel.getOperationsPanel().setGroupAlignment(GroupAlignment.center);
       LayoutManager operationsLayout = new VerticalLayoutManager();
       operationsLayout.setAlign(LayoutManager.CENTER);
       ((Composite) panel.getOperationsPanel().getComponent()).setLayoutManager(operationsLayout);
       ((Composite) panel.getOperationsPanel().getComponent()).setLocked(true);

       UIPropertyUtil.addVisibleElement(panel, panel.getToolbarPanel());
       UIPropertyUtil.addVisibleElement(panel, panel.getPropertiesPanel());
       UIPropertyUtil.addVisibleElement(panel, panel.getOperationsPanel());

       createMockupForStandardOperations(panel);
       initializeStandardToolbar(panel);

       root.addChild(panel.getToolbarPanel().getComponent(), BorderLayoutManager.NORTH);
       root.addChild(panel.getPropertiesPanel().getComponent(), BorderLayoutManager.CENTER);
       root.addChild(panel.getOperationsPanel().getComponent(), BorderLayoutManager.EAST);

       panel.update();

   }

   /**
    * Creates mockup components which contain standard toolbar operations 
    * @param panel Panel
    */
   private static void createMockupForStandardOperations(StandardPanel panel) {
       panel.setAddButton(new Button());
       panel.getAddButton().setImage(new SerializableBufferedImage("plus"));
       panel.getAddButton().updateComponent();

       panel.setUpdateButton(new Button());
       panel.getUpdateButton().setImage(new SerializableBufferedImage("pencil"));
       panel.getUpdateButton().updateComponent();

       panel.setCopyButton(new Button());
       panel.getCopyButton().setImage(new SerializableBufferedImage("copy"));
       panel.getCopyButton().updateComponent();

       panel.setDeleteButton(new Button());
       panel.getDeleteButton().setImage(new SerializableBufferedImage("minus"));
       panel.getDeleteButton().updateComponent();

       panel.setSearchButton(new Button());
       panel.getSearchButton().setImage(new SerializableBufferedImage("zoom"));
       panel.getSearchButton().updateComponent();

       panel.setChangeModeButton(new Button());
       panel.getChangeModeButton().setImage(new SerializableBufferedImage("reload-1"));
       panel.getChangeModeButton().updateComponent();

       panel.setFirstButton(new Button());
       panel.getFirstButton().setImage(new SerializableBufferedImage("arrow-first"));
       panel.getFirstButton().updateComponent();

       panel.setPreviuosButton(new Button());
       panel.getPreviuosButton().setImage(new SerializableBufferedImage("arrow-left"));
       panel.getPreviuosButton().updateComponent();

       panel.setNextButton(new Button());
       panel.getNextButton().setImage(new SerializableBufferedImage("arrow-right"));
       panel.getNextButton().updateComponent();

       panel.setLastButton(new Button());
       panel.getLastButton().setImage(new SerializableBufferedImage("arrow-last"));
       panel.getLastButton().updateComponent();
   }

   /**
    * Initializes standard toolbar (which contains operations such as first, previous, next, last, add ...)
    * Checks if the operations should be present on panel
    * @param panel Panel
    */
   public static void initializeStandardToolbar(StandardPanel panel) {
       Composite composite = (Composite) panel.getToolbarPanel().getComponent();
       if (panel.isSearch()) {
           composite.addChild(panel.getSearchButton());
       }
       if (panel.isDataNavigation()) {
           composite.addChild(panel.getFirstButton());
           composite.addChild(panel.getPreviuosButton());
           composite.addChild(panel.getNextButton());
           composite.addChild(panel.getLastButton());
       }
       if (panel.isAdd()) {
           composite.addChild(panel.getAddButton());
       }
       if (panel.isUpdate()) {
           composite.addChild(panel.getUpdateButton());
       }
       if (panel.isDelete()) {
           composite.addChild(panel.getDeleteButton());
       }
       if (panel.isCopy()) {
           composite.addChild(panel.getCopyButton());
       }
       if (panel.isChangeMode()) {
           composite.addChild(panel.getChangeModeButton());
       }
   }
}
