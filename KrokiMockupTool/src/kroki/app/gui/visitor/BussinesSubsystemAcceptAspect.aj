package kroki.app.gui.visitor;

import kroki.profil.panel.StandardPanel;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlType;

public privileged aspect BussinesSubsystemAcceptAspect {

	
	/****************************************************/
	 /*IMPLEMENTIRANE METODE INTERFEJSA VisitingSubsystem*/
	 /****************************************************/
	 public void BussinesSubsystem.accept(AllPosibleZoomPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void BussinesSubsystem.accept(AllPosibleNextPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void BussinesSubsystem.accept(AllPosibleNexts visitor) {
		 for (UmlType owned : ownedType)
			 if (owned instanceof StandardPanel)
				 visitor.addAllObjects(VisibleClassUtil.containedZooms(((StandardPanel)owned)));

		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void BussinesSubsystem.accept(AllPosibleHierarchyPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void BussinesSubsystem.accept(ContainingPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }
}
