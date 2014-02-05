package graphedit.util;

import graphedit.app.MainFrame;
import graphedit.gui.utils.Dialogs;
import graphedit.gui.utils.ExportDialog;
import graphedit.view.GraphEditView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class GraphicsExportUtility {
	
	private static GraphicsExportUtility instance;
	
	private static GraphEditView view = MainFrame.getInstance().getCurrentView();
	
	private static final String[] supportedTypes = {"png", "jpg"};
	
	private GraphicsExportUtility(){
	}
	
	public static GraphicsExportUtility getInstance(){
		if(instance == null){
			instance = new GraphicsExportUtility();
		}
		view = MainFrame.getInstance().getCurrentView();
		return instance;
	}
	
	public static String[] getSupportedTypes(){
		return supportedTypes;
	}
	
	public static boolean isSupportedType(String type){
		for(String supportedType : supportedTypes){
			if(supportedType.equalsIgnoreCase(type)){
				return true;
			}
		}
		return false;
	}
	
	public void export(){
		File file = ExportDialog.getFileName();
		if(file == null){
			return;
		}
		
		//xMin, xMax, yMin, yMax
		double[] modelBounds = view.getModelBounds();
		if(modelBounds == null){
			Dialogs.showErrorMessage("Your model is empty\nThere is nothing to draw", "Error: Empty Model");
			return;
		}
		
		BufferedImage image = new BufferedImage((int)(modelBounds[1] - modelBounds[0]+10),
				(int)(modelBounds[3]-modelBounds[2]+10), BufferedImage.TYPE_INT_ARGB);
		
		
		Graphics graphics = image.createGraphics();
		graphics.setColor(Color.white);
		graphics.translate((int)(-modelBounds[0]+5), (int)(-modelBounds[2]+5));
		graphics.fillRect((int)modelBounds[0]-10, (int)modelBounds[2]-10, image.getWidth()+10, image.getHeight()+10);
		
		
		
		try{
			view.paintToMyGraphics(graphics);
		}catch(Exception ex){
			Dialogs.showErrorMessage("Your model can't be exported","Error: Export Malfunction");
		}
		
		try {
		    file.createNewFile();
		    ImageIO.write(image, file.getName().substring(file.getName().length()-3), file);
		} catch (IOException e) {
		}
		
		graphics.dispose();
		image.flush();
	}

}
