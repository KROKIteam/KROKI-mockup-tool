/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.uml_core_basic.UmlProperty;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class HierarchySettings extends VisibleAssociationEndSettings {

	JTextField levelTf;
	JLabel levelLb;
	JLabel viaAssociationEndLb;
	JLabel appliedToPanelLb;
	JLabel hierarchyParentLb;
	JTextField hierarchyParentTf;
	JTextField viaAssociationEndTf;
	JTextField appliedToPanelTf;
	JButton hierarchyParentBtn;
	JButton viaAssociationEndBtn;
	JButton appliedToPanelBtn;
	JComboBox<Integer> cbLevels;


	public HierarchySettings(SettingsCreator settingsCreator) {
		super(settingsCreator);
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents() {
		levelLb = new JLabel(Intl.getValue("hierarchy.level"));
		viaAssociationEndLb = new JLabel(Intl.getValue("hierarchy.viaAssociationEnd"));
		appliedToPanelLb = new JLabel(Intl.getValue("hierarchy.appliedToPanel"));
		hierarchyParentLb = new JLabel(Intl.getValue("hierarchy.hierarchyParent"));
		levelTf = new JTextField();
		levelTf.setEditable(false);
		cbLevels = new JComboBox<Integer>();
		cbLevels.setVisible(false);
		cbLevels.addItem(1);

		hierarchyParentTf = new JTextField();
		hierarchyParentTf.setEditable(false);
		viaAssociationEndTf = new JTextField();
		viaAssociationEndTf.setEditable(false);
		appliedToPanelTf = new JTextField();
		appliedToPanelTf.setEditable(false);
		hierarchyParentBtn = new JButton("...");
		viaAssociationEndBtn = new JButton("...");
		appliedToPanelBtn = new JButton("...");
		hierarchyParentBtn.setPreferredSize(new Dimension(30, 20));
		hierarchyParentBtn.setMinimumSize(sortByBtn.getPreferredSize());
		viaAssociationEndBtn.setPreferredSize(new Dimension(30, 20));
		viaAssociationEndBtn.setMinimumSize(sortByBtn.getPreferredSize());
		appliedToPanelBtn.setPreferredSize(new Dimension(30, 20));
		appliedToPanelBtn.setMinimumSize(sortByBtn.getPreferredSize());


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

		intermediate.add(appliedToPanelLb);
		intermediate.add(appliedToPanelTf, "split 2, grow");
		intermediate.add(appliedToPanelBtn, "shrink");

		intermediate.add(levelLb);
		intermediate.add(levelTf,"");
		intermediate.add(cbLevels, "wrap");

		intermediate.add(hierarchyParentLb);
		intermediate.add(hierarchyParentTf, "split 2, grow");
		intermediate.add(hierarchyParentBtn, "shrink");

		intermediate.add(viaAssociationEndLb);
		intermediate.add(viaAssociationEndTf, "split 2, grow");
		intermediate.add(viaAssociationEndBtn, "shrink");


	}

	@Override
	public void updateComponents() {
		super.updateComponents();	


		Hierarchy hierarchy = (Hierarchy) visibleElement;


		//NEW:
		ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();



		boolean enableLevels = false;
		if (hierarchy.getTargetPanel()!=null && hierarchy.getLevel() != 1){
			if ((hierarchy.getTargetPanel() instanceof ParentChild && hierarchy.getAppliedToPanel() != null) ||
					hierarchy.getTargetPanel() instanceof StandardPanel){
				Vector<Integer> possibleLevels = panel.possibleLevels(hierarchy);
				if (possibleLevels != null && possibleLevels.size() > 0){
					levelTf.setVisible(false);
					cbLevels.setVisible(true);
					if (hierarchy.getLevel() == -1)
						possibleLevels.add(0,-1);
					cbLevels.setModel(new DefaultComboBoxModel<Integer>(possibleLevels));
					enableLevels = true;
				}
			}
		}


		if (!enableLevels){
			levelTf.setVisible(true);
			cbLevels.setVisible(false);
		}
		else{

			//set selected level
			for (int i = 0; i < cbLevels.getItemCount(); i++){
				if (hierarchy.getLevel() == cbLevels.getItemAt(i)){
					cbLevels.setSelectedIndex(i);
					break;
				}
			}
		}

		//disable setting hierarchy parent for level 1
		if (hierarchy.getTargetPanel() == null || hierarchy.getLevel() == 1)
			hierarchyParentBtn.setEnabled(false);
		else
			hierarchyParentBtn.setEnabled(true);

		//disable or enable via association end
		List<VisibleAssociationEnd> viaAssociationEnd = panel.possibleAssociationEnds(hierarchy);

		String viaAssociationEndValue = "";
		if (hierarchy.getViaAssociationEnd() != null) 
			viaAssociationEndValue = hierarchy.getViaAssociationEnd().toString();
		viaAssociationEndTf.setText(viaAssociationEndValue);

		if (viaAssociationEnd != null && viaAssociationEnd.size() > 1)
			viaAssociationEndBtn.setEnabled(true);
		else
			viaAssociationEndBtn.setEnabled(false);



		if (hierarchy.getTargetPanel() != null && hierarchy.getTargetPanel() instanceof ParentChild){
			appliedToPanelLb.setVisible(true);
			appliedToPanelTf.setVisible(true);
			appliedToPanelBtn.setVisible(true);
			if (hierarchy.getAppliedToPanel() == null)
				hierarchyParentBtn.setEnabled(false);
		}
		else{
			appliedToPanelLb.setVisible(false);
			appliedToPanelTf.setVisible(false);
			appliedToPanelBtn.setVisible(false);
		}



		levelTf.setText("" + hierarchy.getLevel());
		String hierarchyParentValue = "";
		if (hierarchy.getHierarchyParent() != null) {
			hierarchyParentValue = hierarchy.getHierarchyParent().toString();
		}
		hierarchyParentTf.setText(hierarchyParentValue);



		String appliedToPanelValue = "";
		if (hierarchy.getAppliedToPanel() != null) {
			appliedToPanelValue = hierarchy.getAppliedToPanel().toString();
		}
		appliedToPanelTf.setText(appliedToPanelValue);

		//disable add, update etc. if the attribute is disable on target panel
		if(hierarchy.getTargetPanel()!=null){
			if(hierarchy.getTargetPanel() instanceof StandardPanel){
				StandardPanel targetSp = (StandardPanel) hierarchy.getTargetPanel();
				if(!targetSp.isAdd()){
					addCb.setSelected(false);
					addCb.setEnabled(false);
				}
				if(!targetSp.isChangeMode()){
					changeModeCb.setSelected(false);
					changeModeCb.setEnabled(false);
				}
				if(!targetSp.isCopy()){
					copyCb.setSelected(false);
					copyCb.setEnabled(false);
				}
				if(!targetSp.isDataNavigation()){
					dataNavigationCb.setSelected(false);
					dataNavigationCb.setEnabled(false);
				}
				if (!targetSp.isDelete()){
					deleteCb.setSelected(false);
					deleteCb.setEnabled(false);
				}
				if (!targetSp.isSearch()){
					searchCb.setSelected(false);
					searchCb.setEnabled(false);
				}
				if (!targetSp.isUpdate()){
					updateCb.setSelected(false);
					updateCb.setEnabled(false);
				}
			}

		}


	}

	private void addActions() {


		cbLevels.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {

				if (event.getStateChange() == ItemEvent.SELECTED){

					Hierarchy hierarchy = (Hierarchy) visibleElement;
					ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();


					Integer level = (Integer) cbLevels.getSelectedItem();
					if (hierarchy.getLevel() == level)
						return;

					hierarchy.changeLevel(level);

					//set hierarchy parent if possible (only one association)
					List<Hierarchy> possibleParents = panel.possibleParents(hierarchy, level - 1);
					if (possibleParents != null && possibleParents.size() == 1){
						hierarchy.updateParent(possibleParents.get(0));
						hierarchyParentTf.setText(possibleParents.get(0).toString());
						List<VisibleAssociationEnd> viaAssociationEnd = panel.possibleAssociationEnds(hierarchy);
						if (hierarchy.getViaAssociationEnd() != null)
							viaAssociationEndTf.setText(hierarchy.getViaAssociationEnd().toString());

						if (viaAssociationEnd != null && viaAssociationEnd.size() > 1)
							viaAssociationEndBtn.setEnabled(true);
						else
							viaAssociationEndBtn.setEnabled(false);
					}

					else {
						hierarchy.updateParent(null);
						hierarchyParentTf.setText("");
						viaAssociationEndTf.setText("");
					}

				}
			}


		});


		hierarchyParentBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();
				Hierarchy hierarchy = (Hierarchy) visibleElement;
				//only enable users to chose hierarchies linked with the current one
				List<Hierarchy> hierarcyList;
				if (hierarchy.getLevel() == -1)
					hierarcyList = panel.possibleParents(hierarchy, -1);
				else
					hierarcyList = panel.possibleParents(hierarchy, hierarchy.getLevel() - 1);

				if (hierarcyList == null){
					JOptionPane.showMessageDialog(null, "No suitable parent hierarchies");
					return;
				}

				Hierarchy parentHierarchy = (Hierarchy) ListDialog.showDialog(hierarcyList.toArray(), "Choose parent hierarchy");
				if (parentHierarchy != null){
					hierarchy.updateParent(parentHierarchy);
					updateComponents();
				}
			}
		});

		viaAssociationEndBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				Hierarchy hierarchy = (Hierarchy) visibleElement;
				ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();

				List<VisibleAssociationEnd> viaAssociationEndList = panel.possibleAssociationEnds(hierarchy);

				VisibleAssociationEnd viaAssociationEnd = (VisibleAssociationEnd) ListDialog.showDialog(viaAssociationEndList.toArray(), "Choose via association end:");

				if (viaAssociationEnd != null) {
					hierarchy.setViaAssociationEnd(viaAssociationEnd);
					updateComponents();
				}

			}
		});
		appliedToPanelBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				Hierarchy hierarchy = (Hierarchy) visibleElement;
				ParentChild panel = (ParentChild) ((UmlProperty) visibleElement).umlClass();

				List<VisibleClass> targetPanelList = new ArrayList<VisibleClass>();
				if (hierarchy.getTargetPanel() != null) {
					targetPanelList = panel.getPossibleAppliedToPanels((ParentChild) hierarchy.getTargetPanel());
				}
				VisibleClass targetPanel = (VisibleClass) ListDialog.showDialog(targetPanelList.toArray(), "Choose applied to panel");

				if (targetPanel != null && hierarchy.getAppliedToPanel() != targetPanel) {
					hierarchy.updateAppliedTo(targetPanel);
					appliedToPanelTf.setText(targetPanel.toString());
					updateComponents();
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
