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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.mode.OperationMode;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.visitor.AllPosibleHierarchyPanels;
import kroki.profil.utils.visitor.AllPosibleNextPanels;
import kroki.profil.utils.visitor.AllPosibleZoomPanels;
import kroki.profil.utils.visitor.Visitor;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class VisibleAssociationEndSettings extends VisibleElementSettings {

	protected JLabel addLb;
	protected JLabel updateLb;
	protected JLabel copyLb;
	protected JLabel deleteLb;
	protected JLabel searchLb;
	protected JLabel changeModeLb;
	protected JLabel dataNavigationLb;
	protected JLabel mandatoryLb;
	protected JLabel defaultViewModeLb;
	protected JLabel defaultOperatonModeLb;
	protected JLabel confirmDeleteLb;
	protected JLabel stayInAddModeLb;
	protected JLabel goToLastAddedLb;
	protected JLabel dataFilterLb;
	protected JLabel sortByLb;
	protected JLabel activationPanelLb;
	protected JLabel targetPanelLb;
	protected JCheckBox addCb;
	protected JCheckBox updateCb;
	protected JCheckBox copyCb;
	protected JCheckBox deleteCb;
	protected JCheckBox searchCb;
	protected JCheckBox changeModeCb;
	protected JCheckBox dataNavigationCb;
	protected JCheckBox mandatoryCb;
	protected JComboBox defaultViewModeCmb;
	protected JComboBox defaultOperationModeCmb;
	protected JCheckBox confirmDeleteCb;
	protected JCheckBox stayInAddModeCb;
	protected JCheckBox goToLastAddedCb;
	protected JTextArea dataFilterTa;
	protected JTextField sortByTf;
	protected JButton sortByBtn;
	protected JScrollPane dataFilterSp;
	protected JTextField activationPanelTf;
	protected JTextField targetPanelTf;
	protected JButton targetPanelBtn;

	public VisibleAssociationEndSettings(SettingsCreator settingsCreator) {
		super(settingsCreator);
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents() {

		addLb = new JLabel();
		addLb.setText(Intl.getValue("stdOperations.add"));
		updateLb = new JLabel();
		updateLb.setText(Intl.getValue("stdOperations.update"));
		copyLb = new JLabel();
		copyLb.setText(Intl.getValue("stdOperations.copy"));
		deleteLb = new JLabel();
		deleteLb.setText(Intl.getValue("stdOperations.delete"));
		searchLb = new JLabel();
		searchLb.setText(Intl.getValue("stdOperations.search"));
		changeModeLb = new JLabel();
		changeModeLb.setText(Intl.getValue("stdOperations.changeMode"));
		dataNavigationLb = new JLabel();
		dataNavigationLb.setText(Intl.getValue("stdOperations.dataNavigation"));
		defaultViewModeLb = new JLabel();
		mandatoryLb = new JLabel(Intl.getValue("visibleProperty.mandatory"));
		defaultViewModeLb.setText(Intl.getValue("stdPanelSettings.defaultViewMode"));
		defaultOperatonModeLb = new JLabel();
		defaultOperatonModeLb.setText(Intl.getValue("stdPanelSettings.defaultOperationMode"));
		confirmDeleteLb = new JLabel();
		confirmDeleteLb.setText(Intl.getValue("stdPanelSettings.confirmDelete"));
		stayInAddModeLb = new JLabel();
		stayInAddModeLb.setText(Intl.getValue("stdPanelSettings.stayInAddMode"));
		goToLastAddedLb = new JLabel();
		goToLastAddedLb.setText(Intl.getValue("stdPanelSettings.goToLastAdded"));
		dataFilterLb = new JLabel();
		dataFilterLb.setText(Intl.getValue("stdDataSettings.dataFilter"));
		sortByLb = new JLabel();
		sortByLb.setText(Intl.getValue("stdDataSettings.sortBy"));
		activationPanelLb = new JLabel();
		activationPanelLb.setText(Intl.getValue("visibleAssociationEnd.activationPanel"));
		targetPanelLb = new JLabel();
		targetPanelLb.setText(Intl.getValue("visibleAssociationEnd.targetPanel"));

		addCb = new JCheckBox();
		updateCb = new JCheckBox();
		copyCb = new JCheckBox();
		deleteCb = new JCheckBox();
		searchCb = new JCheckBox();
		changeModeCb = new JCheckBox();
		dataNavigationCb = new JCheckBox();
		mandatoryCb = new JCheckBox();
		defaultViewModeCmb = new JComboBox(ViewMode.values());
		defaultOperationModeCmb = new JComboBox(OperationMode.values());
		confirmDeleteCb = new JCheckBox();
		stayInAddModeCb = new JCheckBox();
		goToLastAddedCb = new JCheckBox();
		dataFilterTa = new JTextArea(5, 30);
		dataFilterTa.setFont(this.getFont());
		dataFilterSp = new JScrollPane(dataFilterTa);
		dataFilterSp.setMinimumSize(dataFilterTa.getPreferredScrollableViewportSize());
		sortByTf = new JTextField(30);
		sortByTf.setEditable(false);
		sortByBtn = new JButton("...");
		sortByBtn.setPreferredSize(new Dimension(30, 20));
		sortByBtn.setMinimumSize(sortByBtn.getPreferredSize());
		activationPanelTf = new JTextField(30);
		activationPanelTf.setEditable(false);
		targetPanelTf = new JTextField();
		targetPanelTf.setEditable(false);
		targetPanelBtn = new JButton("...");
		targetPanelBtn.setPreferredSize(new Dimension(30, 20));
		targetPanelBtn.setMinimumSize(sortByBtn.getPreferredSize());
	}

	private void layoutComponents() {

		JPanel intermediate = null;
		if (panelMap.containsKey("group.INTERMEDIATE")) {
			intermediate = panelMap.get("group.INTERMEDIATE");
		} else {
			intermediate = new JPanel();
			intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
			panelMap.put("group.INTERMEDIATE", intermediate);
			this.addTab(Intl.getValue("group.INTERMEDIATE"), intermediate);
		}

		intermediate.add(addLb);
		intermediate.add(addCb);
		intermediate.add(updateLb);
		intermediate.add(updateCb);
		intermediate.add(copyLb);
		intermediate.add(copyCb);
		intermediate.add(deleteLb);
		intermediate.add(deleteCb);
		intermediate.add(searchLb);
		intermediate.add(searchCb);
		intermediate.add(changeModeLb);
		intermediate.add(changeModeCb);
		intermediate.add(dataNavigationLb);
		intermediate.add(dataNavigationCb);
		intermediate.add(mandatoryLb);
		intermediate.add(mandatoryCb);
		intermediate.add(defaultViewModeLb);
		intermediate.add(defaultViewModeCmb);
		intermediate.add(defaultOperatonModeLb);
		intermediate.add(defaultOperationModeCmb);
		intermediate.add(confirmDeleteLb);
		intermediate.add(confirmDeleteCb);
		intermediate.add(stayInAddModeLb);
		intermediate.add(stayInAddModeCb);
		intermediate.add(goToLastAddedLb);
		intermediate.add(goToLastAddedCb);
		intermediate.add(dataFilterLb);
		intermediate.add(dataFilterSp);
		intermediate.add(sortByLb);
		intermediate.add(sortByTf, "split 2, grow");
		intermediate.add(sortByBtn, "shrink");
		intermediate.add(activationPanelLb);
		intermediate.add(activationPanelTf);
		intermediate.add(targetPanelLb);
		intermediate.add(targetPanelTf, "split 2, grow");
		intermediate.add(targetPanelBtn, "shrink");

	}

	@Override
	public void updateComponents() {
		super.updateComponents();
		VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
		addCb.setSelected(visibleAssociationEnd.isAdd());
		updateCb.setSelected(visibleAssociationEnd.isUpdate());
		copyCb.setSelected(visibleAssociationEnd.isCopy());
		deleteCb.setSelected(visibleAssociationEnd.isDelete());
		searchCb.setSelected(visibleAssociationEnd.isSearch());
		changeModeCb.setSelected(visibleAssociationEnd.isChangeMode());
		dataNavigationCb.setSelected(visibleAssociationEnd.isDataNavigation());
		mandatoryCb.setSelected(visibleAssociationEnd.lower() != 0);
		defaultViewModeCmb.setSelectedItem(visibleAssociationEnd.getStdPanelSettings().getDefaultViewMode());
		defaultOperationModeCmb.setSelectedItem(visibleAssociationEnd.getStdPanelSettings().getDefaultOperationMode());
		confirmDeleteCb.setSelected(visibleAssociationEnd.getStdPanelSettings().isConfirmDelete());
		stayInAddModeCb.setSelected(visibleAssociationEnd.getStdPanelSettings().isStayInAddMode());
		goToLastAddedCb.setSelected(visibleAssociationEnd.getStdPanelSettings().isGoToLastAdded());
		dataFilterTa.setText(visibleAssociationEnd.getStdDataSettings().getDataFilter());
		String sortByValue = "";
		if (visibleAssociationEnd.getStdDataSettings().getSortBy() != null) {
			sortByValue = visibleAssociationEnd.getStdDataSettings().getSortBy().toString();
		}
		sortByTf.setText(sortByValue);
		String activationPanelValue = "";
		if (visibleAssociationEnd.getActivationPanel() != null) {
			activationPanelValue = visibleAssociationEnd.getActivationPanel().toString();
		}
		activationPanelTf.setText(activationPanelValue);
		String targetPanelValue = "";
		if (visibleAssociationEnd.getTargetPanel() != null) {
			targetPanelValue = visibleAssociationEnd.getTargetPanel().toString();
		}
		targetPanelTf.setText(targetPanelValue);
	}

	private void addActions() {
		addCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setAdd(value);
				updatePreformed();
			}
		});

		updateCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setUpdate(value);
				updatePreformed();
			}
		});

		copyCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setCopy(value);
				updatePreformed();
			}
		});

		deleteCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setDelete(value);
				updatePreformed();
			}
		});

		searchCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setSearch(value);
				updatePreformed();
			}
		});

		changeModeCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setChangeMode(value);
				updatePreformed();
			}
		});

		dataNavigationCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.setDataNavigation(value);
				updatePreformed();
			}
		});

		mandatoryCb.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				if(value) {
					visibleAssociationEnd.setLower(1);
				}else {
					visibleAssociationEnd.setLower(0);
				}
				updatePreformed();
			}
		});

		defaultViewModeCmb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();
				Object value = comboBox.getSelectedItem();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdPanelSettings().setDefaultViewMode((ViewMode) value);
				updatePreformed();
			}
		});

		defaultOperationModeCmb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();
				Object value = comboBox.getSelectedItem();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdPanelSettings().setDefaultOperationMode((OperationMode) value);
				updatePreformed();
			}
		});

		confirmDeleteCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdPanelSettings().setConfirmDelete(value);
				updatePreformed();
			}
		});


		stayInAddModeCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdPanelSettings().setStayInAddMode(value);
				updatePreformed();
			}
		});

		goToLastAddedCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdPanelSettings().setGoToLastAdded(value);
				updatePreformed();
			}
		});

		dataFilterTa.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void removeUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void changedUpdate(DocumentEvent e) {
				//nista se ne desava
			}

			private void contentChanged(DocumentEvent e) {
				Document doc = e.getDocument();
				String text = "";
				try {
					text = doc.getText(0, doc.getLength());
				} catch (BadLocationException ex) {
				}
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				visibleAssociationEnd.getStdDataSettings().setDataFilter(text);
				updatePreformed();
			}
		});

		sortByBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				if (visibleAssociationEnd.getTargetPanel() != null) {
					if (visibleAssociationEnd.getTargetPanel() instanceof StandardPanel) {
						StandardPanel visibleClass = (StandardPanel) visibleAssociationEnd.getTargetPanel();
						List<VisibleProperty> list = visibleClass.containedProperties();
						VisibleProperty sortBy = (VisibleProperty) ListDialog.showDialog(list.toArray(), Intl.getValue("stdDataSettings.choose.sortBy"));
						if (sortBy != null) {
							visibleAssociationEnd.getStdDataSettings().setSortBy(sortBy);
							sortByTf.setText(sortBy.toString());
							updatePreformed();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, Intl.getValue("stdDataSettings.choose.sortBy.denied"));
				}
			}
		});
		targetPanelBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				VisibleAssociationEnd visibleAssociationEnd = (VisibleAssociationEnd) visibleElement;
				List<Object> objectList;
				Visitor visitor = null;
				String info = "";
				if (visibleElement instanceof Zoom) {
					visitor = new AllPosibleZoomPanels();
					info = Intl.getValue("zoom.choose.info");
				}
				if (visibleElement instanceof Next) {
					visitor = new AllPosibleNextPanels();
					info = Intl.getValue("next.choose.info");
				}
				if (visibleElement instanceof Hierarchy) {
					visitor = new AllPosibleHierarchyPanels();
					info = Intl.getValue("hierarchy.choose.info");
				}
				if (visitor != null) {
					visitor.visit(visibleElement);
					objectList = visitor.getObjectList();
					Object selected = ListDialog.showDialog(objectList.toArray(), info);
					if (selected != null) {
						visibleAssociationEnd.setTargetPanel((VisibleClass) selected);
						targetPanelTf.setText(selected.toString());
						//ako je u pitanju kraj Hijerarhija onda prisili promenu komponente
						if (visibleElement instanceof Hierarchy) {
							((Hierarchy) visibleElement).forceUpdateComponent();
							 updateComponents();
						}
						updatePreformed();
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
