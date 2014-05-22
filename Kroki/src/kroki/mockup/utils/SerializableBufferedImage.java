/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SerializableBufferedImage implements Serializable {

   transient BufferedImage image;
    
    public SerializableBufferedImage(){
    }

    public SerializableBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        image = new BufferedImage(cm, raster, isRasterPremultiplied, properties);
    }

    public SerializableBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
        image = new BufferedImage(width, height, imageType, cm);
    }

    public SerializableBufferedImage(int width, int height, int imageType) {
        image = new BufferedImage(width, height, imageType);
    }

    public SerializableBufferedImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getSource() {
        return image;
    }

    public void setSource(BufferedImage image) {
        this.image = image;
    }

//    private void writeObject(ObjectOutputStream out) throws IOException {
//        ImageIO.write(image, "png", out);
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        image = ImageIO.read(in);
//    }
}
