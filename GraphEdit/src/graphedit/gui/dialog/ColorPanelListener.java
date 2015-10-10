package graphedit.gui.dialog;

import graphedit.app.MainFrame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 * Adapter class. Handles mouse click event on JPanel instance.
 * @author specijalac
 *
 */
public class ColorPanelListener extends MouseAdapter {

	private JPanel source;

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JPanel)
			source = (JPanel) e.getSource();
		Color color = JColorChooser.showDialog(MainFrame.getInstance(), 
				source.getName(), source.getBackground());
		if (color instanceof Color)
			source.setBackground(color);
	}

}
