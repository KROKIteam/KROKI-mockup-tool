package bp.model.data;

import bp.model.graphic.ActivityEventComponent;
import bp.model.util.Controller;

public abstract class ActivityEvent extends Vertex {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Activity activity;

    public ActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public ActivityEvent() { }
    
    public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity() {
        return this.activity;
    }

    public void updateActivity(final Activity activity, final Controller source) {
        this.activity = activity;
        if (this.activity != null) {
            this.activity.addActivityEvent(this);
        }
    }

    @Override
    public boolean canHaveInput() {
        return false;
    }

    @Override
    public boolean canHaveOutput() {
        return true;
    }

    public ActivityEventComponent getEventComponent() {
        return (ActivityEventComponent) getComponent();
    }

}
