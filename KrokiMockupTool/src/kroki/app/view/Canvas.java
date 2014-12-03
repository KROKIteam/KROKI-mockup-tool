/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import kroki.api.profil.panel.StandardPanelUtil;
import kroki.app.command.CommandManager;
import kroki.app.model.SelectionModel;
import kroki.profil.VisibleElement;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Canvas extends JPanel {

    private Rectangle2D resizeRec;
    /**Pravougaonik selekcije*/
    private Rectangle2D selectionRectangle;
    /**Indikator koji govori da li je pravougaonik selekcije vidljiv ili ne*/
    private boolean showSelectionRectangle;
    private VisibleClass visibleClass;
    private SelectionModel selectionModel;
    private CommandManager commandManager;

    public Canvas(VisibleClass visibleClass) {
        this.visibleClass = visibleClass;
        this.setBackground(Color.WHITE);
        this.selectionModel = new SelectionModel();
        this.commandManager = new CommandManager();
        this.selectionRectangle = new Rectangle2D.Float();
        this.showSelectionRectangle = false;
    }

    public Canvas() {
        this.setBackground(Color.WHITE);
        try {
            createNewStandardPanel();
        } catch (Exception ex) {
            Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.selectionRectangle = new Rectangle2D.Float();
        this.selectionModel = new SelectionModel();
        this.commandManager = new CommandManager();
        this.showSelectionRectangle = false;
    }

    public final void createNewStandardPanel() throws Exception {
        visibleClass = new StandardPanel();
        StandardPanelUtil.defaultGuiSettings((StandardPanel)visibleClass);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        visibleClass.getComponent().getElementPainter().paint(g);
//        if (visibleClass.getLabel() != null) {
//            int x = visibleClass.getComponent().getAbsolutePosition().x;
//            int y = visibleClass.getComponent().getAbsolutePosition().y;
//
//            Insets insets = visibleClass.getComponent().getInsets();
//            x += insets.left;
//            y += insets.top - 10;
//            g2d.setPaint(visibleClass.getComponent().getFgColor());
//            g2d.setFont(new Font("Arial", Font.BOLD, 20));
//            g2d.drawString(visibleClass.getLabel(), x, y);
//        }

        Paint selection = new Color(255, 200, 0, 255);
        Paint selectionFill = new Color(255, 200, 0, 50);

        if (showSelectionRectangle) {
            g2d.setPaint(selectionFill);
            g2d.fill(selectionRectangle);
            g2d.setStroke(new BasicStroke(2));
            g2d.setPaint(selection);
            g2d.draw(selectionRectangle);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(selection);
        for (int i = 0; i < selectionModel.getSelectionNum(); i++) {
            VisibleElement el = selectionModel.getVisibleElementAt(i);
            Rectangle2D rec = el.getComponent().getBoundsForAbsolutePosition();
            g2d.draw(rec);
        }

        if (selectionModel.getSelectionNum() == 1) {
            VisibleElement el = selectionModel.getVisibleElementAt(0);
            int size = HandleManager.w;
            for (Handle h : Handle.values()) {
                Point p = HandleManager.getHandlePosition(el, h);
                g2d.fillRect((int) p.getX() - size / 2, (int) p.getY() - size / 2, size, size);
            }
        }

    }

    public void changeCursorImageAt(Point p) {
        Handle handle = null;

        for (int i = 0; i < selectionModel.getSelectionNum(); i++) {
            VisibleElement el = selectionModel.getVisibleElementAt(i);
            handle = HandleManager.getHandleForPoint(el, p);
            if (handle != null) {
                break;
            }
        }
        if (handle != null) {
            Cursor cursor = null;
            switch (handle) {
                case North:
                    cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                    break;
                case South:
                    cursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
                    break;
                case East:
                    cursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
                    break;
                case West:
                    cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
                    break;
                case SouthEast:
                    cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
                    break;
                case NorthWest:
                    cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
                    break;
                case SouthWest:
                    cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
                    break;
                case NorthEast:
                    cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
                    break;
            }
            setCursor(cursor);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Podešava atribute pravouganika selekcije
     * @param x x koordinata
     * @param y y koordinata
     * @param width širina
     * @param height visina
     */
    public void setSelectionRectangleBounds(int x, int y, int width, int height) {
        selectionRectangle.setRect(x, y, width, height);
    }

    /**
     * Podešava atribute pravouganika selekcije
     * @param x x koordinata
     * @param y y koordinata
     * @param width širina
     * @param height visina
     */
    public void setSelectionRectangleBounds(double x, double y, double width, double height) {
        selectionRectangle.setRect(x, y, width, height);
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public VisibleClass getVisibleClass() {
        return visibleClass;
    }

    public void setVisibleClass(VisibleClass visibleClass) {
        this.visibleClass = visibleClass;
    }

    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    public Rectangle2D getResizeRec() {
        return resizeRec;
    }

    public void setResizeRec(Rectangle2D resizeRec) {
        this.resizeRec = resizeRec;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Rectangle2D getSelectionRectangle() {
        return selectionRectangle;
    }

    public void setSelectionRectangle(Rectangle2D selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }

    public boolean isShowSelectionRectangle() {
        return showSelectionRectangle;
    }

    public void setShowSelectionRectangle(boolean showSelectionRectangle) {
        this.showSelectionRectangle = showSelectionRectangle;
    }
}
