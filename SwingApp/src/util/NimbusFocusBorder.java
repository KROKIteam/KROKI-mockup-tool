package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;



public class NimbusFocusBorder implements javax.swing.border.Border {
    private static final RoundRectangle2D rect = new RoundRectangle2D.Float();
    private static final Area area = new Area();

    private final float arcIn;
    private final float arcOut;

    public NimbusFocusBorder() {
        arcIn = arcOut = 0f;
    }

    public NimbusFocusBorder( float rounded ) {
        arcIn  = rounded * 2;
        arcOut = arcIn + 2.8f;
    }

    public void paintBorder( Component c, Graphics _g, int x, int y, int w, int h ) {
        if( !c.hasFocus() ) return;
        final Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON );
        g.setColor( /* NimbusHelper. */ getFocusColor() );
        rect.setRoundRect( x + 0.6, y + 0.6, w - 1.2, h - 1.2, arcOut, arcOut );
        area.reset();
        area.add( new Area( rect ));
        rect.setRoundRect( x + 2, y + 2, w - 4, h - 4, arcIn, arcIn );
        area.subtract( new Area( rect ));
        g.fill( area );

    }

    public Insets getBorderInsets( Component c ) {
        return new Insets( 2, 2, 2, 2 );
    }

    public boolean isBorderOpaque() { return false; }

    // this actually looks up the color in UIDefaults
    // in the real implementation
    private Color getFocusColor() { return new Color( 115, 164, 209, 255 );}
}