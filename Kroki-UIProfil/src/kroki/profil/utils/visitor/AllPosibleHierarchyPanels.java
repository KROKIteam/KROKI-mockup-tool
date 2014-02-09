/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.visitor;

import java.util.List;

import kroki.profil.association.Hierarchy;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AllPosibleHierarchyPanels extends Visitor {

	@Override
	public void visit(Object object) {
		UmlProperty property = null;
		if (object instanceof UmlProperty) {
			property = (UmlProperty) object;
		} else {
			System.err.println("AllPosibleHierarchyPanels.java - 28: Object cannot be cast to UmlProperty");
			return;
		}
		UmlClass umlClass = property.umlClass();
		if (umlClass == null) {
			System.err.println("AllPosibleHierarchyPanels.java - 33: UmlProperty's class is null");
			return;
		}
		UmlPackage umlPackage = umlClass.umlPackage();
		if (umlPackage == null) {
			System.err.println("AllPosibleHierarchyPanels.java - 38: UmlClass's package is null");
			return;
		}
		while (true) {
			if (umlPackage.nestingPackage() == null) {
				break;
			} else {
				umlPackage = umlPackage.nestingPackage();
			}
		}
		((BussinesSubsystem) umlPackage).accept(this);

		ParentChild parentChild = (ParentChild) umlClass;
		Hierarchy hierarchy = (Hierarchy)object;
		if (hierarchy.getLevel() > 1 || hierarchy.getLevel() == -1){
			objectList.clear();
			objectList.addAll(parentChild.getAllPosibleTargetPanels());
		}
		else
			objectList.remove(parentChild);
	}

	public void add(VisibleClass object) {
		this.objectList.add(object);
	}

	public void addAll(List<VisibleClass> objectList) {
		this.objectList.addAll(objectList);
	}
}
