package kroki.app.gui.settings;

import java.util.HashMap;
import java.util.Map;

import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.Aggregated;
import kroki.profil.property.Calculated;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

public class ElementSettingsPanelMappings {
	
	private static Map<Class<?>, Class<?>> settingsMap = new HashMap<Class<?>, Class<?>>();
	
	static {
		settingsMap.put(Calculated.class, CalculatedSettings.class);
		settingsMap.put(VisibleProperty.class, VisiblePropertySettings.class);
		settingsMap.put(Aggregated.class, AggregatedSettings.class);
		settingsMap.put(ParentChild.class, VisibleClassSettings.class);
		settingsMap.put(VisibleClass.class, VisibleClassSettings.class);
		settingsMap.put(StandardPanel.class, StandardPanelSettings.class);
		settingsMap.put(Transaction.class, TransactionSettings.class);
		settingsMap.put(Report.class, ReportSettings.class);
		settingsMap.put(BussinessOperation.class, BussinessOperationSettings.class);
		settingsMap.put(ElementsGroup.class, ElementsGroupSettings.class);
		settingsMap.put(Zoom.class, ZoomSettings.class);
		settingsMap.put(VisibleAssociationEnd.class, VisibleAssociationEndSettings.class);
		settingsMap.put(Next.class, NextSettings.class);
		settingsMap.put(Hierarchy.class, HierarchySettings.class);
		settingsMap.put(VisibleElement.class, ZoomSettings.class);
		settingsMap.put(BussinesSubsystem.class, BusinessSubsystemSettings.class);
	}
	
	public static Class<?> getSettingsClass(VisibleElement el){
		return settingsMap.get(el.getClass());
	}

}


