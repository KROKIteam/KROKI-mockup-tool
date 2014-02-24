/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.operation.BussinessOperation;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BussinessOperationSettings extends VisibleElementSettings {

    JLabel hasParamsFormLb;
    JLabel filteredByKeyLb;
    JLabel persistentOperationLb;
    JCheckBox hasParamsFormCb;
    JCheckBox filteredByKeyCb;
    JTextField persistentOperationTf;
    JButton persistentOperationBtn;

    public BussinessOperationSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        hasParamsFormLb = new JLabel(Intl.getValue("bussinessOperation.hasParametersForm"));
        filteredByKeyLb = new JLabel(Intl.getValue("bussinessOperation.filteredByKey"));
        persistentOperationLb = new JLabel(Intl.getValue("bussinessOperation.persistentOperation"));
        hasParamsFormCb = new JCheckBox();
        filteredByKeyCb = new JCheckBox();
        persistentOperationTf = new JTextField();
        persistentOperationTf.setEditable(false);
        persistentOperationBtn = new JButton("...");
        persistentOperationBtn.setPreferredSize(new Dimension(30, 20));
        persistentOperationBtn.setMinimumSize(persistentOperationBtn.getPreferredSize());
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
            addTab(Intl.getValue("group.INTERMEDIATE"), pane);
        }
        intermediate.add(hasParamsFormLb);
        intermediate.add(hasParamsFormCb);
        intermediate.add(filteredByKeyLb);
        intermediate.add(filteredByKeyCb);
        intermediate.add(persistentOperationLb);
        intermediate.add(persistentOperationTf, "split 2, grow");
        intermediate.add(persistentOperationBtn, "shrink");
    }

    private void addActions() {
        hasParamsFormCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                BussinessOperation bussinessOperation = (BussinessOperation) visibleElement;
                bussinessOperation.setHasParametersForm(value);
                updatePreformed();
            }
        });
        filteredByKeyCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                BussinessOperation bussinessOperation = (BussinessOperation) visibleElement;
                bussinessOperation.setFilteredByKey(value);
                updatePreformed();
            }
        });

        persistentOperationBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Not supported yet.");
            }
        });

    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        BussinessOperation bussinessOperation = (BussinessOperation) visibleElement;
        hasParamsFormCb.setSelected(bussinessOperation.isHasParametersForm());
        filteredByKeyCb.setSelected(bussinessOperation.isFilteredByKey());
        String persistentOperationValue = "";
        if (bussinessOperation.getPersistentOperation() != null) {
            persistentOperationValue = bussinessOperation.getPersistentOperation().toString();
        }
        persistentOperationTf.setText(persistentOperationValue);
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
