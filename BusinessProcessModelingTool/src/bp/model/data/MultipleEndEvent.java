package bp.model.data;

import java.util.ArrayList;
import java.util.List;

public class MultipleEndEvent extends EndEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<MessageEndEvent> messageEndEvents = new ArrayList<MessageEndEvent>();
    private List<ErrorEndEvent> errorEndEvents = new ArrayList<ErrorEndEvent>();
    private List<SignalEndEvent> signalEndEvents = new ArrayList<SignalEndEvent>();

    public MultipleEndEvent(String uniqueName) {
        super(uniqueName);
    }
    
    public MultipleEndEvent() { }

    public void setMessageEndEvents(List<MessageEndEvent> messageEndEvents) {
		this.messageEndEvents = messageEndEvents;
	}

	public void setErrorEndEvents(List<ErrorEndEvent> errorEndEvents) {
		this.errorEndEvents = errorEndEvents;
	}

	public void setSignalEndEvents(List<SignalEndEvent> signalEndEvents) {
		this.signalEndEvents = signalEndEvents;
	}

	public List<MessageEndEvent> getMessageEndEvents() {
        return messageEndEvents;
    }

    public List<ErrorEndEvent> getErrorEndEvents() {
        return errorEndEvents;
    }

    public List<SignalEndEvent> getSignalEndEvents() {
        return signalEndEvents;
    }

}
