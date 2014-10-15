/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kroki.app.gui.settings;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface SettingsCreator {
    /**Označava da se podesavanje izvrsilo. Kontejner koji sadrzi panele za podesavanja bi trebao da osvezi potrebne komponente nakon ove akcije*/
    public void settingsPreformed();
    /**Označava da se podesavanje izvrsilo. Kontejner koji sadrzi panele za podesavanja bi trebao da osvezi potrebne komponente nakon ove akcije ukljucujuci i drvo*/
    public void settingsPreformedIncludeTree();
}
