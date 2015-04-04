package kroki.app.gui.settings;

import kroki.profil.VisibleElement;

/**
 * Settings interface. All settings classes implement it
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface Settings {

    public void updatePreformed();

    public void updatePreformedIncludeTree();

    public void updateSettings(VisibleElement visibleElement);
}
