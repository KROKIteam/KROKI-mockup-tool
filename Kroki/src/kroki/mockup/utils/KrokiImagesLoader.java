/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Utils class containing a method for loading images
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class KrokiImagesLoader {

    private static ResourceBundle rb = ResourceBundle.getBundle("kroki.resources.KrokiImages");

    public static BufferedImage loadImage(String alias) {
        String url = rb.getString(alias);
        BufferedImage im = null;
        try {
            im = ImageIO.read(KrokiImagesLoader.class.getResource(url));
        } catch (IOException ex) {
            Logger.getLogger(KrokiImagesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return im;
    }
}
