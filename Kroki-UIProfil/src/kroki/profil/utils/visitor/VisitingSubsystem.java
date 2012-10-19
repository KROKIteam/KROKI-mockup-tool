/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.visitor;

/**
 * Element UI profila koji može da bude posećen od strane visitora
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface VisitingSubsystem {

    public void accept(AllPosibleZoomPanels visitor);

    public void accept(AllPosibleNextPanels visitor);

    public void accept(AllPosibleHierarchyPanels visitor);

    public void accept(ContainingPanels visitor);
}
