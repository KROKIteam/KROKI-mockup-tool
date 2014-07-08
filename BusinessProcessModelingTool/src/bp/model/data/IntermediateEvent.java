package bp.model.data;

import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public abstract class IntermediateEvent extends Event {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer minInput;

    public IntermediateEvent(final String uniqueName) {
        super(uniqueName);
    }
    
    public IntermediateEvent() { }

    @Override
    public boolean canHaveInput() {
        return true;
    }

    @Override
    public boolean canHaveOutput() {
        return true;
    }

    public Integer getMinInput() {
        return this.minInput;
    }
    
    public void setMinInput(Integer minInput) {
		this.minInput = minInput;
	}

    public void updateMinInput(final Integer minInput, final Controller source) {
        this.minInput = minInput;
        fireAttributeChanged(BPKeyWords.MIN_INPUT, this.minInput, source);
    }

}
