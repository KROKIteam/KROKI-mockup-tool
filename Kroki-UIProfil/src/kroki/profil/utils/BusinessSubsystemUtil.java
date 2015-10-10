package kroki.profil.utils;

import java.util.ArrayList;
import java.util.List;

import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 * Class contains <code>BusinessSubsystem</code> util methods 
 * @author Kroki Team
 */
public class BusinessSubsystemUtil {


	/**
	 * Finds all association ends contained by the package and its nested packages
	 * @param pack Package
	 * @return All contained association ends
	 */
	public static List<VisibleAssociationEnd> allAssociationEnds(BussinesSubsystem pack){
		ArrayList<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();
		allAssociationEnds(pack, ret);
		return ret;
	}

	/**
	 * Finds all association panels contained by the package and its nested packages
	 * @param pack Package
	 * @return All contained panels
	 */
	public static List<VisibleClass> allPanels(BussinesSubsystem pack){
		ArrayList<VisibleClass> ret = new ArrayList<VisibleClass>();
		allPanels(pack, ret);
		return ret;
	}


	/**
	 * Finds all visible association ends which are contained by the subsystem and its nested packages
	 * @param pack Package
	 * @param ret List containing the results
	 */
	public static void allAssociationEnds(BussinesSubsystem pack, List<VisibleAssociationEnd> ret){
		for (UmlType ownedType : pack.ownedType()){
			if (!(ownedType instanceof VisibleClass))
				continue;
			VisibleClass visibleClass = (VisibleClass)ownedType;
			for (VisibleAssociationEnd end : VisibleClassUtil.containedAssociationEnds(visibleClass))
				ret.add(end);
		}
		for (UmlPackage ownedPackage : pack.nestedPackage())
			allAssociationEnds((BussinesSubsystem) ownedPackage, ret);
	}


	/**
	 * Finds all panels which are contained by the subsystem and its nested packages
	 * @param pack Package
	 * @param ret List containing the results
	 */
	public static void allPanels(BussinesSubsystem pack, List<VisibleClass> ret){
		for (UmlType ownedType : pack.ownedType()){
			if ((ownedType instanceof VisibleClass))
				ret.add((VisibleClass) ownedType);
		}
		for (UmlPackage ownedPackage : pack.nestedPackage())
			allPanels((BussinesSubsystem) ownedPackage, ret);
	}
	
	/**
	 * Finds project containing a package
	 * @param umlPackage Package
	 * @return Project containing the package
	 */
	public static UmlPackage getProject(UmlPackage umlPackage){
		if (umlPackage.nestingPackage() == null)
			return umlPackage;
		return getProject(umlPackage.nestingPackage());
	}
}
