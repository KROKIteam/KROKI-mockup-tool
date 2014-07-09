package com.panelcomposer.elements.spanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;
import util.resolvers.ComponentResolver;
import util.staticnames.Settings;

import com.panelcomposer.exceptions.ComponentCreationException;
import com.panelcomposer.listeners.ZoomActionListener;
import com.panelcomposer.listeners.ZoomFocusListener;
import com.panelcomposer.model.attribute.AbsAttribute;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.configuration.Zoom;

@SuppressWarnings("serial")
public class InputPanel extends JPanel {

	private Dimension dimension;
	private double width;
	private double ratio;
	private double componentsLength;
	private int rowNumber;
	private int zoomCounter;
	private JPanel panelOne;
	private JPanel panelTwo;
	private int counter;
	private LayoutManager panelLayout;
	private String labelText;
	private List<JComponent> panelComponents;
	private SPanel panel;
	private JButton btnCommit;
	private JButton btnCancel;
	private JButton btnStartSearch;

	public InputPanel(SPanel panel) {
		if (panel == null)
			return;
		this.panel = panel;
		init();
	}

	public void init() {
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		width = dimension.getWidth() - 150;
		ratio = width / 1024.0;
		componentsLength = 0;
		rowNumber = 1;
		zoomCounter = 0;
		counter = 0;
		panelComponents = new ArrayList<JComponent>();
		labelText = "";
		
		panelLayout = new FlowLayout(FlowLayout.LEFT);
		setLayout(new MigLayout("", "[0:0, grow 100, fill]", ""));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panelOne = new JPanel(panelLayout);
		List<AbsAttribute> attributes = panel.getTable().getTableModel().getEntityBean().getAttributes();
		for (int i = 0; i < attributes.size(); i++) {
			panelTwo = new JPanel(new FlowLayout(FlowLayout.LEFT));
			if (attributes.get(i) instanceof ColumnAttribute) {
				System.out.println("[CREATE COMPONENT ZA COLUMN] " + attributes.get(i).getFieldName());
				createComponent((ColumnAttribute) attributes.get(i));
			} else if (attributes.get(i) instanceof JoinColumnAttribute) {
				System.out.println("[CREATE COMPONENT ZA JOIN] " + attributes.get(i).getFieldName());
				createComponent((JoinColumnAttribute) attributes.get(i));
			}
		}
		panelOne.setMinimumSize(new Dimension((int) panelOne.getSize().getWidth(), 20 + 40 * rowNumber));
		addCommitPanel();
		setDerivedFormulas();
	}

	/***
	 * Creates component on panel for regular ColumnAttribute
	 * 
	 * @param colAttr
	 */
	private void createComponent(ColumnAttribute colAttr) {
		try {
			panelOne = new JPanel(panelLayout);
			addComponentToPanelTwo(colAttr, null, counter);
			panelOne.add(panelTwo);
			add(panelOne, "wrap, span");
			panelOne = new JPanel(panelLayout);
			setCurrentComponentsLength();
			counter++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Creates components on panel for JoinColumnAttribute
	 * 
	 * @param joinColAttr
	 */
	private void createComponent(JoinColumnAttribute joinColAttr) {
		panelOne = new JPanel(panelLayout);
		panelTwo.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
				joinColAttr.getLabel(), 1, 1, null, Color.BLUE));
		ColumnAttribute colAttr = null;
		JButton btn = null;
		for (int j = 0; j < joinColAttr.getColumns().size(); j++) {
			colAttr = joinColAttr.getColumns().get(j);
			try {
				JComponent comp = addComponentToPanelTwo(colAttr, joinColAttr,counter);
				if (colAttr.getName().equals(joinColAttr.getZoomedBy())) {
					btn = new JButton("...");
					createButtonListener(joinColAttr, btn);
					panelTwo.add(btn);
				} else {
					panelComponents.get(counter - 1).addFocusListener(
							new ZoomFocusListener(joinColAttr, 
									panelComponents.get(counter - 1), comp));
					comp.setEnabled(false);
				}
				setCurrentComponentsLength();
				counter++;
			} catch (ComponentCreationException e) {
				e.printStackTrace();
			}
		}
		panelOne.add(panelTwo);
		panelTwo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(panelOne, "wrap");
		rowNumber++;
	}

	/***
	 * Adding ZoomActionListener on button
	 * 
	 * @param joinColAttr
	 * @param btn
	 */
	private void createButtonListener(JoinColumnAttribute joinColAttr, JButton btn) {
		Zoom zoom = null;
		if (panel.getModelPanel().getZoomPanels().size() > zoomCounter) {
			zoom = panel.getModelPanel().getZoomPanels().get(zoomCounter);
			zoomCounter++;
		}
		btn.addActionListener(new ZoomActionListener(zoom, joinColAttr, panel));
	}

	/***
	 * Actual adding component on panel (panelTwo)
	 * 
	 * @param colAttr
	 * @param joinColAttr
	 *            If available
	 * @param position
	 * @return
	 * @throws ComponentCreationException
	 */
	private JComponent addComponentToPanelTwo(ColumnAttribute colAttr,
			JoinColumnAttribute joinColAttr, int position)
			throws ComponentCreationException {
		JComponent component = null;
		JLabel label = null;
		try {
			if(colAttr.getFieldName().equalsIgnoreCase("id")) {
				colAttr.setHidden(true);
			}
			labelText = colAttr.getLabel() + ":";
			label = new JLabel(labelText);
			panelTwo.add(label);
			component = setUpComponent(colAttr, joinColAttr);
			panelTwo.add(component);
			panelComponents.add(component);
			if (colAttr.getHidden()) {
				label.setVisible(false);
				component.setVisible(false);
			}
			setCurrentComponentsLength();
			return component;
		} catch (Exception e) {
			throw new ComponentCreationException("Couldn't create component");
		}
	}

	private void setCurrentComponentsLength() {
		componentsLength += panelLayout.preferredLayoutSize(panelTwo)
				.getWidth();
		if (componentsLength > width) {
			rowNumber++;
			componentsLength = panelLayout.preferredLayoutSize(panelTwo)
					.getWidth();
		}
	}

	public JComponent setUpComponent(ColumnAttribute colAttr,
			JoinColumnAttribute joinColAttr) {
		JComponent component = colAttr.getComponent();
		int charSize = ComponentResolver.charactersCount(colAttr.getLength(),
				colAttr.getScale());
		charSize = (charSize > Settings.MAX_CHAR_SIZE) ? Settings.MAX_CHAR_SIZE
				: charSize;
		if (charSize == 0) {
			charSize = 10;
		}
		component = ComponentResolver.getComponent(colAttr);
		System.out.println("[COMPONENT] " + component.getClass().getSimpleName());
		if(!component.getClass().getSimpleName().equals("JTextArea")) {
			component.setPreferredSize(new Dimension((int) (charSize * 10 * ratio)/2, 20));
		}else {
			Dimension dim = (new Dimension((int) ((charSize * 10 * ratio)/2)-15, 100));
			component.setPreferredSize(dim);
			component.setMinimumSize(dim);
		}
		component.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY)));
		Font font = new Font("Verdana", Font.PLAIN, 12);
		component.setFont(font);
		component.setEnabled(!colAttr.getDisabled());
		return component;
	}

	public void clearComponentFields() {
		EntityBean bean = getPanel().getModelPanel().getEntityBean();
		ColumnAttribute ca;
		JoinColumnAttribute jca;
		int counter = 0;
		for (AbsAttribute attr : bean.getAttributes()) {
			if (attr instanceof JoinColumnAttribute) {
				jca = (JoinColumnAttribute) attr;
				for (ColumnAttribute colAttr : jca.getColumns()) {
					clearComponentOnDefault(panelComponents.get(counter),
							colAttr);
					counter++;
				}
			} else if (attr instanceof ColumnAttribute) {
				ca = (ColumnAttribute) attr;
				clearComponentOnDefault(panelComponents.get(counter), ca);
				counter++;
			}
		}
		setFocusOnFirst();
	}

	public void clearComponentOnDefault(JComponent component, ColumnAttribute colAttr) {

		if (colAttr.getDefaultValue() == null) {
			if (component instanceof JTextField) {
				((JTextField) component).setText("");
			} else if (component instanceof JCheckBox) {
				((JCheckBox) component).setSelected(false);
			} else if (component instanceof JComboBox) {
				((JComboBox) component).setSelectedIndex(-1);
			}
		} else {
			if (component instanceof JTextField) {
				((JTextField) component).setText(colAttr.getDefaultValue().toString());
			} else if (component instanceof JCheckBox) {
				((JCheckBox) component).setSelected((Boolean) colAttr.getDefaultValue());
			} else if (component instanceof JComboBox) {
				((JComboBox) component).setSelectedIndex((Integer) colAttr.getDefaultValue());
			}
		}
	}

	private void setFocusOnFirst() {
		Iterator<JComponent> compIter = getPanelComponents().iterator();
		while (compIter.hasNext()) {
			JComponent component = compIter.next();
			if (component.isEnabled()) {
				component.requestFocus();
				break;
			}
		}
	}

	/***
	 * Adds buttons for commit, rollback and state exit to this panel
	 */
	private void addCommitPanel() {
		btnCommit = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + "commit.gif")));
		btnCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				panel.handleCommit();
			}
		});
		btnCancel = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + "rollback.gif")));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				panel.handleCancel();
			}
		});
		btnStartSearch = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + "search2.gif")));
		btnStartSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				panel.handleStartSearch();
			}
		});
		btnStartSearch.setEnabled(false);

		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		jp.add(btnCommit);
		jp.add(btnCancel);
		jp.add(btnStartSearch);
		add(jp);
	}

	public List<JComponent> getPanelComponents() {
		return panelComponents;
	}

	public void setPanelComponents(List<JComponent> panelComponents) {
		this.panelComponents = panelComponents;
	}

	public SPanel getPanel() {
		return panel;
	}

	public void setPanel(SPanel panel) {
		this.panel = panel;
	}

	public JButton getBtnCommit() {
		return btnCommit;
	}

	public void setBtnCommit(JButton btnCommit) {
		this.btnCommit = btnCommit;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(JButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public JButton getBtnStartSearch() {
		return btnStartSearch;
	}

	public void setBtnStartSearch(JButton btnStartSearch) {
		this.btnStartSearch = btnStartSearch;
	}

}
