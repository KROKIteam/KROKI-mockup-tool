package graphedit.model;

import graphedit.app.MainFrame;
import graphedit.layout.LayoutStrategy;
import graphedit.model.components.GraphElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.interfaces.GraphEditTreeNode;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.PackageProperties;
import graphedit.model.properties.PropertyEnums.WorkspaceProperties;
import graphedit.properties.Preferences;
import graphedit.util.WorkspaceUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;


public class GraphEditWorkspace extends Observable implements GraphEditTreeNode {


	private List<GraphEditPackage> packageList;


	private Properties<WorkspaceProperties> properties;

	private Preferences preferences;

	public static final String DEFAULT_WORKSPACE = "Workspace";

	private File file;

	private Map<GraphEditPackage, LayoutStrategy> layoutMap;

	private static GraphEditWorkspace workspace;

	private GraphEditWorkspace() {
		this.packageList = new ArrayList<GraphEditPackage>();
		this.properties = new Properties<WorkspaceProperties>();
		this.properties.set(WorkspaceProperties.NAME, DEFAULT_WORKSPACE);
		this.preferences = Preferences.getInstance();
		layoutMap = new HashMap<GraphEditPackage, LayoutStrategy>();
		try {
			setWorkspacePath();
		} catch (Exception e) { }
	}


	public void setPackageList(List<UmlPackage> packages){
		packageList.clear();
		for (UmlPackage pack : packages){
			prepareProject(pack);
		}
		this.setChanged();
		this.notifyObservers();
		for (GraphEditPackage pack : packageList){
			MainFrame.getInstance().showDiagram(pack.getDiagram());
		}
	}

	public void setProject(UmlPackage project){
		packageList.clear();
		prepareProject(project);

		this.setChanged();
		this.notifyObservers();
		MainFrame.getInstance().showDiagram(packageList.get(0).getDiagram());
	}

	private void prepareProject(UmlPackage project){

		GraphEditPackage projectElement = null;
		GraphEditPackage loadedElement = null;

		if (project instanceof BussinesSubsystem){
			loadedElement = (GraphEditPackage) ((BussinesSubsystem) project).getGraphPackage();
		}


		projectElement = new GraphEditPackage(project, null, loadedElement);
		if (loadedElement != null)
			projectElement.setFile(loadedElement.getFile());

		for (GraphEditPackage pack : projectElement.getSubPackages()){
			pack.generateShortcuts(loadedElement);
			pack.generateRelationships(projectElement.getSubClassesMap(), loadedElement);
		}

		projectElement.generateShortcuts(loadedElement);

		projectElement.generateRelationships(projectElement.getSubClassesMap(), loadedElement);

		packageList.add(projectElement);

		//TODO pogledati kada ne treba uopste
		LayoutStrategy layoutStrategy = LayoutStrategy.TREE;
		if (loadedElement != null)
			layoutStrategy = LayoutStrategy.ADDING;

		layoutMap.put(projectElement, layoutStrategy);

	}

	public GraphEditModel getDiagramContainingElement(GraphElement element){
		for (GraphEditPackage pack : packageList){
			if (pack.getDiagram().getDiagramElements().contains(element))
				return pack.getDiagram();
			for (GraphEditPackage subPackage : pack.getSubPackages())
				if (subPackage.getDiagram().getDiagramElements().contains(element))
					return subPackage.getDiagram();
		}
		return null;
	}


	public static GraphEditWorkspace getInstance() {
		if (!(workspace instanceof GraphEditWorkspace)) {
			workspace = new GraphEditWorkspace();
		}
		return workspace;
	}



	public void switchWorkspace(File file) {
		this.packageList = new ArrayList<GraphEditPackage>();
		this.properties = new Properties<WorkspaceProperties>();
		this.properties.set(WorkspaceProperties.NAME, 
				DEFAULT_WORKSPACE + " (" + file.getAbsolutePath() + ")");
		this.file = file;

		if (!file.canWrite()) {
			file = new File(".");
		} 

		this.preferences.setProperty(Preferences.WORKSPACE_PATH_KEY, file.getAbsolutePath());
		this.preferences.saveProperties();
		MainFrame.getInstance().setParametrizedTitle(file.getAbsolutePath());
	}

	private void setWorkspacePath() throws FileNotFoundException, IOException {
		file = new File(preferences.getProperty(Preferences.WORKSPACE_PATH_KEY));
		if (!file.canWrite()) {
			file = new File(".");
		} 
		this.properties.set(WorkspaceProperties.NAME, 
				DEFAULT_WORKSPACE + " (" + file.getAbsolutePath() + ")");

		if (!file.exists()) file.mkdir();
		System.out.println("Path: " + file.getAbsolutePath());
	}

	public List<GraphEditPackage> getpackageList() {
		if (packageList == null)
			packageList = new ArrayList<GraphEditPackage>();
		return packageList;
	}

	public Iterator<GraphEditPackage> getIteratorpackageList() {
		if (packageList == null)
			packageList = new ArrayList<GraphEditPackage>();
		return packageList.iterator();
	}

	public void setpackageList(List<GraphEditPackage> newpackageList) {
		removeAllFromList();
		this.packageList = newpackageList;

		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();
	}

	public void addProject(GraphEditPackage newGraphEditPackage) {
		if (newGraphEditPackage == null)
			return;
		if (this.packageList == null)
			this.packageList = new ArrayList<GraphEditPackage>();
		if (!this.packageList.contains(newGraphEditPackage))
			sortedInsert(newGraphEditPackage);

		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();
	}

	private void sortedInsert(GraphEditPackage newGraphEditPackage){
		for (int i = 0; i < packageList.size(); i++){
			GraphEditPackage el =  packageList.get(i);
			if (((String)el.getProperty(PackageProperties.NAME)).toLowerCase().compareTo(
					((String)newGraphEditPackage.getProperty(PackageProperties.NAME)).toLowerCase()) > 0){
				packageList.add(i, newGraphEditPackage);
				return;
			}
		}
		packageList.add(newGraphEditPackage);
	}

	public void removeProject(GraphEditPackage oldGraphEditPackage) {
		if (oldGraphEditPackage == null)
			return;
		if (this.packageList != null)
			if (this.packageList.contains(oldGraphEditPackage)) {
				this.packageList.remove(oldGraphEditPackage);
			}

		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();
	}


	public void removeAllFromList() {
		if (packageList != null)
			packageList.clear();

		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();
	}

	public GraphEditModel findModelContainingElement(GraphElement element){
		GraphEditModel diag = null;
		for (GraphEditPackage graphPackage : packageList){
			diag = graphPackage.findDiagramContainingElement(element);
			if (diag != null)
				break;
		}
		return diag;
	}


	public LayoutStrategy getLayoutStrategy(GraphEditPackage pack){
		GraphEditPackage topPackage = WorkspaceUtility.getTopPackage(pack);
		LayoutStrategy topStrategy = layoutMap.get(topPackage);
		if (topStrategy != LayoutStrategy.ADDING)
			return topStrategy;

		if (pack.isLoaded())
			return LayoutStrategy.ADDING;
		return LayoutStrategy.TREE;
	}

	public Object getProperty(WorkspaceProperties key) {
		return properties.get(key);
	}

	public Object setProperty(WorkspaceProperties key, Object value) {
		Object result = properties.set(key, value);

		// fire-uj izmene
		this.setChanged();
		this.notifyObservers();

		return result;
	}

	@Override
	public Object getNodeAt(int index) {
		return packageList.get(index);
	}

	@Override
	public int getNodeCount() {
		return packageList.size();
	}

	@Override
	public int getNodeIndex(Object node) {
		if (packageList.contains(node))
			return packageList.indexOf(node);
		return -1;
	}

	@Override
	public String toString() {
		return (String) properties.get(WorkspaceProperties.NAME);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}




}