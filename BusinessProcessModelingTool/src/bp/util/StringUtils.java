package bp.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class StringUtils {

    static {
        img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    };
    
    private static final BufferedImage img;
    
    public static Integer calculateStringWidth(Font font, String string) {
        if (font == null || string == null)
            return 0;
        
        Graphics g = img.getGraphics();
        g.setFont(font);
        return g.getFontMetrics().stringWidth(string);

    }
    
    public static Integer calculateStringHeight(Font font, String string) {
        if (font == null || string == null)
            return 0;

        Graphics g = img.getGraphics();
        g.setFont(font);
        return g.getFontMetrics().getHeight();
    }
}
