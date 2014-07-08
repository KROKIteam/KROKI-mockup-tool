package bp.model.graphic;

/**
 * BPComponent which has same width and height
 * 
 * 
 * @author Sholy
 * 
 */
public abstract class SquareComponent extends BPComponent {

    // Implementation will take height to be main dimension
	
    @Override
    public final Integer getDefaultWidth() {
        return getDefaultHeight();
    }

    @Override
    public final Integer getWidth() {
        return getHeight();
    }

    @Override
    public Integer getMaximumWidth() {
        return getMaximumHeight();
    }

    @Override
    public Integer getMinimumWidth() {
        return getMinimumHeight();
    }

}
