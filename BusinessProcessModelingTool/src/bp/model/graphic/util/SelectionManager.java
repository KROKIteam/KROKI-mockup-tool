package bp.model.graphic.util;

import java.util.HashSet;
import java.util.Set;

import bp.model.data.Element;

public class SelectionManager {

    private final Set<Element> selectedElements = new HashSet<>();
    
    public void addToSelection(Element e) {
        selectedElements.add(e);
    }

    public boolean isElementSelected(Element e) {
        if (e == null)
            return false;

        if (selectedElements.contains(e))
            return true;

        return false;
    }

    public Set<Element> getSelectedElements() {
        return selectedElements;
    }

    public void clearSelection() {
        selectedElements.clear();
    }
}
