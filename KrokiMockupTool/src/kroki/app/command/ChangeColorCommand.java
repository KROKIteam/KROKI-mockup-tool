/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kroki.profil.VisibleElement;

/**
 * Komanda promene boje grupi elemenata
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ChangeColorCommand implements Command {

    private List<VisibleElement> visibleElementList;
    private int what;
    private List<Color> oldColorList;
    private Color newColor;

    public ChangeColorCommand(List<VisibleElement> visibleElementList, int what, Color newColor) {

        this.what = what;
        this.newColor = newColor;
        this.visibleElementList = new ArrayList<VisibleElement>();
        this.visibleElementList.addAll(visibleElementList);
        this.oldColorList = new ArrayList<Color>();
        for (int i = 0; i < visibleElementList.size(); i++) {
            if (what == 0) {
                oldColorList.add(visibleElementList.get(i).getComponent().getFgColor());
            }
            if (what == 1) {
                oldColorList.add(visibleElementList.get(i).getComponent().getBgColor());
            }
        }

    }

    public void doCommand() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            if (what == 0) {
                visibleElement.getComponent().setFgColor(newColor);
            }
            if (what == 1) {
                visibleElement.getComponent().setBgColor(newColor);
            }
        }
    }

    public void undoCommand() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            if (what == 0) {
                visibleElement.getComponent().setFgColor(oldColorList.get(i));
            }
            if (what == 1) {
                visibleElement.getComponent().setBgColor(oldColorList.get(i));
            }
        }
    }
}
