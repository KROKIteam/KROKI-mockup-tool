package bp.event;

import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public abstract class AttributeChangeListener {

	public AttributeChangeListener() { }
	
    public abstract void fireAttributeChanged(BPKeyWords keyWord, Object value);

    public abstract Controller getController();
}
