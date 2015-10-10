package kroki.app.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Class which is used to save projects.
 * Currently contains methods which use XStream, but which are not used
 * due to large file sizes, as well as methods which produce GZip files
 * @author Kroki Team
 *
 */
public class SaveUtil {

	public static boolean saveXStream(Object toSave, File file){
		try{
//			XStream xstream = new XStream(new StaxDriver());
//			setAliases(xstream);
//			BufferedWriter  out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
//			xstream.toXML(toSave, out);
//			out.close();
			
			XStream xstream = new XStream(new StaxDriver());
			setAliases(xstream);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			ObjectOutputStream out = xstream.createObjectOutputStream(writer);
			out.writeObject(toSave);
			out.close();
			writer.close();
			

		} catch (Exception e) { 
			e.printStackTrace();
			return false;
		}

		return true;
	}

	

	public static boolean  saveGZipObject(Object toSave, File file) {
		FileOutputStream fos = null;
		GZIPOutputStream gos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(file);
			gos = new GZIPOutputStream(fos);
			oos = new ObjectOutputStream(gos);
			oos.writeObject(toSave);
			oos.flush();
			oos.close();
			gos.close();
			fos.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Object loadGZipObject(File file) {
		Object obj = null;
		FileInputStream fis = null;
		GZIPInputStream gis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			gis = new GZIPInputStream(fis);
			ois = new ObjectInputStream(gis);
			obj = ois.readObject();
			ois.close();
			gis.close();
			fis.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}
	

	public static Object loadXStream(File file) {

		Object ret = null;
		try {
			
			XStream xstream = new XStream(new StaxDriver());
			setAliases(xstream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			//return xstream.fromXML(in);
			
			ObjectInputStream in = xstream.createObjectInputStream(reader);
			
			ret = in.readObject();
			in.close();
			reader.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}



	private static void setAliases(XStream xstream){
		xstream.alias("Border", kroki.mockup.model.Border.class);
		xstream.alias("Components", kroki.mockup.model.Component.class);
		xstream.alias("Composite", kroki.mockup.model.Composite.class);
		xstream.alias("Insets", kroki.mockup.model.Insets.class);
		xstream.alias("Margins", kroki.mockup.model.Margins.class);
		xstream.alias("LineBorder", kroki.mockup.model.border.LineBorder.class);
		xstream.alias("TitledBorder", kroki.mockup.model.border.TitledBorder.class);
		xstream.alias("Button", kroki.mockup.model.components.Button.class);
		xstream.alias("CheckBox", kroki.mockup.model.components.CheckBox.class);
		xstream.alias("ComboBox", kroki.mockup.model.components.ComboBox.class);
		xstream.alias("ComboZoom", kroki.mockup.model.components.ComboZoom.class);
		xstream.alias("Link", kroki.mockup.model.components.Link.class);
		xstream.alias("NullComponent", kroki.mockup.model.components.NullComponent.class);
		xstream.alias("Panel", kroki.mockup.model.components.Panel.class);
		xstream.alias("RadioButton", kroki.mockup.model.components.RadioButton.class);
		xstream.alias("TextArea", kroki.mockup.model.components.TextArea.class);
		xstream.alias("TextField", kroki.mockup.model.components.TextField.class);
		xstream.alias("TitledContainer", kroki.mockup.model.components.TitledContainer.class);
		xstream.alias("BorderLayoutManager", kroki.mockup.model.layout.BorderLayoutManager.class);
		xstream.alias("CardLayout", kroki.mockup.model.layout.CardLayout.class);
		xstream.alias("FlowLayoutManager", kroki.mockup.model.layout.FlowLayoutManager.class);
		xstream.alias("FreeLayoutManager", kroki.mockup.model.layout.FreeLayoutManager.class);
		xstream.alias("LayoutManager", kroki.mockup.model.layout.LayoutManager.class);
		xstream.alias("VerticalLayoutManager", kroki.mockup.model.layout.VerticalLayoutManager.class);
		xstream.alias("GraphElement", kroki.mockup.view.GraphElement.class);
		xstream.alias("ComponentPainter", kroki.mockup.view.painters.ComponentPainter.class);
		xstream.alias("CompositePainter", kroki.mockup.view.painters.CompositePainter.class);
		xstream.alias("ElementPainter", kroki.mockup.view.painters.ElementPainter.class);
		xstream.alias("ButtonPainter", kroki.mockup.view.painters.components.ButtonPainter.class);
		xstream.alias("CheckBoxPainter", kroki.mockup.view.painters.components.CheckBoxPainter.class);
		xstream.alias("ComboBoxPainter", kroki.mockup.view.painters.components.ComboBoxPainter.class);
		xstream.alias("ComboZoomPainter", kroki.mockup.view.painters.components.ComboZoomPainter.class);
		xstream.alias("LinkPainter", kroki.mockup.view.painters.components.LinkPainter.class);
		xstream.alias("NullComponentPainter", kroki.mockup.view.painters.components.NullComponentPainter.class);
		xstream.alias("PanelPainter", kroki.mockup.view.painters.components.PanelPainter.class);
		xstream.alias("RadioButtonPanter", kroki.mockup.view.painters.components.RadioButtonPainter.class);
		xstream.alias("TextAreaPainter", kroki.mockup.view.painters.components.TextAreaPainter.class);
		xstream.alias("TextFieldPainter", kroki.mockup.view.painters.components.TextFieldPainter.class);
		xstream.alias("TitledContainerPainter", kroki.mockup.view.painters.components.TitledContainerPainter.class);
		xstream.alias("StandardPanel", kroki.profil.panel.StandardPanel.class);
		xstream.alias("ParentChildPanel", kroki.profil.panel.container.ParentChild.class);

	}


}
