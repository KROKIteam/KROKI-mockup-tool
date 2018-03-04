/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.visitor;

import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Finds a business subsystem's contained panels 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ContainingPanels extends Visitor {

    public ContainingPanels() {
    }

    @Override
    public void visit(Object object) {
        if (object instanceof BussinesSubsystem) {
//            ((BussinesSubsystem) object).accept(this);
        	VisitorHelper.accept((BussinesSubsystem) object, this);
        } else {
            System.out.println("Not subsystem!");
        }
    }
}
