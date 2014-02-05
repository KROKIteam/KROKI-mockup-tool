package graphedit.gui.listeners;

import graphedit.app.MainFrame;
import graphedit.view.GraphEditView;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZoomSliderListener implements ChangeListener {

	private JSlider source;
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof JSlider) {
			source = (JSlider)e.getSource();
			
		    //if (!source.getValueIsAdjusting()) {
		        int zoomLevel = (int)source.getValue();
		        // zoomiraj
		        if (MainFrame.getInstance().getCurrentView() instanceof GraphEditView)
		        	MainFrame.getInstance().getCurrentView().zoom((double)zoomLevel/100);
		        	
		    //}
		}
	}

}
