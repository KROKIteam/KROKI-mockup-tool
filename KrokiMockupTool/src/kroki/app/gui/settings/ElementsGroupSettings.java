package kroki.app.gui.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kroki.intl.Intl;
import kroki.mockup.model.Composite;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupOrientation;

/**
 * Tabbed pane showing elements group settings
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ElementsGroupSettings extends VisibleElementSettings {

	private static final long serialVersionUID = 1L;
	
    protected JLabel borderColorLb;
    protected JButton borderColorBtn;
    protected JLabel groupOrientationLb;
    protected JComboBox groupOrientationCmb;
    protected JLabel groupAlignmentLb;
    protected JComboBox groupAlignmentCmb;
    protected JLabel groupLocationLb;
    protected JTextField groupLocationTf;

    public ElementsGroupSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        borderColorLb = new JLabel();
        borderColorLb.setText(Intl.getValue("border.color"));

        borderColorBtn = new JButton();
        borderColorBtn.setPreferredSize(new Dimension(20, 20));
        borderColorBtn.setMaximumSize(fgColorBtn.getPreferredSize());

        groupOrientationLb = new JLabel();
        groupOrientationLb.setText(Intl.getValue("elementsGroup.groupOrientation"));

        groupOrientationCmb = new JComboBox(GroupOrientation.values());

        groupAlignmentLb = new JLabel();
        groupAlignmentLb.setText(Intl.getValue("elementsGroup.groupAlignment"));

        groupAlignmentCmb = new JComboBox(GroupAlignment.values());

        groupLocationLb = new JLabel();
        groupLocationLb.setText(Intl.getValue("elementsGroup.groupLocation"));

        groupLocationTf = new JTextField(30);
        groupLocationTf.setEditable(false);
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        ElementsGroup eg = (ElementsGroup) visibleElement;
        Composite c = (Composite) visibleElement.getComponent();
        borderColorBtn.setIcon(new ImageIcon(PlainColorImage.getImage(c.getBorder().getColor(), 16, 16)));
        groupOrientationCmb.setSelectedItem(eg.getGroupOrientation());
        groupAlignmentCmb.setSelectedItem(eg.getGroupAlignment());
        if(eg.getGroupLocation()!=null)
        	groupLocationTf.setText(eg.getGroupLocation().name());
    }

    private void layoutComponents() {
        JPanel basic = panelMap.get("group.BASIC");
        basic.add(borderColorLb, "shrink");
        basic.add(borderColorBtn);
        basic.add(groupOrientationLb);
        basic.add(groupOrientationCmb);
        basic.add(groupAlignmentLb);
        basic.add(groupAlignmentCmb);
        basic.add(groupLocationLb);
        basic.add(groupLocationTf);
    }

    private void addActions() {
        borderColorBtn.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                Composite c = (Composite) visibleElement.getComponent();
                Color color = JColorChooser.showDialog(null, "Choose color", c.getBorder().getColor());
                if (color != null) {
                    btn.setIcon(new ImageIcon(PlainColorImage.getImage(color, 16, 16)));
                    c.getBorder().setColor(color);
                    updatePreformed();
                }
            }
        });

        groupOrientationCmb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                GroupOrientation value = (GroupOrientation) comboBox.getSelectedItem();
                ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
                elementsGroup.setGroupOrientation(value);
                updatePreformed();
            }
        });

        groupAlignmentCmb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                GroupAlignment value = (GroupAlignment) comboBox.getSelectedItem();
                ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
                elementsGroup.setGroupAlignment(value);
                updatePreformed();
            }
        });

    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
    }
}
