package graphedit.util;

import graphedit.app.MainFrame;
import graphedit.gui.utils.Dialogs;
import graphedit.view.GraphEditView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class GraphicsExportUtility {

	private static GraphicsExportUtility instance;

	private static GraphEditView view = MainFrame.getInstance().getCurrentView();



	private JFileChooser jfc;

	private GraphicsExportUtility(){
		jfc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getWriterFormatNames());
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(filter);
	}

	public static GraphicsExportUtility getInstance(){
		if(instance == null){
			instance = new GraphicsExportUtility();
		}
		view = MainFrame.getInstance().getCurrentView();
		return instance;
	}



	public void export(){
		File file = null;
		if (jfc.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION){
			file = jfc.getSelectedFile();
			String path = file.getAbsolutePath();
			if (!path.matches("(?i).*\\.(jpg|jpeg|png|gif|bmp||wbmp)")){
				if (path.contains("."))
					path = path.substring(0,path.indexOf('.')-1);
				file = new File(path + "." + "jpg");
			}

			String extension = file.getName().substring(file.getName().indexOf('.')+1);
			
			//xMin, xMax, yMin, yMax
			double[] modelBounds = view.getModelBounds();
			if(modelBounds == null){
				Dialogs.showErrorMessage("Your model is empty\nThere is nothing to draw", "Error: Empty Model");
				return;
			}

			BufferedImage image = new BufferedImage((int)(modelBounds[1] - modelBounds[0]+10),
					(int)(modelBounds[3]-modelBounds[2]+10), BufferedImage.TYPE_INT_BGR);

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
				ImageIO.write(image, extension, file);
			} catch (IOException e) {
			}

			graphics.dispose();
			image.flush();
		}
	}

}
