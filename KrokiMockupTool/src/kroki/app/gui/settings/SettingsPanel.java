package kroki.app.gui.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which is added to each UI profile element in order to specify which settings panel
 * should be associated with it
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingsPanel {
    Class<?> value();
}
