/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.property;

import kroki.profil.utils.settings.CalculatedSettings;
import kroki.profil.utils.settings.SettingsPanel;

/**
 * Klasa Calculated označava kalkulisano obeležje,  tj. obeležje čija
 * se vrednost računa primenom zadate formule  nad izabranim obeležjima
 * date ili neke druge klase.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SettingsPanel(CalculatedSettings.class)
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
