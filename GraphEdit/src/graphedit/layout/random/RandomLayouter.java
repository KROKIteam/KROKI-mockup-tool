package graphedit.layout.random;

import graphedit.layout.AbstractLayouter;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLayouter extends AbstractLayouter{

	private GraphEditModel model;
	private GraphEditView view;
	//private Preferences prefs = Preferences.getInstance();
	private LinkStrategy strategy;
	private List<GraphElement> elements = new ArrayList<GraphElement>();

	public RandomLayouter(GraphEditView view) {
		super();
		this.view = view;
		model = view.getModel();
		//Strategy
		//TODO podrzati right angle strategiju

		//if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
		//strategy = new RightAngledStrategy();
		//else 
		strategy = new AsIsStrategy();
		elements = model.getDiagramElements();
		elements.addAll(model.getContainedPackages());
	}

	@Override
	public void layout(){
		int num = elements.size();
		int top = num * 100;

		Random random = new Random(top);
		Point testPosition = new Point(0,0);
		for (GraphElement element : elements){
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
		model.fireUpdates();
	}
	
	


}
