package bp.model.data;

import bp.details.GatewayDetails;
import bp.model.graphic.GatewayComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;


public class Gateway extends Vertex {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer minInput;

    public Gateway(final String uniqueName) {
        super(uniqueName);
    }

    public Gateway() {
	}

	public void setMinInput(Integer minInput) {
		this.minInput = minInput;
	}

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

    public void updateMinInput(final Integer minInput, final Controller source) {
        this.minInput = minInput;
        fireAttributeChanged(BPKeyWords.MIN_INPUT, this.minInput, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new GatewayComponent(this);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new GatewayDetails(this);
    }

    public GatewayComponent getGatewayComponent() {
        return (GatewayComponent) getComponent();
    }
}
