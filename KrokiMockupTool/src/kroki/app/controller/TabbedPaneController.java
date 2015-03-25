package kroki.app.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import kroki.app.gui.ButtonTabComponent;
import kroki.app.state.Context;
import kroki.app.view.Canvas;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.StandardPanelUtil;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TabbedPaneController extends AbstractController {

    /**Application's current context*/
    private Context context;
    /**Component to be controlled*/
    private JTabbedPane tabbedPane;
    /**Open tabs list*/
    private List<Canvas> canvasList;

    public TabbedPaneController(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        this.tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                int index = tabbedPane.getSelectedIndex();
                updateSettingsAt(index);
            }
        });


        this.context = new Context(this);
        this.canvasList = new ArrayList<Canvas>();
        tabbedPane.requestFocus();
        tabbedPane.addKeyListener(this);
    }

    /***************************************/
    /*PUBLIC METHODS*/
    /***************************************/
    /**
     * Opens new empty tab
     */
    public void openTab() {
        VisibleClass visibleClass = new StandardPanel();
        StandardPanelUtil.defaultGuiSettings((StandardPanel)visibleClass);
        visibleClass.setLabel("New standard panel");
        Canvas canvas = new Canvas(visibleClass);
        canvasList.add(canvas);

        canvas.setPreferredSize(new Dimension(2000, 2000));
        JScrollPane jsp = new JScrollPane();
        jsp.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        jsp.getViewport().add(canvas);

        int index = tabbedPane.getTabCount() - 1;
        tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(index);

        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    /**
     * Opens new tab for the specified visible class
     * @param visibleClass Visible class
     */
    public void openTab(VisibleClass visibleClass) {
        Canvas canvas = new Canvas(visibleClass);
        canvasList.add(canvas);

        canvas.setPreferredSize(new Dimension(2000, 10000));
        JScrollPane jsp = new JScrollPane();
        jsp.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        jsp.getViewport().add(canvas);

        tabbedPane.addTab(visibleClass.getLabel(), jsp);
        int index = tabbedPane.getTabCount() - 1;
        tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));

        tabbedPane.setSelectedIndex(index);

        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    /**
     * Closes tab which at position specified with the index
     * @param index Index
     */
    public void closeTab(int index) {
        canvasList.get(index).getSelectionModel().clearSelection();
        canvasList.remove(index);
        tabbedPane.remove(index);
    }

    /**
     * Returns component which is connected to the tab with the specified index
     * @param index Index
     * @return Canvas
     */
    public Canvas getTabContentAt(int index) {
        return canvasList.get(index);
    }

    /**
     * Return component which is connected to the selected tab
     * @return Canvas
     */
    public Canvas getCurrentTabContent() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1)
        	return canvasList.get(index);
        else 
        	return null;
    }

    /**
     * Returns index of the tab which contains the specified visible class
     * @param visibleClass Visible class
     * @return Index of the tab
     */
    public int getTabIndex(VisibleClass visibleClass) {
        for (int i = 0; i < canvasList.size(); i++) {
            Canvas canvas = getTabContentAt(i);
            if (canvas.getVisibleClass().equals(visibleClass)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if there is an open tab which contains the specified visible class
     * @param visibleClass Visible class
     * @return <code>true</code> if the tab is found, <code>false</code> otherwise
     */
    public boolean containsTab(VisibleClass visibleClass) {
        for (Canvas canvas : canvasList) {
            if (canvas.getVisibleClass().equals(visibleClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the tab's title
     * @param index Tab's index
     * @param title Tab's title
     */
    public void setTitleAt(int index, String title) {
        tabbedPane.setTitleAt(index, title);
    }

    /**
     * Set title of the tab which contains the specified visible class
     * @param visibleClass Visible class
     */
    public void setTitleAt(VisibleClass visibleClass) {
        int index = getTabIndex(visibleClass);
        tabbedPane.setTitleAt(index, visibleClass.getLabel());
    }

    /**
     * Selects tab with the specified index
     * @param index Tab's index
     */
    public void setCurrentTabIndex(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * Changes cursor image
     * @param image Cursor image
     */
    public void changeCursorImage(Image image) {
        Toolkit toolkit;
        Cursor cursor = null;
        if (image == null) {
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        } else {
            toolkit = Toolkit.getDefaultToolkit();
            cursor = toolkit.createCustomCursor(image, new Point(0, 0), "new cursor");
        }
        for (int i = 0; i < canvasList.size(); i++) {
            getTabContentAt(i).setCursor(cursor);
        }
    }

    /**
     * Refreshes the content of the tabbed pane
     */
    public void updateTabbedPane() {
        tabbedPane.updateUI();
    }

    /**
     * Updates settings of the tab with the specified index
     * @param index Index
     */
    public void updateSettingsAt(int index) {
        if (index >= canvasList.size() || index < 0) {
            return;
        }
        Canvas c = canvasList.get(index);
        c.getSelectionModel().fireUpdatePerformed();
    }

    /******************************************/
    /*ABSTRACT CONTROLLERA METHODS*/
    /******************************************/
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!tabbedPane.isFocusOwner()) {
            tabbedPane.requestFocus();
        }
        context.getCurrentState().mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        context.getCurrentState().mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        context.getCurrentState().mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        context.getCurrentState().mouseReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        context.getCurrentState().mousePressed(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        context.getCurrentState().keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        context.getCurrentState().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        context.getCurrentState().keyReleased(e);
    }

    /******************/
    /*GETTERS AND SETTERS*/
    /******************/
    public List<Canvas> getCanvasList() {
        return canvasList;
    }

    public void setCanvasList(List<Canvas> canvasList) {
        this.canvasList = canvasList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
