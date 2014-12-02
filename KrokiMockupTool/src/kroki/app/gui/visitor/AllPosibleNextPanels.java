/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.visitor;

import java.util.Iterator;
import java.util.List;

import kroki.api.panel.VisibleClassUtil;
import kroki.profil.association.Zoom;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AllPosibleNextPanels extends Visitor {

    public AllPosibleNextPanels() {
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
        Iterator iter = objectList.iterator();
        while (iter.hasNext()) {

            VisibleClass visibleClass = (VisibleClass) iter.next();
            List<Zoom> zooms = VisibleClassUtil.containedZooms(visibleClass);
            boolean flag = false;
            for (int i = 0; i < zooms.size(); i++) {
                if (zooms.get(i).getTargetPanel() == umlClass) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                iter.remove();
            }
        }
    }
}