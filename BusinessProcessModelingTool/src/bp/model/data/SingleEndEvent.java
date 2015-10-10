package bp.model.data;


public abstract class SingleEndEvent extends EndEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SingleEndEvent(final String uniqueName) {
        super(uniqueName);
    }

    public SingleEndEvent() { }
}
