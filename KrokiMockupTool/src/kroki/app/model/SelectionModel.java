/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import kroki.app.KrokiMockupToolApp;
import kroki.app.event.SelectionModelUpdateEvent;
import kroki.app.event.UpdateListener;
import kroki.app.gui.settings.SettingsFactory;
import kroki.mockup.model.Component;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SelectionModel {

    private List<VisibleElement> visibleElementList;
    private List<VisibleElement> temporaryDeselected;
    private List<UpdateListener> listenerList;
    private SelectionModelUpdateEvent updateEvent;

    public SelectionModel() {
        visibleElementList = new ArrayList<VisibleElement>();
        temporaryDeselected = new ArrayList<VisibleElement>();
        listenerList = new ArrayList<UpdateListener>();
        listenerList.add((UpdateListener) new SettingsFactory());
        listenerList.add(KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar());
    }

    public void addToSelection(VisibleElement visibleElement) {
        if (visibleElementList.contains(visibleElement)) {
            visibleElementList.remove(visibleElement);
        } else {
            visibleElementList.add(visibleElement);
        }
        fireUpdatePerformed();
    }

    public void addToSelection(ArrayList<VisibleElement> visibleElementList) {
        this.visibleElementList.addAll(visibleElementList);
        fireUpdatePerformed();
    }

    public void removeFromSelection(VisibleElement visibleElement) {
        if (visibleElementList.contains(visibleElement)) {
            visibleElementList.remove(visibleElement);
        }
        fireUpdatePerformed();
    }

    public void removeFromSelection(List<VisibleElement> visibleElementList) {
        for (VisibleElement el : visibleElementList) {
            if (this.visibleElementList.contains(el)) {
                this.visibleElementList.remove(el);
            }
        }
        fireUpdatePerformed();
    }

    public int getSelectionNum() {
        return visibleElementList.size();
    }

    public void clearSelection() {
        visibleElementList.clear();
        fireUpdatePerformed();
    }

    /**
     * Vraća najmanji pravougaonik ( {@link Rectangle2D} ) koji sadrži sve selektovane elemente.
     * @return
     */
    public Rectangle2D getSelectionBounds() {
        Rectangle2D rec = null;
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement el = visibleElementList.get(i);
            Rectangle2D elRec = el.getComponent().getBoundsForAbsolutePosition();
            if (rec == null) {
                rec = (Rectangle2D) elRec.clone();
            } else {
                rec.add(elRec);
            }
        }
        return rec;
    }

    /**
     * Metoda koja priprema grupu elemenata za pomeranje.
     * <i>
     * Izvršava deselekciju svih elemenata unutar grupe (ukoliko je neki od njih selektovan). Deselektovane elemente stavlja u privremenu listu koja se nakon
     * završetka pomeranja prazni a njen sadržaj opet prebacuje u listu selektovanih elemenata.
     * </i>
     * @param elGroup Grupa elemenata koju je potrebno 
     */
    public void prepareForMove(ElementsGroup elGroup) {
        for (int j = 0; j < elGroup.getVisibleElementsNum(); j++) {
            VisibleElement subEl = elGroup.getVisibleElementAt(j);
            if (visibleElementList.contains(subEl)) {
                temporaryDeselected.add(subEl);
                visibleElementList.remove(subEl);
            }
            if (subEl instanceof ElementsGroup) {
                prepareForMove((ElementsGroup) subEl);
            }
        }
    }

    public boolean prepareForMove() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement el = visibleElementList.get(i);
            if (el instanceof ElementsGroup) {
                ElementsGroup eg = (ElementsGroup) el;
                for (int j = 0; j < eg.getVisibleElementsNum(); j++) {
                    VisibleElement subEl = eg.getVisibleElementAt(j);
                    if (visibleElementList.contains(subEl)) {
                        temporaryDeselected.add(subEl);
                        visibleElementList.remove(subEl);
                    }
                    if (subEl instanceof ElementsGroup) {
                        prepareForMove((ElementsGroup) subEl);
                    }
                }
            }
        }
        //provera da li komponente pripadaju istim roditeljima (grupama elemenata)
        Component firstComponent;
        if (visibleElementList.size() > 1) {
            VisibleElement first = visibleElementList.get(0);
            firstComponent = first.getComponent().getParent();
            for (int i = 1; i < visibleElementList.size(); i++) {
                VisibleElement el = visibleElementList.get(i);
                Component otherParent = el.getComponent().getParent();
                if (!otherParent.equals(firstComponent)) {
                    finishMove();
                    return false;
                }
            }
        }
        return true;
    }

    public Point2D getRelativePositionFor(double x, double y) {
        Component firstComponent = null;
        if (visibleElementList.size() >= 1) {
            firstComponent = visibleElementList.get(0).getComponent().getParent();
        }
        if (firstComponent == null) {
            return new Point2D.Double(x, y);
        } else {
            double newX = x - firstComponent.getAbsolutePosition().getX();
            double newY = y - firstComponent.getAbsolutePosition().getY();
            return new Point2D.Double(newX, newY);
        }
    }

    public void finishMove() {
        visibleElementList.addAll(temporaryDeselected);
        temporaryDeselected.clear();
        fireUpdatePerformed();
    }

    /**
     * Javljamo svim listenerima da se dogadjaj desio
     */
    public void fireUpdatePerformed() {
        // Guaranteed to return a non-null array
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = 0; i < listenerList.size(); i++) {
            // Lazily create the event:
            if (updateEvent == null) {
                updateEvent = new SelectionModelUpdateEvent(this);
            }
            ((UpdateListener) listenerList.get(i)).updatePerformed(updateEvent);
        }

    }

    public boolean isSelected(VisibleElement visibleElement) {
        return visibleElementList.contains(visibleElement);
    }

    public VisibleElement getVisibleElementAt(int index) {
        return visibleElementList.get(index);
    }

    public List<UpdateListener> getListenerList() {
        return listenerList;
    }

    public void setListenerList(List<UpdateListener> listenerList) {
        this.listenerList = listenerList;
    }

    public List<VisibleElement> getTemporaryDeselected() {
        return temporaryDeselected;
    }

    public void setTemporaryDeselected(List<VisibleElement> temporaryDeselected) {
        this.temporaryDeselected = temporaryDeselected;
    }

    public SelectionModelUpdateEvent getUpdateEvent() {
        return updateEvent;
    }

    public void setUpdateEvent(SelectionModelUpdateEvent updateEvent) {
        this.updateEvent = updateEvent;
    }

    public List<VisibleElement> getVisibleElementList() {
        return visibleElementList;
    }

    public void setVisibleElementList(List<VisibleElement> visibleElementList) {
        this.visibleElementList = visibleElementList;
    }
}
