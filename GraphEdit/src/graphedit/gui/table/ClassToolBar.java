package graphedit.gui.table;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class ClassToolBar extends JToolBar {

	private static final long serialVersionUID = 1L;

	public ClassToolBar(JButton remove) {
		super(SwingConstants.HORIZONTAL);
		
		setFloatable(false);
		add(remove);
	}

}
