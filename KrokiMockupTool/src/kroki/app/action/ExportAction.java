package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import kroki.app.KrokiMockupToolApp;
import kroki.app.view.Canvas;
import kroki.commons.camelcase.CamelCaser;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;


/**
 * Akcija za eksport projekta u xml fajl
 * @author mrd
 *
 */
public class ExportAction extends AbstractAction {

	public ExportAction() {
		putValue(NAME, "Export...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		CamelCaser cc = new CamelCaser();
		Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();
        //NEW:
        BussinesSubsystem project = (BussinesSubsystem) visibleClass.umlPackage();
        while (true) {
            if (project.nestingPackage() != null) {
                project = (BussinesSubsystem) project.nestingPackage();
            } else {
                break;
            }
        }

        for(int i=0; i<project.ownedElementCount(); i++) {
        	
        	VisibleElement el = project.getOwnedElementAt(i);
        	if(el instanceof BussinesSubsystem) {
        		System.out.println("Eelement " + i + ": " + el.getLabel() + " je podsistem");
        	}else if(el instanceof VisibleClass) {
        		if(el instanceof StandardPanel) {
        			StandardPanel sp = (StandardPanel)el;
        			StdPanelSettings sps = sp.getStdPanelSettings();
        			
        			
        			VisibleClass vc = (VisibleClass)el;
        			
        			String settingsString = "Settings:\n";
        			String ejbString = "Atributi: \n";
        			String opsString = "Operacije: \n";
        			
        			//-----------------PANEL SETTINGS------------------------------
        			if(sp.isAdd()) {
        				settingsString += "	ADD : TRUE\n";
        			}else {
        				settingsString += "	ADD : FALSE\n";
        			}
        			if(sp.isDelete()) {
        				settingsString += "	DELETE : TRUE\n";
        			}else {
        				settingsString += "	DELETE : FALSE\n";
        			}
        			if(sps.getDefaultViewMode() == ViewMode.INPUT_PANEL_MODE) {
        				settingsString += "	VIEW MODE : PANEL\n";
        			}else if(sps.getDefaultViewMode() == ViewMode.TABLE_VIEW) {
        				settingsString += "	VIEW MODE : TABLE\n";
        			}
        			if(sp.isChangeMode()) {
        				settingsString += "	CHANGE MODE : TRUE\n";
        			}else {
        				settingsString += "	CHANGE MODE : FALSE\n";
        			}
        			if(sp.isDataNavigation()) {
        				settingsString += "	DATA NAVIGATION : TRUE\n";
        			}else {
        				settingsString += "	DATA NAVIGATION : FALSE\n";
        			}
        			
        			
        			//-------------------------ATRIBUTI KLASE-----------------------------------------------------------------------------
        			for(int j=0; j<vc.containedProperties().size(); j++) {
        				VisibleProperty vp = vc.containedProperties().get(j);
        			
        				ejbString += "	" + j + " " + cc.toCamelCase(vp.getLabel(), true) + "(" + vp.getColumnLabel()  +") : " + vp.getComponentType() + "\n";
        			}
        			
        			for(int l=0; l<vc.containedZooms().size(); l++) {
        				Zoom z = vc.containedZooms().get(l);
        				ejbString += "	" + l + " " + cc.toCamelCase(z.getLabel(), true) + " : " + z.getTargetPanel().getLabel() + "\n";
        			}
        			
        			//--------------------------OPERACIJE------------------------------------------------
        			for(int k=0; k<vc.containedOperations().size(); k++) {
        				VisibleOperation vo = vc.containedOperations().get(k);
        				if(vo instanceof BussinessOperation) {
        					if(vo instanceof Report) {
        						Report r = (Report)vo;
        						
        						opsString += "	" + k + " " + cc.toCamelCase(vo.getLabel(), true) + " : REPORT\n";
        					}else if (vo instanceof Transaction) {
        						opsString += "	" + k + " " + cc.toCamelCase(vo.getLabel(), true) + " : TRANSACTION\n";
        					}
        				}
        			}
        			
        			System.out.println("Panel " + sp.getLabel().toUpperCase() + "\nKlasa: " + sp.getPersistentClass().name() + "\n" + ejbString + settingsString + opsString);
        		}
        		
        	}
        }
        
	}

}
