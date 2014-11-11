/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
