package util;

import com.panelcomposer.model.ejb.EntityBean;

public class ConsoleUtil {
	
	/*public static void printEJB(EJBClass ejb) {
		System.out.println("===============EJB===============");
		System.out.println("Name: " +ejb.getName() + "\t" + 
				"Label: " + ejb.getLabel());
		
		for (int j = 0; j < ejb.getAttributesCount(); j++) {
			System.out.println("-------------------------");
			printEJBAttribute(ejb.getAttributes().get(j));
		}
	}
	
	public static void printEJBAttribute(EJBAttribute attr) {
		System.out.println("Name: " + attr.getName());
		System.out.println("Label: " + attr.getLabel());
		System.out.println("Type: " + attr.getType());
		System.out.println("Upper: " + attr.getUpper() + 
				"\tLower: " + attr.getLower());
		System.out.println("Nullable: " + attr.isNullable());
		System.out.println("Unique: " + attr.isUnique());
		if(attr.getLength() != null)
			System.out.println("Length: " + attr.getLength());
		if(attr.getPrecision() != null && attr.getScale() != null)
			System.out.println("Precision: " + attr.getPrecision() + 
					", Scale: " + attr.getScale());
		if(attr.isKey()) 
			System.out.println("is key");
		if(attr.getMapped() != null) {
			switch(attr.getMapped()) {
				case oneToMany : 
					System.out.println("-- 1 to * --");
					break;
				case manyToOne :
					System.out.println("-- * to 1 --");
					break;
				case oneToOne : 
					System.out.println("-- 1 to 1 --");
					break;
				case manyToMany : 
					System.out.println("-- * to * --");
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
