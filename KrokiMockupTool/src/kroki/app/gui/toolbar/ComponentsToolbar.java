/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.toolbar;

import javax.swing.JButton;
import javax.swing.JToolBar;
import kroki.app.action.mockup.AddCheckBoxAction;
import kroki.app.action.mockup.AddComboBoxAction;
import kroki.app.action.mockup.AddGroupBoxAction;
import kroki.app.action.mockup.AddLinkAction;
import kroki.app.action.mockup.AddRadioButtonAction;
import kroki.app.action.mockup.AddReportAction;
import kroki.app.action.mockup.AddTextAreaAction;
import kroki.app.action.mockup.AddTextFieldAction;
import kroki.app.action.mockup.AddTransactionAction;
import kroki.app.action.mockup.TransformToAggregatedAction;
import kroki.app.action.mockup.TransformToCalculatedAction;
import kroki.app.action.mockup.TransformToCombozoomAction;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@Deprecated
public class ComponentsToolbar extends JToolBar {

    public ComponentsToolbar() {
        setOrientation(JToolBar.VERTICAL);
        initComponents();
    }

    private void initComponents() {

        JButton btn1 = new JButton(new AddGroupBoxAction());
        add(btn1);
        addSeparator();

        JButton btn2 = new JButton(new AddTextAreaAction());
        add(btn2);
        JButton btn3 = new JButton(new AddTextFieldAction());
        add(btn3);
        JButton btn4 = new JButton(new AddComboBoxAction());
        add(btn4);
        addSeparator();

        JButton btn5 = new JButton(new AddRadioButtonAction());
        add(btn5);
        JButton btn6 = new JButton(new AddCheckBoxAction());
        add(btn6);

        JButton btn12 = new JButton(new AddLinkAction());
        add(btn12);
        addSeparator();

        JButton btn7 = new JButton(new AddReportAction());
        add(btn7);
        JButton btn8 = new JButton(new AddTransactionAction());
        add(btn8);
        addSeparator();

        JButton btn9 = new JButton(new TransformToCombozoomAction());
        add(btn9);

        JButton btn10 = new JButton(new TransformToAggregatedAction());
        add(btn10);

        JButton btn11 = new JButton(new TransformToCalculatedAction());
        add(btn11);


    }
}
