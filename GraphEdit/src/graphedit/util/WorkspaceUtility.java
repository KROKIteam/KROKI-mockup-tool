package graphedit.util;

import graphedit.app.MainFrame;
import graphedit.model.components.Package;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.PackageProperties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import kroki.uml_core_basic.UmlPackage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Provides couple of utility, convenience methods for data (
 * <code>Project</code>, <code>GraphEditModel</code>) storage
 * 
 * @author specijalac
 */
public class WorkspaceUtility {

	private static XStream xstream;

	private static BufferedWriter out;

	private static BufferedReader in;

	public static final String FILE_EXTENSION = ".dgm";

	public static final String PROJECT_EXTENSION = ".kroki_graph";
	private static JFileChooser chooser = new JFileChooser();

	public static void save(GraphEditModel model) {
	/*	xstream = new XStream(new DomDriver());
		configureAliases();
		omitObservable();
		if (!model.getParentProject().isSerialized()) saveProject(model.getParentProject());
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(model.getFile()), "UTF-8"));		
			xstream.toXML(model, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	private static String chooseLocation(){
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showSaveDialog(MainFrame.getInstance());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getPath();
			return path;
		}
		return null;
	}
	
	public static boolean saveProject(GraphEditPackage project) {
		xstream = new XStream(new DomDriver());
		configureAliases();
		omitObservable();
		File file = project.getFile();
		
		if (file == null){
			String path = chooseLocation();
		if (path == null)
			return false;
		
			file = new File(path, (String)project.getProperty(PackageProperties.NAME) + PROJECT_EXTENSION);
			if (file.exists()){
				if (JOptionPane.showConfirmDialog(null, "Project already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION) 
						!= JOptionPane.YES_OPTION)
					return false;
			}
			project.setFile(file);
		}
		//parentProject.setSerialized(true);
		
		try{
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			xstream.toXML(project, out);
			out.close();
		
		} catch (Exception e) { 
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.getInstance(), 
					"Project " +(String)project.getProperty(PackageProperties.NAME) +  " wasn't saved successfully!");
			return false;
		}
		
		JOptionPane.showMessageDialog(MainFrame.getInstance(), 
				"Project " + (String)project.getProperty(PackageProperties.NAME) +  " saved successfully!");
		
		return true;
		
	}

	
	public static GraphEditPackage load(File file) {
		
	try {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		xstream = new XStream();
		configureAliases();
		return (GraphEditPackage) xstream.fromXML(in);
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
	/*	xstream = new XStream();
		configureAliases();
		GraphEditWorkspace workspace = GraphEditWorkspace.getInstance();
		
		List<Project> projects = new ArrayList<Project>();
		Properties<ProjectProperties> properties;
		Project project;
		File confFile;
		
		try {
			for (File f : workspace.getFile().listFiles()) {
				if ((confFile = hasConfFile(f)) instanceof File) {
					project = new Project(confFile.getName());
					in = new BufferedReader(new InputStreamReader(new FileInputStream(confFile)));
					properties = (Properties<ProjectProperties>) xstream.fromXML(in);
					in.close();
					project.setProperties(properties);
					project.setFile(confFile.getParentFile());
					// tag it as serialized
					project.setSerialized(true);
					projects.add(project);
				}
			}
			workspace.setProjects(projects);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void load(UmlPackage project) {
	/*	xstream = new XStream();
		configureAliases();
		List<GraphEditModel> elements = new ArrayList<GraphEditModel>();
		List<GraphEditPackage> packages = new ArrayList<GraphEditPackage>();
		GraphEditModel element;
		GraphEditPackage pack;
		try {
			for (File f : project.getFile().listFiles()) {
				if (f.isFile() && f.getName().toLowerCase().endsWith(FILE_EXTENSION)) {
					in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					element = (GraphEditModel) xstream.fromXML(in);
					in.close();
					element.setParentProject(project);
					elements.add(new GraphEditModel(element));
				} else if (f.isDirectory()) {
					pack = new GraphEditPackage(f.getName());
					pack.setParentProject(project);
					pack.setProperty(PackageProperties.NAME, f.getName());
					pack.setFile(f);
					
					// do the same for every single child of its subpackage
					if (pack.getFile().listFiles().length > 0)
						parseFileStructure(pack, project);
					
					packages.add(pack);
				}
			}
			project.setDiagrams(elements);
			project.setPackages(packages);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Beware this method permanently removes target file from file system.
	 * @param target represents a file which is to be deleted.
	 */
	public static void removeFileFromFileSystem(File target) {
		target.delete();
	}
	
	/**
	 * Beware this method permanently removes target directory and its 
	 * contents from file system.
	 * @param target represents a file which is to be deleted.
	 */
	public static void removeDirectoryFromFileSystem(File target) {
		if (target.isDirectory() && target.listFiles().length > 0) {
			for (File f : target.listFiles())
				if (f.isDirectory()) removeDirectoryFromFileSystem(f);
				else removeFileFromFileSystem(f);
		}
		target.delete();
	}
	
	@SuppressWarnings("unused")
	private static void parseFileStructure(GraphEditPackage masterPackage, UmlPackage parentProject) {
	/*	GraphEditModel element;
		GraphEditPackage pack;
		
		System.out.println("Recursive call for: " + masterPackage.getFile().getName());
		
		for (File f : masterPackage.getFile().listFiles()) {
			if (f.isFile() && f.getName().toLowerCase().endsWith(FILE_EXTENSION)) {
				try {
					in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					element = (GraphEditModel) xstream.fromXML(in);
					in.close();
					element.setParentProject(parentProject);
					masterPackage.addDiagram(new GraphEditModel(element));
				} catch (Exception e) { e.printStackTrace(); }
			} else if (f.isDirectory()) {
				pack = new GraphEditPackage(f.getName());
				pack.setParentProject(parentProject);
				pack.setProperty(PackageProperties.NAME, f.getName());
				pack.setFile(f);
				masterPackage.addPackage(pack);
				
				// do the same for every single child of its subpackage
				if (pack.getFile().listFiles().length > 0)
					parseFileStructure(pack, parentProject);
			}
		}*/
	}
	
	public static GraphEditPackage getTopPackage(GraphEditPackage pack){
		while (pack.getParentPackage() != null)
			pack = pack.getParentPackage();
		return pack;
	}
	
	public static List<GraphEditModel> allDiagramsInProject(GraphEditPackage project){
		List<GraphEditModel> ret = new ArrayList<GraphEditModel>();
		allDiagramsInPackage(project, ret);
		return ret;
	}
	
	private static void allDiagramsInPackage(GraphEditPackage pack, List<GraphEditModel> ret){
		ret.add(pack.getDiagram());
		for (Package p : pack.getDiagram().getContainedPackages())
			allDiagramsInPackage((GraphEditPackage) p.getRepresentedElement(), ret);
	}
	
	/**
	 * Method checks whether provided a file meets the rules regarding
	 * project's hierarchy.
	 * @param file represents a file which is to be checked.
	 * @return whether provided <code>file</code> is a <code>Project</code> directory.
	 * @author specijalac
	 */
	@SuppressWarnings("unused")
	private static File hasConfFile(File file) {
		if (file.isDirectory()) {
			for (File f: file.listFiles()) {
				if (f.getName().toLowerCase().endsWith(PROJECT_EXTENSION))
					return f;
			}
		}
		return null;
	}

	/**
	 * This method excludes Observable and its dependencies from being
	 * serialized, along with the <code>GraphEditModel</code>.
	 * @author specijalac
	 */
	private static void omitObservable() {
		if (xstream instanceof XStream) {
			xstream.omitField(java.util.Observable.class, "obs");
			xstream.omitField(java.util.Observable.class, "changed");
		}
	}
	
	/**
	 * This method sets aliases for better appearance of generated xml
	 * @author specijalac
	 */
	private static void configureAliases() {
		xstream.alias("package", graphedit.model.elements.GraphEditPackage.class);
		xstream.alias("model", graphedit.model.diagram.GraphEditModel.class);
		xstream.alias("class", graphedit.model.components.Class.class);
		xstream.alias("interface", graphedit.model.components.Interface.class);
		xstream.alias("properties", graphedit.model.properties.PropertyEnums.class);
		xstream.alias("umlPackage", kroki.profil.subsystem.BussinesSubsystem.class);
		xstream.alias("nestingPackage", kroki.profil.subsystem.BussinesSubsystem.class);
		
	}
	
	
}
