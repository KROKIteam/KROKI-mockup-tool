package bp.model.data;

import java.util.ArrayList;
import java.util.List;

import bp.app.AppCore;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public abstract class Activity extends Vertex {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String data;
    private String loopExpression;
    private Integer minInput;

    private final List<ActivityEvent> activityEvents = new ArrayList<ActivityEvent>();

    public Activity(final String uniqueName) {
        super(uniqueName);
    }

    public Activity() {
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setLoopExpression(String loopExpression) {
		this.loopExpression = loopExpression;
	}

	public void setMinInput(Integer minInput) {
		this.minInput = minInput;
	}

	public String getData() {
        return this.data;
    }

    public void updateData(final String data, final Controller source) {
        this.data = data;
        fireAttributeChanged(BPKeyWords.DATA, this.data, source);
    }

    public String getLoopExpression() {
        return this.loopExpression;
    }

    public void updateLoopExpression(final String loopExpression, final Controller source) {
        this.loopExpression = loopExpression;
        fireAttributeChanged(BPKeyWords.LOOP_EXPRESSION, this.loopExpression, source);
    }

    public Integer getMinInput() {
        return this.minInput;
    }

    public void updateMinInput(final Integer minInput, final Controller source) {
        this.minInput = minInput;
        fireAttributeChanged(BPKeyWords.MIN_INPUT, this.minInput, source);
    }

    public List<ActivityEvent> getActivityEvents() {
        return this.activityEvents;
    }

    public void addActivityEvent(final ActivityEvent event) {
        this.activityEvents.add(event);
    }

    public void removeActivityEvent(final ActivityEvent event) {
        this.activityEvents.remove(event);
    }

    @Override
    public boolean canHaveInput() {
        return true;
    }

    @Override
    public boolean canHaveOutput() {
        return true;
    }

    protected void addElementsListener() {
        AppCore.getInstance().getBpPanel().getProcess().addElementsListener(new ElementsListener());
    }
    
    public class ElementsListener extends bp.event.ElementsListener {
    	
    	public ElementsListener() {
    		
    	}
    	
    	@Override
        public void elementRemoved(final Element e) {
            if (e instanceof ActivityEvent) {
                final ActivityEvent ae = (ActivityEvent) e;
                if (ae.getActivity().equals(this)) {
                    removeActivityEvent(ae);
                }
            }
        }

        @Override
        public void elementAdded(final Element e) {
            if (e instanceof ActivityEvent) {
                final ActivityEvent activityEvent = (ActivityEvent) e;
                if (activityEvent.getActivity().equals(this)) {
                    addActivityEvent(activityEvent);
                }
            }

        }
    }
}
