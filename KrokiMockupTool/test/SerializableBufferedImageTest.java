
import java.awt.image.BufferedImage;
import kroki.common.copy.DeepCopy;
import kroki.mockup.utils.SerializableBufferedImage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SerializableBufferedImageTest {

    public static void main(String[] args) {

        SerializableBufferedImage sbi = new SerializableBufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);

        System.out.println("sbi:" + sbi);

        SerializableBufferedImage clone = (SerializableBufferedImage) DeepCopy.copy(sbi);
        System.out.println("Buffered image is cloned!");
        System.out.println("clone: " + clone);

    }
}
