package graphedit.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceLoader {
	
	private final String imagesPath = "/icons/";
	
	public ImageIcon loadImageIcon(String name){
		InputStream is = this.getClass().getResourceAsStream(imagesPath +name);
		ImageIcon ico = null;
		try {
			ico = new ImageIcon(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ico;
	}
	
	public Image loadImage(String name){
		InputStream is = this.getClass().getResourceAsStream(imagesPath +name);
		Image img = null;
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	
}
