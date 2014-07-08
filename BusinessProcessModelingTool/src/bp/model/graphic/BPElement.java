package bp.model.graphic;

import bp.model.graphic.util.IHandlers;
import bp.view.BasicPainter;

public abstract class BPElement extends BPGraphElement {

    public abstract BasicPainter getPainter();

    public abstract IHandlers getHandlers();
}
