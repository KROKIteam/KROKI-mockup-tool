package graphedit.view;

import graphedit.model.components.GraphElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.Preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PackagePainter extends ElementPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int rectHeight = 65;
	protected int rectWidth = 90;
	protected int addHeight = 15;
	protected int addWidth = 30;
	protected Font font;
	protected transient GradientPaint paint;
	protected GeneralPath path;
	protected double percentH = 0.23;
	protected double percentW = 0.33;
	protected int minWidth, minHeight, minSpaceWidth = 10, minSpaceHeight = 10, optSpaceWidth = 20, optSpaceHeight = 10,
	optimalWidth, optimalHeight;
	protected boolean updateMeasures = true;
	int lowerRectHeight, width, totalHeight, upperRetHeight, upperRectWidth;


	public PackagePainter(GraphElement element){
		super(element);
		font = new Font("Sans-serif", Font.PLAIN, 12);	
		fillColor1 = Preferences.getInstance().parseColor(Preferences.PACKAGE_COLOR_1); 
		fillColor2 = Preferences.getInstance().parseColor(Preferences.PACKAGE_COLOR_2);
	}

	@Override
	public boolean elementAt(Point2D p) {
		return path.contains(p);
	}

	/**
	 * Called only once, from constructor
	 * Sets shape, path and initializes size
	 */
	@Override
	public void setShape() {
		path = new GeneralPath();
		//element.setProperty(GraphElementProperties.SIZE, new Dimension( rectWidth ,rectHeight + addHeight));

		int totalHeight = rectHeight + addHeight;
		Shape lowerRectangle = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() - rectWidth/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2 + addHeight, 
				rectWidth, rectHeight);

		Shape upperRectangle = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() - rectWidth/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2, 
				addWidth, addHeight);
		path.append(lowerRectangle, true);
		path.append(upperRectangle, true);

		//shape se postavlja da se oko njega iscrta indikator selekcije

		shape = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() - rectWidth/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2, 
				rectWidth, totalHeight);
	}

	@Override
	public void formShape() {
		path.reset();
		Dimension size = (Dimension) element.getProperty(GraphElementProperties.SIZE);
		totalHeight = (int) size.getHeight();
		width = (int) size.getWidth();
	//	if (width < optimalWidth)
		//	width = optimalWidth;
		//size.setSize(optimalWidth, totalHeight);
		
		lowerRectHeight = (int) Math.round(totalHeight/(1+percentH));
		upperRetHeight = totalHeight - lowerRectHeight;
		upperRectWidth = (int) Math.round(width*percentW);
		
		Shape lowerRectangle = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() - width/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2 + upperRetHeight, 
				 width, lowerRectHeight);

		Shape upperRectangle = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() -width/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2, 
				upperRectWidth, upperRetHeight);
		path.append(lowerRectangle, true);
		path.append(upperRectangle, true);
		
		//shape se postavlja da se oko njega iscrta indikator selekcije
		shape = new Rectangle2D.Double(((Point2D)element.getProperty(GraphElementProperties.POSITION))
				.getX() - width/2,((Point2D)element.getProperty(GraphElementProperties.POSITION)).getY() - totalHeight/2, 
				 width, totalHeight);

	}


	@Override
	public void paint(Graphics2D g) {
		if (updateMeasures){
			calculateMeasures(g);
			formShape();
			updateMeasures = false;
		}
		setGradientPaint();
		g.setFont(font);
		g.setPaint(paint);
		g.fill(path);
		g.setColor(Color.black);
		g.draw(path);
		drawStrings(g);
	}

	private void drawStrings(Graphics2D g) {
		FontMetrics fontMetrics = g.getFontMetrics();
		String name = (String) element.getProperty(GraphElementProperties.NAME);
		int xPos = shape.getBounds().x + shape.getBounds().width/2 - fontMetrics.stringWidth(name)/2; 
		int yPos = shape.getBounds().y + shape.getBounds().height/2 + addHeight/2;
		g.drawString(name,xPos,yPos);
		
	}


	protected void setGradientPaint() {
		paint = new GradientPaint((float) path.getBounds().getMinX(),
				(float) path.getBounds().getMinY(), fillColor1, (float) path
				.getBounds().getMaxX(), (float) path.getBounds()
				.getMaxY(), fillColor2);
	}

	protected void calculateMeasures(Graphics2D g) {
		int textWidht =  g.getFontMetrics(font).stringWidth((String) element.getProperty(GraphElementProperties.NAME));
		int textHeight = g.getFontMetrics(font).getHeight();
		
		minWidth = textWidht + minSpaceHeight;
		minHeight = textHeight + minSpaceHeight;
		//dodaj jezicak
		minHeight = (int) Math.round(minHeight + minHeight * percentH);
		
		optimalWidth = textWidht + optSpaceWidth;
		optimalHeight = textHeight + optSpaceHeight;
		optimalHeight = (int) Math.round(optimalHeight + optimalHeight * percentH);
		
		if (element.getProperty(GraphElementProperties.SIZE) == null || ((Dimension)element.getProperty(GraphElementProperties.SIZE)).getWidth()<minWidth ){
			int setHeight = rectHeight + addHeight;
			int setWidth = rectWidth;
			if (optimalHeight > setHeight)
				setHeight = optimalHeight;
			if (optimalWidth > setWidth)
				setWidth = optimalWidth;
			element.setProperty(GraphElementProperties.SIZE, new Dimension(setWidth, setHeight));
		}
			
	}

	public Dimension2D getMinimumSize() { 
		return new Dimension(minWidth, minHeight);
	}
	
	
	@Override
	public void moveShape(double xPos, double yPos){
		transform = new AffineTransform();
		transform.translate(xPos, yPos);
		path = new GeneralPath(transform.createTransformedShape(path));
		shape = transform.createTransformedShape(shape);
	}
	
	@Override
	public void resizeShape(double width, double height){
		transform = new AffineTransform();
		//skaliranje
		transform.scale(width, height);
		Point2D position = shape.getBounds().getLocation();
		shape = transform.createTransformedShape(shape);
		path = new GeneralPath(transform.createTransformedShape(path));
		//ponistavanje smicanja
		moveShape(position.getX()-shape.getBounds().getLocation().getX(),
				position.getY()-shape.getBounds().getLocation().getY());
	}

	public boolean isUpdateMeasures() {
		return updateMeasures;
	}

	public void setUpdateMeasures(boolean updateMeasures) {
		this.updateMeasures = updateMeasures;
	}
	

}
