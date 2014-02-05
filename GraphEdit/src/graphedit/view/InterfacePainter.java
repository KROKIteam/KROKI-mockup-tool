package graphedit.view;

import graphedit.model.components.GraphElement;
import graphedit.properties.Preferences;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.geom.Dimension2D;

public class InterfacePainter extends ClassPainter {

	private static final long serialVersionUID = 1L;

	public InterfacePainter(GraphElement element) {
		super(element);
		fillColor1 = Preferences.getInstance().parseColor(Preferences.INTERFACE_COLOR_1); 
		fillColor2 = Preferences.getInstance().parseColor(Preferences.INTERFACE_COLOR_2); 
	}

	@Override
	protected void setGradientPaint() {
		paint = new GradientPaint((float) shape.getBounds().getMinX(),
				(float) shape.getBounds().getMinY(), fillColor1, (float) shape
						.getBounds().getMaxX(), (float) shape.getBounds()
						.getMaxY(), fillColor2);
	}
	
	@Override
	public Dimension2D getMinimumSize(){
		return new Dimension(minWidth, minHeight);
	}
}
