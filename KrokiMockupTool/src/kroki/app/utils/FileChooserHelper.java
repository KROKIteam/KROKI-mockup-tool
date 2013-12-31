package kroki.app.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooserHelper {

	private static String extension(File file)
	{
		int index=file.getName().lastIndexOf(".")+1;
		if(index<=0)
			return null;
		return file.getName().substring(index);
	}
	
	public static File fileChooser(JFrame frame,boolean saveDialog,String windowName,String filterName,String filterExtension)
	{
		JFileChooser fileChooser=new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		//fileChooser.setFileFilter(new CustomFileFilter(fileExtension));
		fileChooser.setFileFilter(new FileNameExtensionFilter(filterName,filterExtension));
		File file = null;
		if(fileChooser.showDialog(frame, windowName)==JFileChooser.APPROVE_OPTION)
		{
			
			String ext=extension(fileChooser.getSelectedFile());
			String message=null;
			if(saveDialog)
				message=windowName.toLowerCase()+" to";
			else
				message=windowName.toLowerCase()+" from";
			if(ext==null)
			{
				if(saveDialog)
				{
					String fileName=fileChooser.getSelectedFile().getAbsolutePath();
					file=new File(fileName+"."+filterExtension);
				}else
					JOptionPane.showMessageDialog(frame, "Choose file with extension\n"+filterExtension+"\n to "+message+"!", "Eror choosing file to "+message, JOptionPane.ERROR_MESSAGE);
			}
			else if(!filterExtension.equals(ext))
			{
				JOptionPane.showMessageDialog(frame, "Choose file with extension\n"+filterExtension+"\n to "+message+"!", "Eror choosing file to "+message, JOptionPane.ERROR_MESSAGE);
			}else
				file=fileChooser.getSelectedFile();
			
		}
		return file;
	}
}
