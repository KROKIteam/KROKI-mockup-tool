package kroki.app.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.SaveUtil;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public final class Workspace implements Serializable{

	private static final long serialVersionUID = 1206782231294485404L;
	private String name;
	private List<UmlPackage> packageList = new ArrayList<UmlPackage>();
	//private List<BussinesSubsystem> bussinesSubsystemList;

	public Workspace() {
		name = StringResource.getStringResource("jTree.root.name");

	}

	public void addPackage(UmlPackage umlPackage) {
		if (!packageList.contains(umlPackage)) {
			sortedInsert((BussinesSubsystem) umlPackage);
		}
	}

	private void sortedInsert(BussinesSubsystem element){
		for (int i = 0; i < packageList.size(); i++){
			BussinesSubsystem el = (BussinesSubsystem) packageList.get(i);
			if (el.getLabel().toLowerCase().compareTo(element.getLabel().toLowerCase()) > 0){
				packageList.add(i, element);
				return;
			}
		}
		packageList.add(element);
	}


	public void saveProjectAs(UmlPackage project){
		if (!packageList.contains(project))
			return;

		if (project instanceof BussinesSubsystem){
			BussinesSubsystem proj = (BussinesSubsystem) project;

			JFileChooser jfc = new JFileChooser();
			jfc.setSelectedFile(new File(proj.getLabel().replace(" ", "_")));
			FileFilter filter = new FileNameExtensionFilter("KROKI files", "kroki");
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileFilter(filter);
			int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
			if (retValue == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				if(!file.getAbsolutePath().endsWith(".kroki")) {
					file = new File(file.getAbsolutePath() + ".kroki");
				}
				System.out.println("saving to file: " + file.getAbsolutePath());
				proj.setFile(file);
				if (SaveUtil.saveGZipObject(proj, proj.getFile())){
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Project '" + (String)proj.getLabel() +  "' successfully saved to " + file.getAbsolutePath() + "!", 0);
				}
				else{
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Error saving the project!", 3);
				}
			} else {
				System.out.println("saving canceled: ");
			}
		}
	}

	public void saveProject(UmlPackage project){
		if (!packageList.contains(project))
			return;

		if (project instanceof BussinesSubsystem){
			BussinesSubsystem proj = (BussinesSubsystem) project;

			//if project already has a file to save, save to that file, else display choose file dialog
			if(proj.getFile() != null) {
				System.out.println("saving to file: " + proj.getFile().getAbsolutePath());
				if (SaveUtil.saveGZipObject(proj, proj.getFile())){
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Project '" + (String)proj.getLabel() +  "' successfully saved!", 0);
				}
				else{
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Error saving the project!", 3);
				}
			}else {
				JFileChooser jfc = new JFileChooser();
				jfc.setSelectedFile(new File(proj.getLabel().replace(" ", "_")));
				FileFilter filter = new FileNameExtensionFilter("KROKI files", "kroki");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(filter);
				int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					if(!file.getAbsolutePath().endsWith(".kroki")) {
						file = new File(file.getAbsolutePath() + ".kroki");
					}
					System.out.println("saving to file: " + file.getAbsolutePath());
					proj.setFile(file);
					if (SaveUtil.saveGZipObject(proj, proj.getFile())){
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Project '" + (String)proj.getLabel() +  "' successfully saved to " + file.getAbsolutePath() + "!", 0);
					}
					else{
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Error saving the project!", 3);
					}
				} else {
					System.out.println("saving canceled: ");
				}
			}
		}
	}

	public void saveAllProjects(){
		for (UmlPackage project : packageList){
			saveProject(project);
		}
	}

	public void removePackage(UmlPackage umlPackage) {
		if (packageList.contains(umlPackage)) {
			packageList.remove(umlPackage);
		}
	}

	public int getIndexOf(UmlPackage umlPackage) {
		return packageList.indexOf(umlPackage);
	}

	public int getPackageCount() {
		return packageList.size();
	}

	public UmlPackage getPackageAt(int index) {
		return packageList.get(index);
	}

	@Override
	public String toString() {
		return name;
	}

	public List<UmlPackage> getPackageList() {
		return packageList;
	}

	public void setPackageList(List<UmlPackage> packageList) {
		this.packageList = packageList;
	}
}
