package kroki.app.generators;

import gui.menudesigner.model.Submenu;

import java.io.File;
import java.util.ArrayList;

import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.Menu;
import kroki.profil.VisibleElement;
import kroki.profil.utils.DatabaseProps;

/**
 * Generates contents of application repository using other generator classes
 * @author Milorad Filipovic
 */
public class ApplicationRepositoryGenerator {

	EJBGenerator EJBGenerator;
	DatabaseConfigGenerator DBConfigGenerator;
	PanelGenerator panelGenerator;
	MenuGenerator menuGenerator;
	EnumerationGenerator enumGenerator;
	
	public ApplicationRepositoryGenerator() {
		EJBGenerator = new EJBGenerator();
		panelGenerator = new PanelGenerator();
		menuGenerator = new MenuGenerator();
		enumGenerator = new EnumerationGenerator(false);
		DBConfigGenerator = new DatabaseConfigGenerator(new DatabaseProps());
	}
	
	public void generate(ArrayList<EJBClass> classes, ArrayList<Menu> menus, ArrayList<VisibleElement> elements, ArrayList<Enumeration> enumerations, Submenu rootMenu) {
		DBConfigGenerator.geneateHibernateConfigXML("ApplicationRepository" + File.separator + "generated" + File.separator +  "db_config" + File.separator + "hibernate.cfg.xml");
		DBConfigGenerator.generatePersistenceXMl(true);
		EJBGenerator.generateEJBClasses(classes, false);
		EJBGenerator.generateEJBXmlFiles(classes, "ApplicationRepository" + File.separator + "generated" + File.separator +  "model" + File.separator + "ejb");
		EJBGenerator.generateXMLMappingFile(classes, "REPO");
		menuGenerator.generateMenu(menus);
		menuGenerator.generateNewMenu(rootMenu);
		panelGenerator.generate(elements, "REPO");
		enumGenerator.generateXMLFiles(enumerations);
	}
	
}
