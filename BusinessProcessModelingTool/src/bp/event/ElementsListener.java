package bp.event;

import bp.model.data.Element;

public interface ElementsListener {

    public void elementAdded(Element e);

    public void elementRemoved(Element e);
}
