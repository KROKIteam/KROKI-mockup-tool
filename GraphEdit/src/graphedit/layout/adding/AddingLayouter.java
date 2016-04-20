package graphedit.layout.adding;

import graphedit.layout.AbstractLayouter;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.view.GraphEditView;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AddingLayouter extends AbstractLayouter{

	//private Preferences prefs = Preferences.getInstance();
	private List<GraphElement> elements = new ArrayList<GraphElement>();

	public AddingLayouter(GraphEditView view) {
		super(view);
		//Strategy
		//TODO podrzati right angle strategiju

		//if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
		//strategy = new RightAngledStrategy();
		//else 
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
				Dimension loadedDim = element.getLoadedDimension();
				if (loadedDim != null){
					Dimension setDim = (Dimension) element.getProperty(GraphElementProperties.SIZE);
					int width = (int) (setDim.getWidth() > loadedDim.getWidth() ? setDim.getWidth() : loadedDim.getWidth());
					int height = (int) (setDim.getHeight() > loadedDim.getHeight() ? setDim.getHeight() : loadedDim.getHeight());
					setDim.setSize(width, height);
						//element.setProperty(GraphElementProperties.SIZE, loadedDim);
					view.getElementPainter(element).formShape();
				}
				
				for (Connector conn : ((LinkableElement)element).getConnectors()){
					Point2D conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
					
					if (conn.getLoadedPosition() != null){
						conPosition.setLocation(conn.getLoadedPosition());
					}
					
					
					else{  //ako je nova veza
						conPosition.setLocation((Point2D) element.getProperty(GraphElementProperties.POSITION));
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
			if (element instanceof LinkableElement)
				setConnectorLocations((LinkableElement) element);
		}
		model.fireUpdates();
	}
}



