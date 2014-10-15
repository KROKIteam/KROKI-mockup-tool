/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.settings;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleClassSettings extends VisibleElementSettings {

    JLabel modalLb;
    JCheckBox modalCb;

    public VisibleClassSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        modalLb = new JLabel();
        modalLb.setText(Intl.getValue("visibleClass.modal"));
        modalCb = new JCheckBox();
    }

    
    private void layoutComponents() {
        JPanel basic = null;
        JScrollPane pane;
        if (panelMap.containsKey("group.BASIC")) {
            basic = panelMap.get("group.BASIC");
        } else {
            basic = new JPanel();
            basic.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
            panelMap.put("group.BASIC", basic);
            pane = new JScrollPane(basic);
            this.addTab(Intl.getValue("group.BASIC"), pane);
        }
        basic.add(modalLb);
        basic.add(modalCb);

    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        VisibleClass visibleClass = (VisibleClass) visibleElement;
        modalCb.setSelected(visibleClass.isModal());
    }

    private void addActions() {
        modalCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox src = (JCheckBox) e.getSource();
                boolean value = src.isSelected();
                VisibleClass visibleClass = (VisibleClass) visibleElement;
                visibleClass.setModal(value);
                updatePreformed();
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
