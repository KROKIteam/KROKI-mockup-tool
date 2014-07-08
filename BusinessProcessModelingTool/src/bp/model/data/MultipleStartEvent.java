package bp.model.data;

public class MultipleStartEvent extends StartEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TimerStartEvent timerStartEvent;
    private ConditionalStartEvent conditionalStartEvent;
    private MessageStartEvent messageStartEvent;
    private SignalStartEvent signalStartEvent;
    private ErrorStartEvent errorStartEvent;

    public MultipleStartEvent(String uniqueName) {
        super(uniqueName);
    }

    public MultipleStartEvent() { }
    
    public TimerStartEvent getTimerStartEvent() {
        return timerStartEvent;
    }

    public void setTimerStartEvent(TimerStartEvent timerStartEvent) {
        this.timerStartEvent = timerStartEvent;
    }

    public ConditionalStartEvent getConditionalStartEvent() {
        return conditionalStartEvent;
    }

    public void setConditionalStartEvent(ConditionalStartEvent conditionalStartEvent) {
        this.conditionalStartEvent = conditionalStartEvent;
    }

    public MessageStartEvent getMessageStartEvent() {
        return messageStartEvent;
    }

    public void setMessageStartEvent(MessageStartEvent messageStartEvent) {
        this.messageStartEvent = messageStartEvent;
    }

    public SignalStartEvent getSignalStartEvent() {
        return signalStartEvent;
    }

    public void setSignalStartEvent(SignalStartEvent signalStartEvent) {
        this.signalStartEvent = signalStartEvent;
    }

    public ErrorStartEvent getErrorStartEvent() {
        return errorStartEvent;
    }

    public void setErrorStartEvent(ErrorStartEvent errorStartEvent) {
        this.errorStartEvent = errorStartEvent;
    }

}
