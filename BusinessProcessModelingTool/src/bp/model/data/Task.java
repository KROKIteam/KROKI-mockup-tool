package bp.model.data;

import bp.details.TaskDetails;
import bp.model.graphic.TaskComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

/**
 * Task represents atomic unit of work
 * 
 * @author Sholy
 * 
 */
public class Task extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String actor;
    private Lane laneActor;

    private Boolean autoAssign;
    private Integer multipleExecution;
    private ExecutionType multipleExecutionType;


    public Task(final String uniqueName) {
        super(uniqueName);
    }
    
    public Task() { }

    public void setActor(String actor) {
		this.actor = actor;
	}

	public void setLaneActor(Lane laneActor) {
		this.laneActor = laneActor;
	}

	public void setAutoAssign(Boolean autoAssign) {
		this.autoAssign = autoAssign;
	}

	public void setMultipleExecution(Integer multipleExecution) {
		this.multipleExecution = multipleExecution;
	}

	public void setMultipleExecutionType(ExecutionType multipleExecutionType) {
		this.multipleExecutionType = multipleExecutionType;
	}

	public String getActor() {
        return this.actor;
    }

    public void updateActor(final String actor, final Controller source) {
        this.actor = actor;
        fireAttributeChanged(BPKeyWords.ACTOR, this.actor, source);
    }

    public Boolean getAutoAssign() {
        return this.autoAssign;
    }

    public void updateAutoAssign(final Boolean autoAssign, final Controller source) {
        this.autoAssign = autoAssign;
        fireAttributeChanged(BPKeyWords.AUTO_ASSIGN, this.autoAssign, source);
    }

    public Integer getMultipleExecution() {
        return this.multipleExecution;
    }

    public void updateMultipleExecution(final Integer multipleExecution, final Controller source) {
        this.multipleExecution = multipleExecution;
        fireAttributeChanged(BPKeyWords.MULTIPLE_EXECUTION, this.multipleExecution, source);
    }

    public ExecutionType getMultipleExecutionType() {
        return this.multipleExecutionType;
    }

    public void updateMultipleExecutionType(final ExecutionType multipleExecutionType, final Controller source) {
        this.multipleExecutionType = multipleExecutionType;
        fireAttributeChanged(BPKeyWords.MULTIPLE_EXECUTION_TYPE, this.multipleExecutionType, source);
    }

    public Lane getLaneActor() {
        return this.laneActor;
    }

    public void updateLaneActor(final Lane laneActor, final Controller source) {
        this.laneActor = laneActor;
        fireAttributeChanged(BPKeyWords.LANE_ACTOR, this.laneActor, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new TaskComponent(this);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new TaskDetails(this);
    }

    public TaskComponent getTaskComponent() {
        return (TaskComponent) getComponent();
    }

}
