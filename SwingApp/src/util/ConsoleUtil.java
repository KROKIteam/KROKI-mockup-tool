package util;

import com.panelcomposer.model.ejb.EntityBean;

public class ConsoleUtil {
	
	/*public static void printEJB(EJBClass ejb) {
		Logger.log("===============EJB===============");
		Logger.log("Name: " +ejb.getName() + "\t" + 
				"Label: " + ejb.getLabel());
		
		for (int j = 0; j < ejb.getAttributesCount(); j++) {
			Logger.log("-------------------------");
			printEJBAttribute(ejb.getAttributes().get(j));
		}
	}
	
	public static void printEJBAttribute(EJBAttribute attr) {
		Logger.log("Name: " + attr.getName());
		Logger.log("Label: " + attr.getLabel());
		Logger.log("Type: " + attr.getType());
		Logger.log("Upper: " + attr.getUpper() + 
				"\tLower: " + attr.getLower());
		Logger.log("Nullable: " + attr.isNullable());
		Logger.log("Unique: " + attr.isUnique());
		if(attr.getLength() != null)
			Logger.log("Length: " + attr.getLength());
		if(attr.getPrecision() != null && attr.getScale() != null)
			Logger.log("Precision: " + attr.getPrecision() + 
					", Scale: " + attr.getScale());
		if(attr.isKey()) 
			Logger.log("is key");
		if(attr.getMapped() != null) {
			switch(attr.getMapped()) {
				case oneToMany : 
					Logger.log("-- 1 to * --");
					break;
				case manyToOne :
					Logger.log("-- * to 1 --");
					break;
				case oneToOne : 
					Logger.log("-- 1 to 1 --");
					break;
				case manyToMany : 
					Logger.log("-- * to * --");
					break;
			}
		}
	}*/
		

	/*public static void printModel(Model model) {
		
		for (int i = 0; i < model.getEjbs().size(); i++) {
			printEJB(model.getEjbs().get(i));
		}
	}*/

	public static void printEntityBean(EntityBean ejb) {
		// TODO Auto-generated method stub
		
	}
}
