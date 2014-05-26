package graphedit.layout.adding;

import graphedit.layout.AbstractLayouter;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AddingLayouter extends AbstractLayouter{

	private GraphEditModel model;
	private GraphEditView view;
	//private Preferences prefs = Preferences.getInstance();
	private LinkStrategy strategy;
	private List<GraphElement> elements = new ArrayList<GraphElement>();

	public AddingLayouter(GraphEditView view) {
		super();
		this.view = view;
		model = view.getModel();
		//Strategy
		//TODO podrzati right angle strategiju

		//if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
		//strategy = new RightAngledStrategy();
		//else 
		strategy = new AsIsStrategy();
		elements.addAll(model.getDiagramElements());
		elements.addAll(model.getContainedPackages());
	}

	@Override
	public void layout(){
		int num = elements.size();
		int top = num * 100;

		Random random = new Random(top);
		Point testPosition = new Point(0,0);

		for (GraphElement element : elements){

			//pogledaj da li je elementu podesena pozicija, ako jeste ostavi je
			if (element.isLoaded()){
				Point2D position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
				setPosition(strategy, view, (LinkableElement)element, (int) position.getX(), (int)position.getY());
				Dimension loadedDim = element.getLoadedDimension();
				if (loadedDim != null){
					Dimension setDim = (Dimension) element.getProperty(GraphElementProperties.SIZE);
					if (loadedDim.getHeight() >= setDim.getHeight() && loadedDim.getWidth() >= setDim.getWidth()){
						element.setProperty(GraphElementProperties.SIZE, loadedDim);
						view.getElementPainter(element).formShape();
					}
				}
			}
			else{
				boolean set = false;
				while (!set){
					int xPoint = random.nextInt(top);
					int yPoint = random.nextInt(top);
					testPosition.setLocation(xPoint, yPoint);
					if (view.getElementAtPosition(testPosition) == null && view.getLinkAtPosition(testPosition) == null){
						setPosition(strategy, view, (LinkableElement) element, xPoint, yPoint);
						set = true;
					}
				}
			}

		}
		model.fireUpdates();
	}
}



