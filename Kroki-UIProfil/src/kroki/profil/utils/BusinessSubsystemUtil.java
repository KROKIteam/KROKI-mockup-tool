package kroki.profil.utils;

import java.util.ArrayList;
import java.util.List;

import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

public class BusinessSubsystemUtil {


	public static List<VisibleAssociationEnd> allAssociationEnds(BussinesSubsystem pack){
		ArrayList<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();
		allAssociationEnds(pack, ret);
		return ret;
	}


	public static List<VisibleClass> allPanels(BussinesSubsystem pack){
		ArrayList<VisibleClass> ret = new ArrayList<VisibleClass>();
		allPanels(pack, ret);
		return ret;
	}


	/**
	 * Finds all visible association ends which are contained by the subsystem and its nested packages
	 * @param pack
	 * @param ret
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

	public static void allPanels(BussinesSubsystem pack, List<VisibleClass> ret){
		for (UmlType ownedType : pack.ownedType()){
			if ((ownedType instanceof VisibleClass))
				ret.add((VisibleClass) ownedType);
		}
		for (UmlPackage ownedPackage : pack.nestedPackage())
			allPanels((BussinesSubsystem) ownedPackage, ret);
	}
}
