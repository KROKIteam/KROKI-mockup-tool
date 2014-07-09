package com.panelcomposer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JComponent;

import util.EntityHelper;
import util.StringUtil;
import util.resolvers.ComponentResolver;
import util.xml_readers.PanelReader;

import com.panelcomposer.core.AppCache;
import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.MPanel;
import com.panelcomposer.model.panel.MStandardPanel;
import com.panelcomposer.model.panel.configuration.Zoom;

public class ZoomActionListener implements ActionListener {

	protected JoinColumnAttribute jcAttr;
	protected SPanel panel;
	protected Zoom zoom;
	
	public ZoomActionListener(Zoom zoom, JoinColumnAttribute jcAttr, SPanel panel) {
		this.jcAttr = jcAttr;
		this.panel = panel;
		this.zoom = zoom;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			Class<?> zoomClass = jcAttr.getLookupClass();
			String panelId = AppCache.getInstance().getPanelId(zoomClass.getName());
			String zoomName = null;
			
			if(zoom != null) 
				zoomName = zoom.getName();
			
			MPanel mp = PanelReader.loadPanel(panelId, PanelType.STANDARDPANEL, 
					zoomName, OpenedAs.ZOOM);
			MStandardPanel msp = (MStandardPanel) mp;
			Object objZoomed = null;
			SForm sf = new SForm(msp, panel, OpenedAs.ZOOM, null);
			sf.setVisible(true);
			objZoomed = sf.getPanels().get(0).getModelPanel().getDataSettings().getZoomed();
			
			if(objZoomed != null) {
				EntityBean bean = panel.getModelPanel().getEntityBean();
				int index = EntityHelper.getIndexOfJoinByLookup(bean, zoomClass);
				for (int i = 0; i < jcAttr.getColumns().size(); i++) {
					String fieldName = jcAttr.getColumns().get(i).getName();
					String getValueName = "get" + StringUtil.capitalize(fieldName);
					Method getValue = zoomClass.getMethod(getValueName);
					Object value = getValue.invoke(objZoomed);
					JComponent comp = panel.getInputPanel().getPanelComponents().get(index+i);
					comp = ComponentResolver.setValue(comp, value, jcAttr.getColumns().get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} 
