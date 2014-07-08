package bp.model.data;

import bp.details.StartEventDetails;
import bp.model.graphic.EventComponent;

public class StartEvent extends Event {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StartEvent(final String uniqueName) {
        super(uniqueName);
    }

	public StartEvent() { }
	
    @Override
    public boolean canHaveInput() {
        return false;
    }

    @Override
    public boolean canHaveOutput() {
        return true;
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new StartEventDetails(this);
    }

}
