package graphedit.view;

import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.Preferences;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JPanel;

abstract class GraphEditViewPanel extends JPanel{


	protected static final double translationFactor = 100;
	protected static final double scalingFactor = 1.2;
	protected static final double GRID_WIDTH = 250;
	protected AffineTransform transformation = new AffineTransform();
	protected Preferences preferences = Preferences.getInstance();
	protected static final long serialVersionUID = 1L;
	protected int metrics;
	protected boolean rulerActive;
	protected boolean gridActive;

	protected GraphEditModel model;	

	protected abstract void paintOurView(Graphics g, boolean includeTransform);

	public void paintToMyGraphics(Graphics g){
		if(g==null) return;
		//Graphics2D g2 = (Graphics2D)g;
		paintOurView(g, false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		paintOurView(g, true);
	}

	protected void transformFromUserSpace(Point2D userSpace) {
		transformation.transform(userSpace, userSpace);
	}

	protected void transformToUserSpace(Point2D deviceSpace) {
		try {
			transformation.inverseTransform(deviceSpace, deviceSpace);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	protected double limitScaleFactor(double scale) {
		final double scaleMax = Double.parseDouble(preferences.getProperty(Preferences.MAX_ZOOM)); 
		final double scaleMin = Double.parseDouble(preferences.getProperty(Preferences.MIN_ZOOM)); 

		if (scale > scaleMax) {
			return scaleMax;
		}
		return (scale < scaleMin) ? scaleMin : scale;
	}

	protected Point2D getCenterPoint() {
		return new Point2D.Double(getWidth() / 2, getHeight() / 2);
	}

	public double[] getModelBounds(){
		return getModelBounds(model.getAllElements());
	}

	public void bestFitZoom(boolean limitScaleFactor) {
		double bestFitFactor = Double.parseDouble(Preferences.getInstance().getProperty(Preferences.BESTFIT_FACTOR));
		double[] modelBounds = getModelBounds();
		if (modelBounds == null) {
			return;
		}
		double xMin = modelBounds[0], xMax = modelBounds[1];
		double yMin = modelBounds[2], yMax = modelBounds[3];

		Point2D center = new Point2D.Double((xMax + xMin) / 2, (yMax + yMin) / 2);
		double scale;
		if (getWidth() / (xMax - xMin) < getHeight() / (yMax - yMin)) {
			scale = getWidth() / (xMax - xMin);
		} else {
			scale = getHeight() / (yMax - yMin);
		}

		transformFromUserSpace(center);
		zoomToPoint(scale * bestFitFactor, center, limitScaleFactor);
	}

	protected void zoomToPoint(double scale, Point2D position, boolean limitScaleFactor) {
		if (limitScaleFactor)
			scale = limitScaleFactor(scale);

		transformToUserSpace(position);

		transformation.setToScale(scale, scale);

		Point2D center = getCenterPoint();
		transformToUserSpace(center);

		transformation.translate(center.getX() - position.getX(),
				center.getY() - position.getY());

		repaint();
	}

	protected double[] getModelBounds(List<GraphElement> elements) {
		if (elements == null || elements.isEmpty()) {
			return null;
		}
		double[] retVal = new double[4];
		double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE;
		double yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;
		double x1, y1, x2, y2;
		Point2D position;
		Dimension2D size;

		for (GraphElement element : elements) {
			position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
			if (element instanceof Link){
				double[] linkBounds = ((Link)element).getLinkBounds();
				x1 = linkBounds[0];
				x2 = linkBounds[1];
				y1 = linkBounds[2];
				y2 = linkBounds[3];
			}
			else{
				size = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
				x1 = position.getX() - size.getWidth() / 2;
				y1 = position.getY() - size.getHeight() / 2;
				x2 = position.getX() + size.getWidth() / 2;
				y2 = position.getY() + size.getHeight() / 2;
			}
			if (x1 < xMin) {
				xMin = x1;
			}
			if (y1 < yMin) {
				yMin = y1;
			}
			if (x2 > xMax) {
				xMax = x2;
			}
			if (y2 > yMax) {
				yMax = y2;
			}
		}
		retVal[0] = xMin;
		retVal[1] = xMax;
		retVal[2] = yMin;
		retVal[3] = yMax;
		return retVal;
	}

}
