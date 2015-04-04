package kroki.app.event;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Listener interface
 */
public interface UpdateListener extends EventListener {
    public void updatePerformed(EventObject e);
}
