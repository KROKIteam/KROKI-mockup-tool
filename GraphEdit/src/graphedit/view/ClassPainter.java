package graphedit.view;

import graphedit.app.MainFrame;
import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.Preferences;
import graphedit.util.Calculate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * This class is the graphical represent of all <code>Class</code> instances.
 */
public class ClassPainter extends ElementPainter {

	private static final long serialVersionUID = 1L;

	private Class shortcutTo;

	public ClassPainter(GraphElement element) {
		super(element);
		font = new Font("Sans-serif", Font.PLAIN, 12);

		fillColor1 = Preferences.getInstance().parseColor(Preferences.CLASS_COLOR_1); 
		fillColor2 = Preferences.getInstance().parseColor(Preferences.CLASS_COLOR_2);

		if (element instanceof Shortcut)
			shortcutTo = (Class) ((Shortcut)element).shortcutTo();
	}

	@Override
	public void paint(Graphics2D g) {
		setShape(g);
		setGradientPaint();
		g.setFont(font);
		g.setPaint(paint);
		g.fill(shape);
		g.setColor(Color.black);
		g.draw(shape);
		drawStrings(g);
	}

	public void setShape(Graphics2D g) {
		Dimension2D size = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
		double currentWidth = 0, currentHeight = 0;
		if (size instanceof Dimension2D) {
			currentWidth = size.getWidth();
			currentHeight = size.getHeight();
		}
		if (updated) {
			updateMeasures(g);
			if (size instanceof Dimension2D) {
				if ( attributesOrMethodsUpdated  || (currentWidth < optimalWidth || currentHeight < (optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight))) {
					if (currentWidth > optimalWidth)
						optimalWidth = currentWidth; 

					if (currentHeight > (optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight))
						totalHeight = currentHeight;
					else 
						totalHeight = optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight;

					totalWidth = optimalWidth;	


					updateShape();
				}
			} else { 
				setShape();
			}

			updated = false;
		} else {
			mainRectangleUpperLeftX = shape.getBounds().getMinX();
			mainRectangleUpperLeftY = shape.getBounds().getMinY();
			totalWidth = shape.getBounds().getSize().getWidth();
			optimalWidth = totalWidth;
			totalHeight = shape.getBounds().getSize().getHeight();
		}
	}

	@SuppressWarnings("unchecked")
	public void drawStrings(Graphics2D g) {
		double currentY;
		FontMetrics fontMetrics = g.getFontMetrics();
		Class c = (Class)element;
		String className = ""; 
		shortcutInfo = "";
		if (shortcutTo != null){
			c = shortcutTo;
			shortcutInfo = ((Shortcut)element).shortcutInfo();
			className = "Shortcut to "; 
		}

		attributes = (List<Attribute>) c.getProperty(GraphElementProperties.ATTRIBUTES);
		methods = (List<Method>) c.getProperty(GraphElementProperties.METHODS);


		className += (String)c.getProperty(GraphElementProperties.NAME);

		g.drawString(className,
				(int)(mainRectangleUpperLeftX + (totalWidth - fontMetrics.stringWidth(className))/2), 
				(int)(mainRectangleUpperLeftY + fontMetrics.getHeight()));

		String stereotype = "<<" + (String) c.getProperty(GraphElementProperties.STEREOTYPE) + ">>";
		if (!stereotype.equals("<<>>"))
			g.drawString(stereotype,
					(int)(mainRectangleUpperLeftX + (totalWidth - fontMetrics.stringWidth(stereotype))/2), 
					(int)(mainRectangleUpperLeftY + optimalClassHeight - 2	 * treshold));

		if (c instanceof Shortcut)
			shortcutInfo = ((Shortcut)c).shortcutInfo();

		if (!shortcutInfo.equals(""))
			g.drawString(shortcutInfo,
					(int)(mainRectangleUpperLeftX + (totalWidth - fontMetrics.stringWidth(shortcutInfo))/2), 
					(int)(mainRectangleUpperLeftY + 2*fontMetrics.getHeight() + treshold));

		currentY = mainRectangleUpperLeftY + optimalClassHeight + optimalAttributesHeight - 4 * treshold;
		//ispisi u obrnutom redosledu
		Attribute a;
		for (int i=attributes.size()-1;i>=0; i--){
			a = attributes.get(i);
			if (!a.isVisible())
				continue;
			g.drawString(a.toString(),
					(int)(mainRectangleUpperLeftX + 2 * treshold), 
					(int)(currentY));
			currentY -= fontMetrics.getHeight() + treshold;
		}
		currentY = mainRectangleUpperLeftY + optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight - 4 * treshold;
		Method m;
		for (int i = methods.size()-1; i>=0; i--){
			m = methods.get(i);
			if (!m.isVisible())
				continue;
			g.drawString(m.toString(),
					(int)(mainRectangleUpperLeftX + 2 *treshold), 
					(int)(currentY));
			currentY -= fontMetrics.getHeight() + treshold;
		}

		// injects the size, this shall be done only once in painters lifetime
		if (element.getProperty(GraphElementProperties.SIZE) == null) {
			element.setProperty(GraphElementProperties.SIZE, new Dimension((int)totalWidth, (int)totalHeight));
		}
	}

	protected void updateMeasures(Graphics2D g) {
		double[] result;
		if (shortcutTo != null){
			shortcutInfo = ((Shortcut)element).shortcutInfo();
			result = Calculate.getOptimalMeasures(shortcutTo, g.getFontMetrics(font), true, shortcutInfo);
		}
		else
			result = Calculate.getOptimalMeasures((Class)element, g.getFontMetrics(font), false, shortcutInfo);

		optimalWidth = result[0]; 
		optimalClassHeight = result[1]; 
		optimalAttributesHeight = result[2]; 
		optimalMethodsHeight = result[3];

		// set minimum width and height for this particular class
		minWidth = (int)optimalWidth;
		minHeight = (int)(optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight);
	}

	/**
	 * This method should be invoked if, and only if, class shape's been changed
	 * <code>ResizeElementsCommand</code>, <code>MoveElementsCommand</code> commands.
	 */
	@Override
	public void formShape() {
		Point2D position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
		Dimension2D size = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
		optimalWidth = size.getWidth();
		totalWidth = optimalWidth;
		totalHeight = size.getHeight(); 
		mainRectangleUpperLeftX = position.getX() - totalWidth/2;
		mainRectangleUpperLeftY = position.getY() - totalHeight/2;
		generateGeneralPath();

	}

	/**
	 * This method should be invoked initially when class is changes, for instance
	 * attribut name is changed, method added, etc... 
	 */
	public void setShape() {

		Point2D position = (Point2D) ((Class)element).getProperty(GraphElementProperties.POSITION);
		totalWidth = optimalWidth;
		totalHeight = optimalClassHeight + optimalAttributesHeight + optimalMethodsHeight;
		mainRectangleUpperLeftX = position.getX() - totalWidth/2;
		mainRectangleUpperLeftY = position.getY() - totalHeight/2;
		generateGeneralPath();
	}

	/**
	 * forms shape for optimal measures, taking care of its element's connectors positions
	 */
	public void updateShape() {

		// take care of connectors' positions
		if (((LinkableElement)element).getConnectors().size() > 0 && totalWidth > 0 && totalHeight > 0) {
			Dimension2D currentDimension = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
			double sx = totalWidth/(currentDimension.getWidth() == 0 ? totalWidth : currentDimension.getWidth());
			double sy = totalHeight/(currentDimension.getHeight() == 0 ? totalHeight : currentDimension.getHeight());
			element.setProperty(GraphElementProperties.SIZE, new Dimension((int)totalWidth, (int)totalHeight));
			formShape();
			((LinkableElement)element).scaleAllConnectors(sx, sy, getShape());

			// temporarily... should be done in command by model.fireUpdate()
			if (MainFrame.getInstance().getCurrentView() != null) {
				MainFrame.getInstance().getCurrentView().getModel().fireUpdates();
			}
		} else {
			element.setProperty(GraphElementProperties.SIZE, new Dimension((int)totalWidth, (int)totalHeight));
			formShape();
		}
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	protected void generateGeneralPath() {
		GeneralPath path;
		Shape mainRectangle = new Rectangle2D.Double(mainRectangleUpperLeftX, mainRectangleUpperLeftY, totalWidth,	totalHeight);
		shape = new GeneralPath(mainRectangle);
		path = (GeneralPath)shape;
		path.moveTo(mainRectangleUpperLeftX, mainRectangleUpperLeftY + optimalClassHeight);
		path.lineTo(mainRectangleUpperLeftX + optimalWidth, mainRectangleUpperLeftY + optimalClassHeight);
		path.moveTo(mainRectangleUpperLeftX + optimalWidth, mainRectangleUpperLeftY + optimalClassHeight + optimalAttributesHeight);
		path.lineTo(mainRectangleUpperLeftX, mainRectangleUpperLeftY + optimalClassHeight + optimalAttributesHeight);
	}

	protected void setGradientPaint() {
		paint = new GradientPaint((float) shape.getBounds().getMinX(),
				(float) shape.getBounds().getMinY(), fillColor1, (float) shape
				.getBounds().getMaxX(), (float) shape.getBounds()
				.getMaxY(), fillColor2);
	}

	@Override
	public void setElement(GraphElement newGraphElement) {
		this.element = newGraphElement;
		setShape();
		if (element.isShadowElement()){
			fillColor1 = new Color(fillColor1.getRed(), fillColor1.getGreen(), fillColor1.getBlue(), 128);
			fillColor2 = new Color(fillColor2.getRed(), fillColor2.getGreen(), fillColor2.getBlue(), 128);
		}
	}
	@Override
	public Dimension2D getMinimumSize() {
		return new Dimension(minWidth, minHeight);
	}

	public boolean isAttributesOrMethodsUpdated() {
		return attributesOrMethodsUpdated;
	}

	public void setAttributesOrMethodsUpdated(boolean attributesOrMethodsUpdated) {
		this.attributesOrMethodsUpdated = attributesOrMethodsUpdated;
	}


	protected Font font;
	protected int minWidth, minHeight;
	protected double optimalWidth; 
	protected double optimalClassHeight; 
	protected double optimalAttributesHeight; 
	protected double optimalMethodsHeight;
	protected double mainRectangleUpperLeftX;
	protected double mainRectangleUpperLeftY;
	protected double totalWidth;
	protected double totalHeight;
	public static final double treshold = 2.0;
	protected transient GradientPaint paint;
	private boolean updated = true;
	private List<Attribute> attributes;
	private List<Method> methods;
	private boolean attributesOrMethodsUpdated = false;
	private String shortcutInfo="";

}