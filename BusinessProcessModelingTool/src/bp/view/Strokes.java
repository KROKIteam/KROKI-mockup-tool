package bp.view;

import java.awt.BasicStroke;
import java.awt.Stroke;

public class Strokes {

    public static final Float MITER_LIMIT = 10.0f;
    public static final Float DASH_PHASE = 0.0f;
    public static final float DASH_PATTERN[] = {10.0f};

    public static final Float THIN_LINE = 4.0f;
    public static final Float THICK_LINE = 12.0f;
    public static final Float EDGE_LINE = 2.0f;

    public static Stroke getLine(Float width) {
        return new BasicStroke(width);
    }

    public static Stroke getDashedLine(Float width) {
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, MITER_LIMIT, DASH_PATTERN,
                DASH_PHASE);
    }
}
