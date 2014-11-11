package adapt.model.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdaptManyToManyPanel extends AdaptPanel {

	protected List<AdaptStandardPanel> panels = new ArrayList<AdaptStandardPanel>();
	
	public void add(AdaptStandardPanel spanel) {
		panels.add(spanel);
	}
	
	public AdaptStandardPanel findByLevel(Integer level) {
		Iterator<AdaptStandardPanel> it = panels.iterator();
		while(it.hasNext()) {
			AdaptStandardPanel panel = it.next();
			if(panel.getLevel().intValue() == level.intValue()) {
				return panel;
			}
		}
		return null;
	}

	public List<AdaptStandardPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<AdaptStandardPanel> panels) {
		this.panels = panels;
	}
}
