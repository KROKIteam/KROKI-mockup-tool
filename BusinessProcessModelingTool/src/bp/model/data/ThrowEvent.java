package bp.model.data;

public abstract class ThrowEvent extends IntermediateEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long maxTriggers;

    public ThrowEvent(String uniqueName) {
        super(uniqueName);
    }
    
    public ThrowEvent() { }

    public Long getMaxTriggers() {
        return maxTriggers;
    }

    public void setMaxTriggers(Long maxTriggers) {
        this.maxTriggers = maxTriggers;
    }

}
