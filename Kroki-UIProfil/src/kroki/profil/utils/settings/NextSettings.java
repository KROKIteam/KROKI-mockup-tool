/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Next;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
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
        autoActivateLb = new JLabel(Intl.getValue("next.autoActivate"));
        displayIdentifierLb = new JLabel(Intl.getValue("next.displayRepresentative"));
        displayRepresentativeLb = new JLabel(Intl.getValue("next.displayIdentifier"));
        autoActivateCb = new JCheckBox();
        displayIdentifierCb = new JCheckBox();
        displayRepresentativeCb = new JCheckBox();
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
