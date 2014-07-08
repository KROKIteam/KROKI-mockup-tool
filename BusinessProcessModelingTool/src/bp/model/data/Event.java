package bp.model.data;

import bp.model.graphic.EventComponent;

public abstract class Event extends Vertex {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Event(final String uniqueName) {
        super(uniqueName);
    }

    public Event() { }
    
    public EventComponent getEventComponent() {
        return (EventComponent) getComponent();
    }

}
