/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.property;

import kroki.profil.persistent.PersistentProperty;

/**
 * Klasa Aggregated označava agregirano obeležje,  tj. obeležje  čija
 * se vrednost računa primenom funkcije za agregaciju nad definisanim
 * skupom vrednosti izabranog obeležja. Izračunata vrednost se koristi samo
 * za prikaz u okviru panela i ne čuva se trajno u bazi.
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
