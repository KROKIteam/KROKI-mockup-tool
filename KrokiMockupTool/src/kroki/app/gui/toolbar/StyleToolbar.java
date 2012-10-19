/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.toolbar;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.util.Enumeration;
import java.util.EventObject;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import kroki.app.KrokiMockupToolApp;
import kroki.app.action.style.AlignCenterAction;
import kroki.app.action.style.AlignLeftAction;
import kroki.app.action.style.AlignRightAction;
import kroki.app.action.style.BorderLineAction;
import kroki.app.action.style.BorderTitleAction;
import kroki.app.action.style.FontChangeAction;
import kroki.app.action.style.FontSizeChange;
import kroki.app.action.style.LayoutFreeAction;
import kroki.app.action.style.LayoutHorizontalAction;
import kroki.app.action.style.LayoutVerticalAction;
import kroki.app.action.style.OpenBgColorPopup;
import kroki.app.action.style.OpenFgColorPopup;
import kroki.app.event.UpdateListener;
import kroki.app.model.SelectionModel;
import kroki.mockup.model.Border;
import kroki.mockup.model.Composite;
import kroki.mockup.model.border.LineBorder;
import kroki.mockup.model.border.TitledBorder;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupOrientation;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StyleToolbar extends JToolBar implements UpdateListener {

    /**************/
    /**komponente**/
    /**************/
    private JComboBox fontCb;
    private JComboBox fontSizeCb;
    private ButtonGroup layoutBg;
    private JToggleButton layoutHorizontalTb;
    private JToggleButton layoutVerticalTb;
    private JToggleButton layoutFreeTb;
    private ButtonGroup alignBg;
    private JToggleButton alignLeftTb;
    private JToggleButton alignCenterTb;
    private JToggleButton alignRightTb;
    private ButtonGroup borderBg;
    private JToggleButton borderTitledTb;
    private JToggleButton borderLineTb;
    private JButton bgColorBtn;
    private JButton fgColorBtn;

    public StyleToolbar() {
        setOrientation(JToolBar.HORIZONTAL);
        initComponents();
    }

    private void initComponents() {
        //kombo box sa fontovima
        String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontCb = new JComboBox(fontList);
        fontCb.setAction(new FontChangeAction());
        fontCb.setPreferredSize(new Dimension(150, 20));
        fontCb.setMaximumSize(fontCb.getPreferredSize());

        //kombo box sa velicinama fonta
        String[] sizes = {"8", "10", "12", "14", "16", "18", "24", "36", "48", "72"};
        fontSizeCb = new JComboBox(sizes);
        fontSizeCb.setEditable(false);
        fontSizeCb.setAction(new FontSizeChange());
        fontSizeCb.setPreferredSize(new Dimension(50, 20));
        fontSizeCb.setMaximumSize(fontSizeCb.getPreferredSize());

        //colors
        bgColorBtn = new JButton();
        bgColorBtn.setAction(new OpenBgColorPopup());

        fgColorBtn = new JButton();
        fgColorBtn.setAction(new OpenFgColorPopup());

        //layout

        layoutBg = new ButtonGroup();

        layoutHorizontalTb = new JToggleButton();
        layoutHorizontalTb.setAction(new LayoutHorizontalAction());

        layoutVerticalTb = new JToggleButton();
        layoutVerticalTb.setAction(new LayoutVerticalAction());


        layoutFreeTb = new JToggleButton();
        layoutFreeTb.setAction(new LayoutFreeAction());

        layoutBg.add(layoutHorizontalTb);
        layoutBg.add(layoutVerticalTb);
        layoutBg.add(layoutFreeTb);

        //alignment
        alignBg = new ButtonGroup();

        alignLeftTb = new JToggleButton();
        alignLeftTb.setAction(new AlignLeftAction());

        alignCenterTb = new JToggleButton();
        alignCenterTb.setAction(new AlignCenterAction());

        alignRightTb = new JToggleButton();
        alignRightTb.setAction(new AlignRightAction());

        alignBg.add(alignLeftTb);
        alignBg.add(alignCenterTb);
        alignBg.add(alignRightTb);

        //border
        borderBg = new ButtonGroup();

        borderTitledTb = new JToggleButton();
        borderTitledTb.setAction(new BorderTitleAction());

        borderLineTb = new JToggleButton();
        borderLineTb.setAction(new BorderLineAction());

        borderBg.add(borderTitledTb);
        borderBg.add(borderLineTb);

        add(fontCb);
        add(fontSizeCb);
        addSeparator();
        add(bgColorBtn);
        add(fgColorBtn);
        addSeparator();
        add(layoutHorizontalTb);
        add(layoutVerticalTb);
        add(layoutFreeTb);
        addSeparator();
        add(alignLeftTb);
        add(alignCenterTb);
        add(alignRightTb);
        addSeparator();
        add(borderTitledTb);
        add(borderLineTb);

        disableAllToggles();
    }

    public void updateAllToggles(ElementsGroup elementsGroup) {
        GroupOrientation groupOrientation = elementsGroup.getGroupOrientation();
        if (groupOrientation != null) {
            switch (groupOrientation) {
                case horizontal: {
                    enableAlignToggles();
                    layoutHorizontalTb.setSelected(true);
                }
                break;
                case vertical: {
                    enableAlignToggles();
                    layoutVerticalTb.setSelected(true);
                }
                break;
                case area: {
                    layoutFreeTb.setSelected(true);
                    disableAlignToggles();
                }
            }
        }
        GroupAlignment groupAlignment = elementsGroup.getGroupAlignment();
        if (groupAlignment != null) {
            switch (groupAlignment) {
                case left: {
                    alignLeftTb.setSelected(true);
                }
                break;
                case center: {
                    alignCenterTb.setSelected(true);
                }
                break;
                case right: {
                    alignRightTb.setSelected(true);
                }
                break;
            }
        }
        Border border = ((Composite) elementsGroup.getComponent()).getBorder();
        if (border != null) {
            if (border instanceof TitledBorder) {
                borderTitledTb.setSelected(true);
            } else if (border instanceof LineBorder) {
                borderLineTb.setSelected(true);
            }
        }
    }

    public void disableAllToggles() {
        layoutBg.clearSelection();
        Enumeration<AbstractButton> layoutBtns = layoutBg.getElements();
        while (layoutBtns.hasMoreElements()) {
            layoutBtns.nextElement().setEnabled(false);
        }

        alignBg.clearSelection();
        Enumeration<AbstractButton> alignBtns = alignBg.getElements();
        while (alignBtns.hasMoreElements()) {
            alignBtns.nextElement().setEnabled(false);
        }

        borderBg.clearSelection();
        Enumeration<AbstractButton> borderBtns = borderBg.getElements();
        while (borderBtns.hasMoreElements()) {
            borderBtns.nextElement().setEnabled(false);
        }
    }

    public void enableAllToggles() {

        Enumeration<AbstractButton> layoutBtns = layoutBg.getElements();
        while (layoutBtns.hasMoreElements()) {
            layoutBtns.nextElement().setEnabled(true);
        }
        layoutBg.clearSelection();


        Enumeration<AbstractButton> alignBtns = alignBg.getElements();
        while (alignBtns.hasMoreElements()) {
            alignBtns.nextElement().setEnabled(true);
        }
        alignBg.clearSelection();


        Enumeration<AbstractButton> borderBtns = borderBg.getElements();
        while (borderBtns.hasMoreElements()) {
            borderBtns.nextElement().setEnabled(true);
        }
        borderBg.clearSelection();
    }

    public void disableAlignToggles() {
        alignBg.clearSelection();
        Enumeration<AbstractButton> alignBtns = alignBg.getElements();
        while (alignBtns.hasMoreElements()) {
            alignBtns.nextElement().setEnabled(false);
        }
    }

    public void enableAlignToggles() {
        Enumeration<AbstractButton> alignBtns = alignBg.getElements();
        while (alignBtns.hasMoreElements()) {
            alignBtns.nextElement().setEnabled(true);
        }
        alignBg.clearSelection();
    }

    public void enableLayoutToggles() {
        Enumeration<AbstractButton> layoutBtns = layoutBg.getElements();
        while (layoutBtns.hasMoreElements()) {
            layoutBtns.nextElement().setEnabled(true);
        }
        layoutBg.clearSelection();
    }

    public void disableLayoutToggles() {
        layoutBg.clearSelection();
        Enumeration<AbstractButton> layoutBtns = layoutBg.getElements();
        while (layoutBtns.hasMoreElements()) {
            layoutBtns.nextElement().setEnabled(false);
        }
    }

    public void enableBorderToggles() {
        Enumeration<AbstractButton> borderBtns = borderBg.getElements();
        while (borderBtns.hasMoreElements()) {
            borderBtns.nextElement().setEnabled(true);
        }
        borderBg.clearSelection();
    }

    public void disableBorderToggles() {
        borderBg.clearSelection();
        Enumeration<AbstractButton> borderBtns = borderBg.getElements();
        while (borderBtns.hasMoreElements()) {
            borderBtns.nextElement().setEnabled(false);
        }
    }

    public void updatePerformed(EventObject e) {
        SelectionModel selectionModel = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().getSelectionModel();
        if (selectionModel.getSelectionNum() == 1) {
            VisibleElement visibleElement = selectionModel.getVisibleElementAt(0);
            if (visibleElement instanceof ElementsGroup) {
                enableAllToggles();
                updateAllToggles((ElementsGroup) visibleElement);
            } else {
                disableAllToggles();
            }
        } else {
            disableAllToggles();
        }
        updateUI();
    }
}
