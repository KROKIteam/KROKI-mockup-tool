package kroki.profil.property;

/**
 * Class <code>Calculated</code> represemmts a calculated property, whose 
 * value is calculated by applying a specified function to chosen properties
 * of the current or some other class
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */

public class Calculated extends VisibleProperty {
	
	private static final long serialVersionUID = 1L;

    protected String expression;

    public Calculated(VisibleProperty visibleProperty) {
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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
