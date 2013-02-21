/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui;

import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import kroki.app.action.mockup.AddCheckBoxAction;
import kroki.app.action.mockup.AddComboBoxAction;
import kroki.app.action.mockup.AddGroupBoxAction;
import kroki.app.action.mockup.AddHierarchyAction;
import kroki.app.action.mockup.AddLinkAction;
import kroki.app.action.mockup.AddRadioButtonAction;
import kroki.app.action.mockup.AddReportAction;
import kroki.app.action.mockup.AddTextAreaAction;
import kroki.app.action.mockup.AddTextFieldAction;
import kroki.app.action.mockup.AddTransactionAction;
import kroki.app.action.mockup.TransformToAggregatedAction;
import kroki.app.action.mockup.TransformToCalculatedAction;
import kroki.app.action.mockup.TransformToCombozoomAction;
import net.miginfocom.swing.MigLayout;

/**
 * Klasa koja predstavlja paletu alatki
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Palette extends JPanel {

    private JButton addPanelBtn;
    private JButton addTextFieldBtn;
    private JButton addTextAreaBtn;
    private JButton addComboBoxBtn;
    private JButton addRadioButtonBtn;
    private JButton addCheckBoxBtn;
    private JButton addLinkBtn;
    private JButton addReportBtn;
    private JButton addTransactionBtn;
    private JButton addHierarchyBtn;
    private JButton transformToCombozoomBtn;
    private JButton transformToAggregatedBtn;
    private JButton transformToCalculatedBtn;
    private JLabel componentsLbl;
    private JLabel actionsLbl;

    public Palette() {
        LayoutManager layoutManager = new MigLayout("wrap", "[grow, fill]");
        this.setLayout(layoutManager);
        initComponents();
    }

    private void initComponents() {
        addPanelBtn = new JButton(new AddGroupBoxAction());
        addPanelBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addPanelBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addTextFieldBtn = new JButton(new AddTextFieldAction());
        addTextFieldBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addTextFieldBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addTextAreaBtn = new JButton(new AddTextAreaAction());
        addTextAreaBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addTextAreaBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addComboBoxBtn = new JButton(new AddComboBoxAction());
        addComboBoxBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addComboBoxBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addRadioButtonBtn = new JButton(new AddRadioButtonAction());
        addRadioButtonBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addRadioButtonBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addCheckBoxBtn = new JButton(new AddCheckBoxAction());
        addCheckBoxBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addCheckBoxBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addLinkBtn = new JButton(new AddLinkAction());
        addLinkBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addLinkBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addReportBtn = new JButton(new AddReportAction());
        addReportBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addReportBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addTransactionBtn = new JButton(new AddTransactionAction());
        addTransactionBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addTransactionBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        addHierarchyBtn = new JButton(new AddHierarchyAction());
        addHierarchyBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addHierarchyBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        transformToAggregatedBtn = new JButton(new TransformToAggregatedAction());
        transformToAggregatedBtn.setHorizontalAlignment(SwingConstants.LEFT);
        transformToAggregatedBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        transformToCalculatedBtn = new JButton(new TransformToCalculatedAction());
        transformToCalculatedBtn.setHorizontalAlignment(SwingConstants.LEFT);
        transformToCalculatedBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        transformToCombozoomBtn = new JButton(new TransformToCombozoomAction());
        transformToCombozoomBtn.setHorizontalAlignment(SwingConstants.LEFT);
        transformToCombozoomBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        componentsLbl = new JLabel("Components:");
        actionsLbl = new JLabel("Actions:");

        add(componentsLbl);
        add(addPanelBtn);
        add(addTextFieldBtn);
        add(addTextAreaBtn);
        add(addComboBoxBtn);
        //add(addRadioButtonBtn);
        add(addCheckBoxBtn);
        add(addReportBtn);
        add(addTransactionBtn);
        add(addLinkBtn);
        add(addHierarchyBtn);
        add(actionsLbl);
        add(transformToAggregatedBtn);
        add(transformToCalculatedBtn);
        add(transformToCombozoomBtn);
    }
}
