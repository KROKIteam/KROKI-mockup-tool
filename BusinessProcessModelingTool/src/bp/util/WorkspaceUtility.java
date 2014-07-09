package bp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import bp.app.AppCore;
import bp.model.data.Process;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Provides couple of utility, convenience methods for 
 * data (<code>Process</code>) storage.
 * 
 * @author specijalac
 */
public class WorkspaceUtility {

	private static XStream xstream;

	private static BufferedWriter out;

	private static BufferedReader in;

	public static final String BUSINESS_PROCESS_EXTENSION = ".kroki_bpm";
	
	private static JFileChooser chooser = new JFileChooser();

	private static String chooseLocation(){
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showSaveDialog(AppCore.getInstance());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			return path;
		}
		return null;
	}
	
	public static boolean saveProcess(Process process) {
		xstream = new XStream(new DomDriver());
		
		/* Configure XStream... */
		configureAliases();
		//omitFields();
		
		File parentFile = AppCore.getInstance().getProjectFile().getParentFile();
		String uniqueName = process.getUniqueName();
		
		if (parentFile == null){
			String path = chooseLocation();
			
			if (path == null)
				return false;
			
			parentFile = new File(path);
		}
		
		File file = new File(parentFile, uniqueName + BUSINESS_PROCESS_EXTENSION);
		
		if (file.exists()){
			if (JOptionPane.showConfirmDialog(null, 
					"Business process already exists. Overwrite?", "Confirm", 
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return false;
		}
		
		try{
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			xstream.toXML(process, out);
			out.close();
		
		} catch (Exception e) { 
			e.printStackTrace();
			JOptionPane.showMessageDialog(AppCore.getInstance(), 
					"Business process " + uniqueName +  " wasn't saved successfully!");
			return false;
		}
		
		return true;
		
	}

	public static Process load(File projectFile, String uniqueName) {
		File processFile = new File(projectFile.getParentFile(), uniqueName + BUSINESS_PROCESS_EXTENSION);
		return processFile.exists() ? load(processFile) : null;
	}
	
	public static Process load(File file) {
		
		System.out.println("Loading: " + file.getAbsolutePath());
		
		try {
			xstream = new XStream();
			configureAliases();
			
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			Process process = (Process) xstream.fromXML(in);
			return process.reloadTransientFields();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method excludes Observable and its dependencies from being
	 * serialized, along with the <code>GraphEditModel</code>.
	 * @author specijalac
	 */
	@SuppressWarnings("unused")
	private static void omitFields() {
		if (xstream instanceof XStream) {
			xstream.omitField(bp.details.ProcessDetails.class, "details");
			xstream.omitField(bp.util.BPNameGenerator.class, "nameGenerator");
			xstream.omitField(bp.event.AttributeChangeListener.class, "acListeners");
			xstream.omitField(bp.event.ElementsListener.class, "eListeners");
		}
	}
	
	/**
	 * This method sets aliases for better appearance of generated xml
	 * @author specijalac
	 */
	private static void configureAliases() {
		xstream.alias("ConditionalActivityEvent", bp.model.data.ConditionalActivityEvent.class);
		xstream.alias("ConditionalCatchEvent", bp.model.data.ConditionalCatchEvent.class);
		xstream.alias("ConditionalEdge", bp.model.data.ConditionalEdge.class);
		xstream.alias("ConditionalStartEvent", bp.model.data.ConditionalStartEvent.class);
		xstream.alias("Edge", bp.model.data.Edge.class);
		xstream.alias("EndEvent", bp.model.data.EndEvent.class);
		xstream.alias("ErrorActivityEvent", bp.model.data.ErrorActivityEvent.class);
		xstream.alias("ErrorEndEvent", bp.model.data.ErrorEndEvent.class);
		xstream.alias("ErrorStartEvent", bp.model.data.ErrorStartEvent.class);
		xstream.alias("Gateway", bp.model.data.Gateway.class);
		xstream.alias("Lane", bp.model.data.Lane.class);
		xstream.alias("LinkCatchEvent", bp.model.data.LinkCatchEvent.class);
		xstream.alias("LinkThrowEvent", bp.model.data.LinkThrowEvent.class);
		xstream.alias("MessageActivityEvent", bp.model.data.MessageActivityEvent.class);
		xstream.alias("MessageCatchEvent", bp.model.data.MessageCatchEvent.class);
		xstream.alias("MessageEndEvent", bp.model.data.MessageEndEvent.class);
		xstream.alias("MessageStartEvent", bp.model.data.MessageStartEvent.class);
		xstream.alias("MessageThrowEvent", bp.model.data.MessageThrowEvent.class);
		xstream.alias("MultipleEndEvent", bp.model.data.MultipleEndEvent.class);
		xstream.alias("MultipleStartEvent", bp.model.data.MultipleStartEvent.class);
		xstream.alias("Process", bp.model.data.Process.class);
		xstream.alias("SignalActivityEvent", bp.model.data.SignalActivityEvent.class);
		xstream.alias("SignalCatchEvent", bp.model.data.SignalCatchEvent.class);
		xstream.alias("SignalEndEvent", bp.model.data.SignalEndEvent.class);
		xstream.alias("SignalStartEvent", bp.model.data.SignalStartEvent.class);
		xstream.alias("SignalThrowEvent", bp.model.data.SignalThrowEvent.class);
		xstream.alias("StartEvent", bp.model.data.StartEvent.class);
		xstream.alias("SubProcess", bp.model.data.SubProcess.class);
		xstream.alias("SystemTask", bp.model.data.SystemTask.class);
		xstream.alias("Task", bp.model.data.Task.class);
		xstream.alias("TimerActivityEvent", bp.model.data.TimerActivityEvent.class);
		xstream.alias("TimerCatchEvent", bp.model.data.TimerCatchEvent.class);
		xstream.alias("TimerStartEvent", bp.model.data.TimerStartEvent.class);
		xstream.alias("UserTask", bp.model.data.UserTask.class);

		xstream.alias("TaskComponent", bp.model.graphic.TaskComponent.class);
		xstream.alias("LaneComponent", bp.model.graphic.LaneComponent.class);
		xstream.alias("GatewayComponent", bp.model.graphic.GatewayComponent.class);
		xstream.alias("EventComponent", bp.model.graphic.EventComponent.class);
		xstream.alias("BPEdge", bp.model.graphic.BPEdge.class);
		xstream.alias("ActivityEventComponent", bp.model.graphic.ActivityEventComponent.class);

		xstream.alias("EdgeHandlers", bp.model.graphic.util.EdgeHandlers.class);
		xstream.alias("ElementHandlers", bp.model.graphic.util.ElementHandlers.class);
		xstream.alias("Handler", bp.model.graphic.util.Handler.class);
		
		/* To be added later... */
	}
	
	
}
