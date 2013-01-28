package kroki.app.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.DatabaseConfigGenerator;
import kroki.app.generators.EJBGenerator;
import kroki.app.generators.MenuGenerator;
import kroki.app.generators.PanelGenerator;
import kroki.app.generators.WebResourceGenerator;
import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.OneToManyAttribute;
import kroki.app.generators.utils.Submenu;
import kroki.commons.camelcase.CamelCaser;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

import adapt.application.Application;

import com.panelcomposer.core.MainApp;

public class ExportWebAction extends AbstractAction {

	ArrayList<VisibleElement> elements;
	ArrayList<EJBClass> classes;
	
	public ExportWebAction() {
		putValue(NAME, "Web Application");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		//nadjem selektovani projekat iz workspace-a
		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}
		} catch (NullPointerException e2) {
		}

        elements = new ArrayList<VisibleElement>();
        classes = new ArrayList<EJBClass>();
        
        WebResourceGenerator WebGenerator = new WebResourceGenerator();
        EJBGenerator ejbGenerator = new EJBGenerator();
        DatabaseConfigGenerator dbConfigGen = new DatabaseConfigGenerator(proj.getDBConnectionProps());
        
        //ITERACIJA KROZ ELEMENTE SELEKTOVANOG PROJEKTA
        if(proj != null) {
//        	for(int i=0; i<proj.ownedElementCount(); i++) {
//        		VisibleElement el = proj.getOwnedElementAt(i);
//        		if(el instanceof BussinesSubsystem) {
//        			getSubSystemClasses(el);
//        		}else if(el instanceof VisibleClass) {
//        			elements.add(el);
//        		}
//        	}
        	
        	for(int i=0; i<proj.ownedElementCount(); i++) {
        		VisibleElement el = proj.getOwnedElementAt(i);
        		if(el instanceof BussinesSubsystem) {
        			getSubSystemData(el);
        			getSubSystemClasses(el);
        		}else if(el instanceof VisibleClass) {
        			getClassData(el, "");
        			elements.add(el);
        		}
        	}
        	
            //GENERISANJE DATOTEKA
            WebGenerator.generate(elements);
            ejbGenerator.generateEJBClasses(classes, false);
            dbConfigGen.generatePersistenceXMl(true);
        	
            //pokreni web aplikaciju
            Application adapt = new Application();
            //adapt.main(null);
        }else {
        	//ako nista nije selektovano, prikazem poruku
        	JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
        }
		
	}
	
	
	public void getClassData(VisibleElement el, String classPackage) {
		CamelCaser cc = new CamelCaser();
		
		if(el instanceof StandardPanel) {
			StandardPanel sp = (StandardPanel)el;
			StdPanelSettings sps = sp.getStdPanelSettings();
			VisibleClass vc = (VisibleClass)el;
			
			//LISTE ATRIBUTA ZA EJB KLASU
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			ArrayList<ManyToOneAttribute> mtoAttributes = new ArrayList<ManyToOneAttribute>();
			ArrayList<OneToManyAttribute> otmAttributes = new ArrayList<OneToManyAttribute>();
			
			/*****************************************/
			/*    PODACI ZA GENERISANJE EJB KLASA    */
			/*za svaki panel generise se jedna klasa */
			/*****************************************/
			for(int j=0; j<vc.containedProperties().size(); j++) {
				VisibleProperty vp = vc.containedProperties().get(j);
			
				String type = "java.lang.String";
				if(vp.getComponentType() == ComponentType.TEXT_FIELD) {
					if(vp.getDataType().equals("BigDecimal")) {
						type = "java.math.BigDecimal";
					}else if(vp.getDataType().equals("Date")) {
						type = "java.util.Date";
					}
				}else if(vp.getComponentType() == ComponentType.CHECK_BOX) {
					type =  "java.lang.Boolean";
				} 
				
				
				Attribute attr = new Attribute(cc.toCamelCase(vp.getLabel(), true), vp.getColumnLabel(), vp.getLabel(), type, false, true);
				if(vp.isRepresentative()) {
					attr.setName("name");
				}
				attr.setRepresentative(vp.isRepresentative());
				attributes.add(attr);
			}
			
			for(int l=0; l<vc.containedZooms().size(); l++) {
				Zoom z = vc.containedZooms().get(l);
				StandardPanel zsp = (StandardPanel) z.getTargetPanel();
				EJBClass zcl = getClass(zsp.getPersistentClass().name());
				//dodajem ManyToOne (zoom) atribut
				String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				String reffColumn = "id";
				String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);
				
				ManyToOneAttribute mto = new ManyToOneAttribute(cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true), n, z.getLabel(), type, true);
				mtoAttributes.add(mto);
				
				//u suprotni kraj asocijacije dodam OneToMany atribut
				if(zcl != null) {
					String name = sp.getPersistentClass().name().substring(0, 1).toLowerCase() + sp.getPersistentClass().name().substring(1) + "Set";
					String label = z.getLabel();
					String reffTable = sp.getPersistentClass().name();
					String mappedBy = cc.toCamelCase(z.getLabel(), true);
					
					//pokupim sve representative atribute kako bi bili prikazani u zoom polju
					for(int m=0; m<zcl.getAttributes().size(); m++) {
						Attribute a = zcl.getAttributes().get(m);
						
						if(a.getRepresentative()) {
							System.out.println("atribut" + a.getLabel() + "JE reprezentativan");
							mto.getColumnRefs().add(a);
						}else {
							System.out.println("atribut" + a.getLabel() + "NIJE reprezentativan");
						}
					}
					
					OneToManyAttribute otm = new OneToManyAttribute(name, label, reffTable, mappedBy);
					zcl.getOneToManyAttributes().add(otm);
					
				}else {
					System.out.println("NULL majku mu");
				}
				
			}
			
			//ZA SVAKI PANEL U MODELU GENERISE SE JEDNA EJB KLASA I PROSLEDJUJE GENERATORU
			EJBClass ejb = new EJBClass("adapt.entities", sp.getPersistentClass().name(), sp.getLabel(), attributes, mtoAttributes, otmAttributes);
			classes.add(ejb);
		}
	}
	
	
	//KUPI PODATKE PODSISTEMA IZ MODELA NA OSNOVU KOJIH SE VRSI GENERISANJE
	public void getSubSystemData(VisibleElement el) {
		BussinesSubsystem bs = (BussinesSubsystem) el;
		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				getClassData(e, el.name());
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemData(e);
			}
		}
		
	}
	
	//sve klase prosledjenog paketa stavlja u listu elements
	public void getSubSystemClasses(VisibleElement el) {
		BussinesSubsystem bs = (BussinesSubsystem) el;
		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				elements.add(e);
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemClasses(e);
			}
		}
	}
	
	//na osnovu imena vraca referencu na ejb klasu iz modela
	public EJBClass getClass(String name) {
		EJBClass clas = null;
		for(int i=0; i<classes.size(); i++) {
			EJBClass cl = classes.get(i);
			if(cl.getName().equalsIgnoreCase(name)) {
				clas = cl;
			}
		}
		return clas;
	}

}
