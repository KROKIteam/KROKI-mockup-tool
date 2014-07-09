package bp.event;

import bp.model.data.Element;

public abstract class ElementsListener {

	public ElementsListener() { }
	
    public abstract void elementAdded(Element e);

    public abstract void elementRemoved(Element e);
}
