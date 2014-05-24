/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SerializableBufferedImage implements Serializable {

	transient Image image;
	private int width;
	private int height;
	private int imageType;
	private String alias;

	public SerializableBufferedImage(){
	}


	public SerializableBufferedImage(int width, int height, int imageType) {
		image = new BufferedImage(width, height, imageType);
		this.width = width;
		this.height = height;
		this.imageType = imageType;
	}


	public SerializableBufferedImage(String alias){
		image = KrokiImagesLoader.loadImage(alias);
		this.alias = alias;
	}

	public BufferedImage getSource() {
		if (image == null){
			if (alias == null)	
				image = new BufferedImage(width, height, imageType);
			else
				image = KrokiImagesLoader.loadImage(alias);
		}
		return (BufferedImage) image;
	}

	public void setSource(BufferedImage image) {
		this.image = image;
	}


}
