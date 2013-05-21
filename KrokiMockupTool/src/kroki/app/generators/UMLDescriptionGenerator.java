package kroki.app.generators;

import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

public class UMLDescriptionGenerator {
	
	/**
	 * Generates UML description for PlantUML
	 * @param project
	 * @return
	 */
	public String generateDescription(BussinesSubsystem project) {
		NamingUtil namer = new NamingUtil();
		String desc = "@startuml";
		
		for(int i=0; i<project.ownedElementCount(); i++) {
			VisibleElement el = project.getOwnedElementAt(i);
			if(el instanceof BussinesSubsystem) {
				desc += "\npackage " + namer.toCamelCase(el.getLabel(), true) + " <<Frame>> {\n}";
			}else if(el instanceof VisibleClass) {
				getClassData(el, "", desc);
			}
		}
		return desc + "\n@enduml";
	}
	
	public void getClassData(VisibleElement el, String classPackage, String desc) {
	}

	public void getSubSystemData(VisibleElement el, int index, String desc) {
	}
	
}
