package bp.model.graphic.util;

import java.awt.Point;

public interface IHandlers {

    public boolean isHandlerAt(Point p);

    public Handler getHandlerAt(Point p);

    public void updateHandlers();
}
