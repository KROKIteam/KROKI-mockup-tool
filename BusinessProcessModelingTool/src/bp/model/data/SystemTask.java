package bp.model.data;

import bp.details.SystemTaskDetails;
import bp.model.graphic.TaskComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;

/**
 * Task specialization - to be performed by machine
 * 
 * @author Sholy
 * 
 */
public class SystemTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String implementation;

    public SystemTask(final String uniqueName) {
        super(uniqueName);
    }

    public SystemTask() { }
    
    public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getImplementation() {
        return this.implementation;
    }

    public void updateImplementation(final String implementation, final Controller source) {
        this.implementation = implementation;
        fireAttributeChanged(BPKeyWords.IMPLEMENTATION, this.implementation, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new TaskComponent(this, ImageRes.SYSTEM_TASK);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new SystemTaskDetails(this);
    }
}
