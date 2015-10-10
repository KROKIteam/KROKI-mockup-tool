package graphedit.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import graphedit.model.components.Link;

public class RealizationLinkPainter extends GeneralizationLinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RealizationLinkPainter(Link link) {
		super(link);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		Stroke s=g.getStroke();
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, 
				new float[] {5, 3}, 0));
		g.draw(path);
		g.setStroke(s);
		g.setColor(Color.white);
		g.fill(shape);
		g.setColor(Color.black);
		g.draw(shape);
		paintStereotype(g);

	}

}
