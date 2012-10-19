/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TabbedPaneController extends AbstractController {

    /**Kontekst u kojem se nalazi aplikacija*/
    Context context;
    /**Komponenta koju je potrebno kontrolisati*/
    JTabbedPane tabbedPane;
    /**Lista otvorenih tabova*/
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
    /*JAVNE METODE ZA RAD SA TABBED PANE-om*/
    /***************************************/
    /**
     * Otvara novi tab prazan tab.
     */
    public void openTab() {
        VisibleClass visibleClass = new StandardPanel();
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
     * Otvara novi tab za odredjenu vidljivu klasu
     * @param visibleClass vidljiva klasa.
     */
    public void openTab(VisibleClass visibleClass) {
        Canvas canvas = new Canvas(visibleClass);
        canvasList.add(canvas);

        canvas.setPreferredSize(new Dimension(2000, 2000));
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
     * Zatvara tab koji se nalazi na poziciji označenoj prosleđenim indeksom.
     * @param index indeks.
     */
    public void closeTab(int index) {
        canvasList.get(index).getSelectionModel().clearSelection();
        canvasList.remove(index);
        tabbedPane.remove(index);
    }

    /**
     * Vraća komponentu koja je pridružena tabu sa prosleđenim indeksom.
     * @param index indeks
     * @return Canvas
     */
    public Canvas getTabContentAt(int index) {
        return canvasList.get(index);
    }

    /**
     * Vraća komponentu pridruženu tabu koji je trenutno selektovan
     * @return Canvas
     */
    public Canvas getCurrentTabContent() {
        int index = tabbedPane.getSelectedIndex();
        return canvasList.get(index);
    }

    /**
     * Vraća indeks taba koji u sebi sadrži prosleđenu vidljivu klasu
     * @param visibleClass vidljiva klasa
     * @return indeks
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
     * Proverava da li postoji otvoreni tab koji u sebi sadrži vidljivu klasu
     * @param visibleClass vidljiva klasa
     * @return true/false
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
     * Podešava naslov u zaglavlju taba
     * @param index indeks taba
     * @param title naslov
     */
    public void setTitleAt(int index, String title) {
        tabbedPane.setTitleAt(index, title);
    }

    /**
     * Podešava naslov taba koji u sebi ima vidljivu klasu
     * @param visibleClass vidljiva klasa
     */
    public void setTitleAt(VisibleClass visibleClass) {
        int index = getTabIndex(visibleClass);
        tabbedPane.setTitleAt(index, visibleClass.getLabel());
    }

    /**
     * Podešava trenutno selektovani tab na tab sa prosleđenim indeksom
     * @param index indeks
     */
    public void setCurrentTabIndex(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * Menja ikonicu kursora u prosleđenu
     * @param image ikonica
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
     * Osvežava sadržaj tabbed pane-a
     */
    public void updateTabbedPane() {
        tabbedPane.updateUI();
    }

    public void updateSettingsAt(int index) {
        if (index >= canvasList.size() || index < 0) {
            return;
        }
        Canvas c = canvasList.get(index);
        c.getSelectionModel().fireUpdatePerformed();
    }

    /******************************************/
    /*METODE NASLEĐENE OD ABSTRACT CONTROLLERA*/
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
    /*GETERI I SETTERI*/
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
