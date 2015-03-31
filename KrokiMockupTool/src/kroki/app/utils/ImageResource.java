package kroki.app.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

/**
 * Util class which returns a resource based on the given key
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ImageResource {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("kroki.resources.KrokiMockupToolApp");
    private static HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

    public static Image getImageResource(String key) {
        BufferedImage value = null;
        if (imageMap.containsKey(key)) {
            value = imageMap.get(key);
        } else {
            String url = resourceBundle.getString(key);
            if (url != null) {
                try {
                    value = ImageIO.read(ImageResource.class.getResource(url));
                    imageMap.put(key, value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;

    }
}
