package kroki.profil.property;

import kroki.profil.persistent.PersistentProperty;

/**
 * Class <Aggregated> represents an aggregated property i.e. property whose
 * value is calculated by applying the aggregation function to a defined
 * set of values of the chosen property. The calculated value is only shown
 * inside the panel and is not permanently saved.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Aggregated extends VisibleProperty {
	
	private static final long serialVersionUID = 1L;

    protected AggregateFunciton funciton = AggregateFunciton.SUM;
    protected String selection;
    protected PersistentProperty aggregatingAttribute;

    public Aggregated(VisibleProperty visibleProperty) {
        this.columnLabel = visibleProperty.columnLabel;
        this.displayFormat = visibleProperty.displayFormat;
        this.representative = visibleProperty.representative;
        this.autoGo = visibleProperty.autoGo;
        this.disabled = visibleProperty.disabled;
        this.defaultValue = visibleProperty.defaultValue;
        this.label = visibleProperty.getLabel();
        this.visible = visibleProperty.isVisible();
        this.componentType = visibleProperty.getComponentType();
        this.component = visibleProperty.getComponent();
        this.component.setParent(null);

        this.setDisabled(true);
    }

    public AggregateFunciton getFunciton() {
        return funciton;
    }

    public void setFunciton(AggregateFunciton funciton) {
        this.funciton = funciton;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public PersistentProperty getAggregatingAttribute() {
        return aggregatingAttribute;
    }

    public void setAggregatingAttribute(PersistentProperty aggregatingAttribute) {
        this.aggregatingAttribute = aggregatingAttribute;
    }
}
