package bp.event;

import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public interface AttributeChangeListener {

    public void fireAttributeChanged(BPKeyWords keyWord, Object value);

    public Controller getController();
}
