/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.visitor;

import java.util.Iterator;

import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class AllPosibleNexts extends Visitor {

    public AllPosibleNexts() {
    }

    @Override
    public void visit(Object object) {
        UmlProperty property = null;
        if (object instanceof UmlProperty) {
            property = (UmlProperty) object;
        } else {
            System.err.println("AllPosibleNextPanels.java - 31: Object cannot be cast to UmlProperty");
            return;
        }
        UmlClass umlClass = property.umlClass();
        if (umlClass == null) {
            System.err.println("AllPosibleNextPanels.java - 36: UmlProperty's class is null");
            return;
        }
        UmlPackage umlPackage = umlClass.umlPackage();
        if (umlPackage == null) {
            System.err.println("AllPosibleNextPanels.java - 41: UmlClass's package is null");
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
        Iterator<Object> iter = objectList.iterator();
        Next next = (Next) object;
        
        while (iter.hasNext()) {
        	Zoom zoom =  (Zoom) iter.next();
        	//only zooms that don't have opposite and have target panel set
        	if (zoom.getTargetPanel() == null || zoom.opposite() != null || 
        			(next.getTargetPanel() != null && zoom.getActivationPanel() != next.getTargetPanel()) 
        			|| (zoom.getTargetPanel() != next.getActivationPanel()))
        		iter.remove();
	        	
        }
    }
}
