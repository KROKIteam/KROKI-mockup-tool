/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import kroki.commons.camelcase.CamelCaser;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.mode.OperationMode;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.property.VisibleProperty;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StandardPanelSettings extends VisibleClassSettings {

	CamelCaser cc = new CamelCaser();
	
	protected JTextField classTf;
    protected JCheckBox addCb;
    protected JCheckBox updateCb;
    protected JCheckBox copyCb;
    protected JCheckBox deleteCb;
    protected JCheckBox searchCb;
    protected JCheckBox changeModeCb;
    protected JCheckBox dataNavigationCb;
    protected JComboBox defaultViewModeCmb;
    protected JComboBox defaultOperationModeCmb;
    protected JCheckBox confirmDeleteCb;
    protected JCheckBox stayInAddModeCb;
    protected JCheckBox goToLastAddedCb;
    protected JTextArea dataFilterTa;
    protected JTextField sortByTf;
    protected JButton sortByBtn;
    protected JLabel classLb;
    protected JLabel addLb;
    protected JLabel updateLb;
    protected JLabel copyLb;
    protected JLabel deleteLb;
    protected JLabel searchLb;
    protected JLabel changeModeLb;
    protected JLabel dataNavigationLb;
    protected JLabel defaultViewModeLb;
    protected JLabel defaultOperatonModeLb;
    protected JLabel confirmDeleteLb;
    protected JLabel stayInAddModeLb;
    protected JLabel goToLastAddedLb;
    protected JLabel dataFilterLb;
    protected JLabel sortByLb;
    protected JScrollPane dataFilterSp;

    public StandardPanelSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {

    	classLb = new JLabel();
    	classLb.setText(Intl.getValue("stdPanelSettings.persistentClass"));
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

        classTf = new JTextField();
        addCb = new JCheckBox();
        updateCb = new JCheckBox();
        copyCb = new JCheckBox();
        deleteCb = new JCheckBox();
        searchCb = new JCheckBox();
        changeModeCb = new JCheckBox();
        dataNavigationCb = new JCheckBox();
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

        intermediate.add(classLb);
        intermediate.add(classTf);
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

    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        StandardPanel visibleClass = (StandardPanel) visibleElement;
        if(visibleClass.getPersistentClass().name() == null) {
        	classTf.setText(cc.toCamelCase(visibleClass.getLabel(), false));
        	visibleClass.getPersistentClass().setName(classTf.getText());
        }else {
        	classTf.setText(visibleClass.getPersistentClass().name());
        }
        addCb.setSelected(visibleClass.isAdd());
        updateCb.setSelected(visibleClass.isUpdate());
        copyCb.setSelected(visibleClass.isCopy());
        deleteCb.setSelected(visibleClass.isDelete());
        searchCb.setSelected(visibleClass.isSearch());
        changeModeCb.setSelected(visibleClass.isChangeMode());
        dataNavigationCb.setSelected(visibleClass.isDataNavigation());
        defaultViewModeCmb.setSelectedItem(visibleClass.getStdPanelSettings().getDefaultViewMode());
        defaultOperationModeCmb.setSelectedItem(visibleClass.getStdPanelSettings().getDefaultOperationMode());
        confirmDeleteCb.setSelected(visibleClass.getStdPanelSettings().isConfirmDelete());
        stayInAddModeCb.setSelected(visibleClass.getStdPanelSettings().isStayInAddMode());
        goToLastAddedCb.setSelected(visibleClass.getStdPanelSettings().isGoToLastAdded());
        dataFilterTa.setText(visibleClass.getStdDataSettings().getDataFilter());
        String sortByValue = "";
        if (visibleClass.getStdDataSettings().getSortBy() != null) {
            sortByValue = visibleClass.getStdDataSettings().getSortBy().toString();
        }
        sortByTf.setText(sortByValue);
    }

    private void addActions() {
    	
    	classTf.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				StandardPanel visibleClass = (StandardPanel) visibleElement;
				visibleClass.getPersistentClass().setName(classTf.getText().trim());
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
    	
        addCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setAdd(value);
                updatePreformed();
            }
        });

        updateCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setUpdate(value);
                updatePreformed();
            }
        });

        copyCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setCopy(value);
                updatePreformed();
            }
        });

        deleteCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setDelete(value);
                updatePreformed();
            }
        });

        searchCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setSearch(value);
                updatePreformed();
            }
        });

        changeModeCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setChangeMode(value);
                updatePreformed();
            }
        });

        dataNavigationCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.setDataNavigation(value);
                updatePreformed();
            }
        });

        defaultViewModeCmb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object value = comboBox.getSelectedItem();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdPanelSettings().setDefaultViewMode((ViewMode) value);
                updatePreformed();
            }
        });

        defaultOperationModeCmb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object value = comboBox.getSelectedItem();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdPanelSettings().setDefaultOperationMode((OperationMode) value);
                updatePreformed();
            }
        });

        confirmDeleteCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdPanelSettings().setConfirmDelete(value);
                updatePreformed();
            }
        });


        stayInAddModeCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdPanelSettings().setStayInAddMode(value);
                updatePreformed();
            }
        });

        goToLastAddedCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdPanelSettings().setGoToLastAdded(value);
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
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                visibleClass.getStdDataSettings().setDataFilter(text);
                updatePreformed();
            }
        });

        sortByBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                //stdDataSettings.choose.sortBy
                StandardPanel visibleClass = (StandardPanel) visibleElement;
                List<VisibleProperty> list = visibleClass.containedProperties();
                VisibleProperty sortBy = (VisibleProperty) ListDialog.showDialog(list.toArray(), Intl.getValue("stdDataSettings.choose.sortBy"));
                if (sortBy != null) {
                    visibleClass.getStdDataSettings().setSortBy(sortBy);
                    sortByTf.setText(sortBy.toString());
                    updatePreformed();
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
