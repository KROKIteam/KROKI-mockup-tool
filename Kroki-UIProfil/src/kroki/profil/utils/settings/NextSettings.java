/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.visitor.AllPosibleHierarchyPanels;
import kroki.profil.utils.visitor.AllPosibleNexts;
import kroki.profil.utils.visitor.AllPosibleZoomPanels;
import kroki.profil.utils.visitor.Visitor;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class NextSettings extends VisibleAssociationEndSettings {

	protected JLabel autoActivateLb;
	protected JLabel displayIdentifierLb;
	protected JLabel displayRepresentativeLb;
	protected JCheckBox autoActivateCb;
	protected JCheckBox displayIdentifierCb;
	protected JCheckBox displayRepresentativeCb;


	public NextSettings(SettingsCreator settingsCreator) {
		super(settingsCreator);
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents() {
		oppositeLb = new JLabel(Intl.getValue("next.zoom"));
		autoActivateLb = new JLabel(Intl.getValue("next.autoActivate"));
		displayIdentifierLb = new JLabel(Intl.getValue("next.displayRepresentative"));
		displayRepresentativeLb = new JLabel(Intl.getValue("next.displayIdentifier"));
		autoActivateCb = new JCheckBox();
		displayIdentifierCb = new JCheckBox();
		displayRepresentativeCb = new JCheckBox();
		oppositeTf = new JTextField();
		oppositeTf.setPreferredSize(new Dimension(30, 20));
		oppositeTf.setEditable(false);
		oppositeBtn = new JButton("...");
		oppositeBtn.setPreferredSize(new Dimension(30, 20));
		oppositeBtn.setMinimumSize(oppositeBtn.getPreferredSize());
	}

	private void layoutComponents() {
		JPanel intermediate = null;
		JScrollPane pane;
		if (panelMap.containsKey("group.INTERMEDIATE")) {
			intermediate = panelMap.get("group.INTERMEDIATE");
		} else {
			intermediate = new JPanel();
			intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
			panelMap.put("group.INTERMEDIATE", intermediate);
			pane = new JScrollPane(intermediate);
			this.addTab(Intl.getValue("group.INTERMEDIATE"), pane);
		}
		intermediate.add(oppositeLb);
		intermediate.add(oppositeTf, "split 2, grow");
		intermediate.add(oppositeBtn, "shrink");
		intermediate.add(autoActivateLb);
		intermediate.add(autoActivateCb);
		intermediate.add(displayIdentifierLb);
		intermediate.add(displayIdentifierCb);
		intermediate.add(displayRepresentativeLb);
		intermediate.add(displayRepresentativeCb);
	}

	@Override
	public void updateComponents() {
		super.updateComponents();
		Next next = (Next) visibleElement;
		autoActivateCb.setSelected(next.isAutoActivate());
		displayIdentifierCb.setSelected(next.isDisplayIdentifier());
		displayRepresentativeCb.setSelected(next.isDisplayRepresentative());

		String oppositeValue = "";
		if (next.opposite() != null) {
			oppositeValue = next.opposite().toString();
		}
		oppositeTf.setText(oppositeValue.toString());
	}

	private void addActions() {
		autoActivateCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setAutoActivate(value);
				updatePreformed();
			}
		});
		displayIdentifierCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setDisplayIdentifier(value);
				updatePreformed();
			}
		});
		displayRepresentativeCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setDisplayRepresentative(value);
				updatePreformed();
			}
		});

		oppositeBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				Next next = (Next) visibleElement;
				List<Object> objectList;
				Visitor visitor = null;
				String info = "";

				visitor = new AllPosibleNexts();
				info = Intl.getValue("next.choose.zoom");
				if (visitor != null) {
					visitor.visit(visibleElement);
					objectList = visitor.getObjectList();
					Object selected = ListDialog.showDialog(objectList.toArray(), info);
					Object panel;
					if (selected != null) {
						//target zoom was changed, update zoom's opposite
						if (next.opposite() != null)
							next.opposite().setOpposite(null);
						//set opposite
						next.setOpposite((Zoom)selected);
						panel = ((Zoom)selected).getActivationPanel();
						//set zoom's opposite
						((Zoom)selected).setOpposite((Next) visibleElement);
						oppositeTf.setText(((Zoom)selected).getLabel());
						//set target panel if not set
						if (next.getTargetPanel() == null){
							next.setTargetPanel((VisibleClass) panel);
							targetPanelTf.setText(panel.toString());
						}
					}
				}
			}
		});
	}

		@Override
		public void updateSettings(VisibleElement visibleElement) {
			super.updateSettings(visibleElement);
		}

		@Override
		public void updatePreformed() {
			super.updatePreformed();
		}
	}
