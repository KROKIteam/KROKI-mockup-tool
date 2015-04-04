package kroki.app.event;

import java.util.EventObject;

/**
 * Event which represents selection changes.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SelectionModelUpdateEvent extends EventObject {
    
	private static final long serialVersionUID = 1L;

	public SelectionModelUpdateEvent(Object source) {
        super(source);
    }
}
