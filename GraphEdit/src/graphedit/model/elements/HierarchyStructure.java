package graphedit.model.elements;

import graphedit.model.components.Connector;
import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.io.Serializable;
import java.util.HashMap;

import kroki.profil.association.Hierarchy;

public class HierarchyStructure implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Connector, HierarchyElement> hierarchyMap = new HashMap<Connector, HierarchyElement>();
	
	public int getCurrentLevel(){
		return hierarchyMap.size() + 1;
	}
	
	public void addHierarchy(HierarchyElement hierarchyElement, Connector connector){
		Hierarchy hierarchy = hierarchyElement.getHierarchy();
		int level = hierarchy.getLevel();
		//ubaci na ovaj level, pomeri ostale
		for (Connector c : hierarchyMap.keySet()){
			HierarchyElement elem = hierarchyMap.get(c);
			if (elem.getHierarchy().getLevel()>=level){
				if (elem.getHierarchy().getLevel() == level)
					elem.getHierarchy().setHierarchyParent(hierarchy);
				int newLevel = elem.getHierarchy().getLevel();
				if (elem.getHierarchy().getHierarchyParent()!=hierarchy)
					elem.getHierarchy().setLevel(++newLevel);
				c.getLink().setProperty(LinkProperties.STEREOTYPE, "hierarchy level = " + newLevel);
			}
		}
		hierarchy.setLevel(level);
		hierarchyMap.put(connector, hierarchyElement);
	}
	
	public void addHierarchy(Hierarchy hierarchy, int index,  Connector connector){
		hierarchyMap.put(connector, new HierarchyElement(hierarchy, index));
		
	}
	
	public HierarchyElement getHierarchy(Connector key){
		return hierarchyMap.get(key);
	}
	
	public void removeHierarchy(HierarchyElement h, Connector connector) {
		//pomeri nivoe hijerarchije
		Hierarchy hierarchy = h.getHierarchy();
		int level = hierarchy.getLevel();
		for (Connector c : hierarchyMap.keySet()){
			HierarchyElement elem = hierarchyMap.get(c);
			if (elem.getHierarchy().getHierarchyParent() == hierarchy)
				elem.getHierarchy().setHierarchyParent(hierarchy.getHierarchyParent());
			if (elem.getHierarchy().getLevel()>level){
				int newLevel = elem.getHierarchy().getLevel()-1;
				elem.getHierarchy().setLevel(newLevel);
				c.getLink().setProperty(LinkProperties.STEREOTYPE, "hierarchy level = " + newLevel);
			}
		}
		hierarchyMap.remove(connector);
	}
	
	public int getSize(){
		return hierarchyMap.size();
	}
	
	
	public Hierarchy getCurrentHierarchyParent(){
		if (hierarchyMap.size() == 0)
			return null;
		int maxLevel = 0;
		Hierarchy lowestHierarchy = null;
		for (HierarchyElement h : hierarchyMap.values()){
			if (h.getHierarchy().getLevel() > maxLevel){
				maxLevel = h.getHierarchy().getLevel();
				lowestHierarchy = h.getHierarchy();
			}
		}
		return lowestHierarchy;
	}


	
}
