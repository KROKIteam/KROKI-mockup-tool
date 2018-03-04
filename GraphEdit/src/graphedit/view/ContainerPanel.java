package graphedit.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import graphedit.view.scrollers.HorizontalScrollBar;
import graphedit.view.scrollers.VerticalScrollBar;

public class ContainerPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	GraphEditView view;
	
	private JScrollBar hBar, vBar;
	
	public ContainerPanel(GraphEditView view){
		
		this.view = view;
		
		setLayout(new BorderLayout());
		add(view);
		hBar = new HorizontalScrollBar(view);
	    vBar = new VerticalScrollBar(view);
	    add(hBar, BorderLayout.SOUTH);
	    add(vBar, BorderLayout.EAST);
	   
	    
	   
	}

	public GraphEditView getView() {
		return view;
	}

	public void setView(GraphEditView view) {
		this.view = view;
	}

	public JScrollBar gethBar() {
		return hBar;
	}

	public void sethBar(JScrollBar hBar) {
		this.hBar = hBar;
	}

	public JScrollBar getvBar() {
		return vBar;
	}

	public void setvBar(JScrollBar vBar) {
		this.vBar = vBar;
	}

}
