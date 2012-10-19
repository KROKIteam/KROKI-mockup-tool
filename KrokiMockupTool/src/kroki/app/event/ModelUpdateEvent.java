package kroki.app.event;

import java.util.EventObject;

/**
 * Dogadjaj opisuje promenu nad modelom. Ovu klasu je moguce prosiriti tako
 * da nosi informacije o tome sta je tacno u modelu promenjeno u cilju optimizacije
 * iscrtavanje.
 * @author igor
 *
 */
@SuppressWarnings("serial")
public class ModelUpdateEvent extends EventObject {

	public ModelUpdateEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}


}
