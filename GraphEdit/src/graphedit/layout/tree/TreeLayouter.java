package graphedit.layout.tree;

import graphedit.layout.AbstractLayouter;
import graphedit.layout.LayouterException;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.ClassShortcut;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.properties.LayoutProperties;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.view.ClassPainter;
import graphedit.view.GraphEditView;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

public class TreeLayouter extends AbstractLayouter{

	private GraphEditModel model;
	private GraphEditView view;
	private LayoutProperties layoutProperties = LayoutProperties.getInstance();
	//private Preferences prefs = Preferences.getInstance();
	private LinkStrategy strategy;
	private List<GraphElement> addedElements = new ArrayList<GraphElement>();
	private Graphics g;

	public TreeLayouter(GraphEditView view, Graphics g) {
		super();
		this.view = view;
		model = view.getModel();
		this.g = g;
		//Strategy
		//TODO podrzati right angle strategiju

		//if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
		//strategy = new RightAngledStrategy();
		//else 
		strategy = new AsIsStrategy();
	}

	private boolean checkTree(LayoutTreeNode node){
		boolean ok = checkTreeNode(node);

		for (LayoutTreeNode child : node.getChildren()){
			ok = ok && checkTreeNode(child);
		}
		return ok;
	}

	private boolean checkTreeNode(LayoutTreeNode node){
		if (node.getChildren().contains(node))
			return false;
		return true;
	}


	@Override
	public void layout() throws LayouterException{
		LayoutTreeFactory factory = new LayoutTreeFactory(model);
		List<LayoutTree> trees = factory.createTrees();
	


		// setup the tree layout configuration

		double gapBetweenLevels = layoutProperties.getGapBetweenLevels();
		double gapBetweenNodes = layoutProperties.getGapBetweenElements();
		int gapBetweenTrees = layoutProperties.getGapBetweenTrees();

		DefaultConfiguration<LayoutTreeNode> configuration = new DefaultConfiguration<LayoutTreeNode>(
				gapBetweenLevels, gapBetweenNodes);
		LayoutNodeExtentProvider nodeExtentProvider = new LayoutNodeExtentProvider();
		int offset = 0;
		if (trees == null)
			throw new LayouterException("Tree Layout exception");

		Collections.sort(trees);

		for (LayoutTree tree : trees){
			// create the layout
			if (!checkTree(tree.getRoot()))
				throw new LayouterException("Tree Layout exception");
			TreeLayout<LayoutTreeNode> treeLayout = new TreeLayout<LayoutTreeNode>(tree,
					nodeExtentProvider, configuration);
			setPositions(treeLayout, tree.getRoot(), offset);
			offset += treeLayout.getBounds().getWidth() + gapBetweenTrees;
		}
		model.fireUpdates();
	}
	

	private void setPositions(TreeLayout<LayoutTreeNode> treeLayout, LayoutTreeNode node, int offset){
		Rectangle2D.Double bounds = ( Rectangle2D.Double) treeLayout.getNodeBounds().get(node);
		LinkableElement element = (LinkableElement) node.getElement();
//		if (addedElements.contains(element)){
//			//napravi shortcut
//			Shortcut shortcut = null;
//			if (element instanceof Class){
//				shortcut = new ClassShortcut(new Point(0,0), (Class)element, model);
//				model.addDiagramElement((GraphElement) shortcut);
//				ClassPainter painter = new ClassPainter((GraphElement)shortcut);
//				view.addElementPainter(painter);
//				painter.drawStrings((Graphics2D) g);
//				
//				setPosition(strategy, view, (LinkableElement)shortcut, (int)bounds.getCenterX() + offset, (int)bounds.getCenterY());
//			}
//		}
//		else{
//			addedElements.add(element);
//			setPosition(strategy, view, (LinkableElement)node.getElement(), (int)bounds.getCenterX() + offset, (int)bounds.getCenterY());
//		}
		
		addedElements.add(element);
		setPosition(strategy, view, (LinkableElement)node.getElement(), (int)bounds.getCenterX() + offset, (int)bounds.getCenterY());
		
		for (LayoutTreeNode child : node.getChildren())
			setPositions(treeLayout, child, offset);
	}





}
