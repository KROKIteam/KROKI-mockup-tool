package adapt.resources;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.exceptions.EntityAttributeNotFoundException;
import adapt.exceptions.PanelTypeParsingException;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;
import adapt.model.panel.AdaptPanel;
import adapt.model.panel.AdaptParentChildPanel;
import adapt.model.panel.AdaptStandardPanel;
import adapt.model.panel.configuration.PanelSettings;
import adapt.util.ejb.EntityHelper;
import adapt.util.ejb.PersisenceHelper;
import adapt.util.html.HTMLToolbarAction;
import adapt.util.html.TableModel;
import adapt.util.resolvers.PanelTypeResolver;
import adapt.util.staticnames.Settings;
import adapt.util.xml_readers.PanelReader;

public class ViewResource extends BaseResource {

	public ViewResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	@Override
	public void handleGet() {
		try {
			String panelName = (String)getRequest().getAttributes().get("activate");
			String childPanelName  = (String)getRequest().getAttributes().get("childPanelName");
			String associationEnd = (String)getRequest().getAttributes().get("associationEnd");
			String pid = (String) getRequest().getAttributes().get("pid");				
			PanelType panelType = PanelType.STANDARDPANEL;
			ArrayList<AdaptStandardPanel> panels = new ArrayList<AdaptStandardPanel>();
			
			//Standard form request
			if(panelName != null) {
				System.out.println("[HANDLE GET] panelName = " + panelName);
				AdaptPanel panel = PanelReader.loadPanel(panelName, panelType, null, OpenedAs.DEFAULT);
				if(panelType == PanelType.STANDARDPANEL) {
					AdaptStandardPanel stdPanel = (AdaptStandardPanel) panel;
					String query = "FROM " + stdPanel.getEntityBean().getEntityClass().getName();
					prepareContent(panel, query);
					panels.add(stdPanel);
				}else if (panelType == PanelType.PARENTCHILDPANEL) {
					AdaptParentChildPanel pcPanel = (AdaptParentChildPanel)panel;
					for (AdaptStandardPanel adaptStandardPanel : pcPanel.getPanels()) {
						String query = "FROM " + adaptStandardPanel.getEntityBean().getEntityClass().getName();
						prepareContent(adaptStandardPanel, query);
						panels.add(adaptStandardPanel);
					}
				}
			}
			
			//Child form request
			//Association end = name of the join column in child entity associated with parent on current parent-child panel
			if(childPanelName != null && associationEnd != null && pid != null) {
				AdaptStandardPanel childPanel  = (AdaptStandardPanel) PanelReader.loadPanel(childPanelName,  PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
				EntityBean childBean = childPanel.getEntityBean();
				try {
					//Get join column for this form, so entities could be queried based on it
					JoinColumnAttribute jcAttribute = EntityHelper.getJoinByFieldName(childBean, associationEnd);
					String query = "FROM " + childBean.getEntityClass().getName() + " x WHERE x." + jcAttribute.getFieldName() + ".id = " + pid;
					prepareContent(childPanel, query);
					panels.add(childPanel);
				} catch (EntityAttributeNotFoundException ea) {
					ea.printStackTrace();
				}
			}
			
			addToDataModel("panels", panels);
		} catch (Exception e) {
			e.printStackTrace();
			AppCache.displayTextOnMainFrame("Error fetching panel data.", 1);
		}
		super.handleGet();
	}

	//Read data for panel that needs to be displayed and pass it to freemarker template
	public void prepareContent(AdaptPanel panel, String query) {
		if(panel != null) {
			//switch (panelType) {
			//case STANDARDPANEL:
				AdaptStandardPanel stdPanel = (AdaptStandardPanel) panel;
				addToDataModel("panel", stdPanel);
				addToDataModel("entityClassName", stdPanel.getEntityBean().getEntityClass().getName());
				prepareTableControls(stdPanel);
				prepareTableData(stdPanel, query);
				prepareInputForm(stdPanel);
				//break;
			//}
		}
	}

	public void prepareTableData(AdaptStandardPanel panel, String query) {
		EntityManager em = PersisenceHelper.createEntityManager();

		em.getTransaction().begin();
		Query q = em.createQuery(query);
		List<Object> results = q.getResultList();
		
		TableModel model = new TableModel(panel.getEntityBean());
		ArrayList<LinkedHashMap<String, String>> tableModel = model.getModel(results);
		addToDataModel("tableModel", tableModel);
		
		em.getTransaction().commit();
		em.close();
	}

	// set table controls based on panel settings
	public void prepareTableControls(AdaptStandardPanel panel) {
		ArrayList<HTMLToolbarAction> controls = new ArrayList<HTMLToolbarAction>();
		PanelSettings settings = panel.getPanelSettings();
		if(settings.getAdd()) {
			HTMLToolbarAction addAction = new HTMLToolbarAction("btnAdd", "Add new entity", "/files/images/icons-white/add.png", false);
			controls.add(addAction);
		}
		if(settings.getUpdate() && settings.getChangeMode()) {
			HTMLToolbarAction editAction = new HTMLToolbarAction("btnSwitch", "Modify entity", "/files/images/icons-white/swich.png", false);
			controls.add(editAction);
		}
		if(settings.getDelete()) {
			HTMLToolbarAction deleteAction = new  HTMLToolbarAction("btnDelete", "Delete entity", "/files/images/icons-white/delete.png", true);
			controls.add(deleteAction);
		}
		HTMLToolbarAction nextAction = new HTMLToolbarAction("btnNextForms", "View connected forms", "/files/images/icons-white/next-forms.png", false);
		controls.add(nextAction);
		addToDataModel("tableControls", controls);
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		return getHTMLTemplateRepresentation("formTemplate.html", dataModel);
	}
}
