package graphedit.view;

import java.awt.Color;
import java.awt.Graphics2D;

import graphedit.model.components.Link;

public class CompositionLinkPainter extends AggregationLinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompositionLinkPainter(Link link) {
		super(link);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.fill(shape);
		g.draw(shape);
		g.draw(path);
		paintCardinality(g);
		paintStereotype(g);
	}
}
