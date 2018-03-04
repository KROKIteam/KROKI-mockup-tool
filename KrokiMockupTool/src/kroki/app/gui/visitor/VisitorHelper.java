package kroki.app.gui.visitor;

import kroki.profil.panel.StandardPanel;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlType;

/*
 * 	This class implements functionalities that were in BussinesSubsystemAcceptAspect
 */
public class VisitorHelper {

	public static void accept(BussinesSubsystem subsystem, AllPosibleZoomPanels visitor) {
		visitor.addAllObjects(subsystem.ownedType());
		for(int i=0; i<subsystem.nestedPackage().size(); i++) {
			BussinesSubsystem subs = (BussinesSubsystem) subsystem.nestedPackage().get(i);
			accept(subs, visitor);
		}
	}
	
	public static void accept(BussinesSubsystem subsystem,  AllPosibleNextPanels visitor) {
		 visitor.addAllObjects(subsystem.ownedType());
		 for (int i = 0; i < subsystem.nestedPackage().size(); i++) {
			 BussinesSubsystem subs = (BussinesSubsystem)  subsystem.nestedPackage().get(i);
			 accept(subs, visitor);
		 }
	 }
	
	public static void accept(BussinesSubsystem subsystem,  AllPosibleNexts visitor) {
		for (UmlType owned : subsystem.ownedType()) {
			if(owned instanceof StandardPanel) {
				visitor.addAllObjects(VisibleClassUtil.containedZooms((StandardPanel)owned));
			}
		}
		for(int i=0; i<subsystem.nestedPackage().size(); i++) {
			BussinesSubsystem subs = (BussinesSubsystem) subsystem.nestedPackage().get(i);
			accept(subs, visitor);
		}
	}
	
	public static void accept(BussinesSubsystem subsystem,  AllPosibleHierarchyPanels visitor) {
		visitor.addAllObjects(subsystem.ownedType());
		for (int i = 0; i < subsystem.nestedPackage().size(); i++) {
			 BussinesSubsystem subs = (BussinesSubsystem)  subsystem.nestedPackage().get(i);
			 accept(subs, visitor);
		 }
	}
	
	public static void accept(BussinesSubsystem subsystem,  ContainingPanels visitor) {
		visitor.addAllObjects(subsystem.ownedType());
		for (int i = 0; i < subsystem.nestedPackage().size(); i++) {
			 BussinesSubsystem subs = (BussinesSubsystem)  subsystem.nestedPackage().get(i);
			 accept(subs, visitor);
		 }
	}
}