package kroki.app.event;

import java.util.EventObject;

/**
 * An event which describes a model change. This class can be further extended 
 * so that it would carry information regarding what exactly was changed.
 * @author igor
 *
 */
public class ModelUpdateEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public ModelUpdateEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}


}
