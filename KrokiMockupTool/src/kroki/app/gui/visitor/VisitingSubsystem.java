package kroki.app.gui.visitor;

/**
 * Element of the UI profile which can be visited
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface VisitingSubsystem {

    public void accept(AllPosibleZoomPanels visitor);

    public void accept(AllPosibleNexts visitor);

    public void accept(AllPosibleHierarchyPanels visitor);

    public void accept(ContainingPanels visitor);
}
