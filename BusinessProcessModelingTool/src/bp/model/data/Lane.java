package bp.model.data;

import java.util.ArrayList;
import java.util.List;

import bp.details.LaneDetails;
import bp.model.graphic.LaneComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

public class Lane extends Element {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String actor;
    private Lane parent;
    private List<Lane> children = new ArrayList<Lane>();
    private List<Task> tasks = new ArrayList<Task>();

    public Lane(final String uniqueName) {
        super(uniqueName);
    }

    public Lane() {
	}

	public void setChildren(List<Lane> children) {
		this.children = children;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public void setParent(Lane parent) {
		this.parent = parent;
	}

	@Override
    protected void initializeComponent() {
        this.component = new LaneComponent(this);
        this.component.setzIndex(1);
    }

    @Override
    protected void initializeDetails() {
        this.details = new LaneDetails(this);
    }

    public Lane getParent() {
        return this.parent;
    }

    public void updateParent(final Lane parent, final Controller source) {
        this.parent = parent;
        fireAttributeChanged(BPKeyWords.PARENT, this.parent, source);
    }

    public List<Lane> getChildren() {
        return this.children;
    }

    public String getActor() {
        return this.actor;
    }

    public void updateActor(final String actor, final Controller source) {
        this.actor = actor;
        fireAttributeChanged(BPKeyWords.ACTOR, this.actor, source);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public LaneComponent getLaneComponent() {
        return (LaneComponent) getComponent();
    }

}
