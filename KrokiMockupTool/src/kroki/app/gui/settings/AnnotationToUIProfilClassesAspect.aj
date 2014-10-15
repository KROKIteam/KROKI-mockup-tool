package kroki.app.gui.settings;

import kroki.profil.property.*;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.*;
import kroki.profil.operation.*;
import kroki.profil.association.*;
import kroki.profil.group.ElementsGroup;
import kroki.profil.*;

public aspect AnnotationToUIProfilClassesAspect {

	declare @type : Calculated : @SettingsPanel(CalculatedSettings.class);
	
	declare @type : VisibleProperty : @SettingsPanel(VisiblePropertySettings.class);

	declare @type : Aggregated : @SettingsPanel(AggregatedSettings.class);
	
	declare @type : ParentChild : @SettingsPanel(VisibleClassSettings.class);
	
	declare @type : VisibleClass : @SettingsPanel(VisibleClassSettings.class) ;

	declare @type : StandardPanel : @SettingsPanel(StandardPanelSettings.class) ;
	
	declare @type : Transaction : @SettingsPanel(TransactionSettings.class) ;
	
	declare @type : Report : @SettingsPanel(ReportSettings.class) ;
	
	declare @type : BussinessOperation : @SettingsPanel(BussinessOperationSettings.class) ;

	declare @type : ElementsGroup : @SettingsPanel(ElementsGroupSettings.class) ;
	declare @type : Zoom : @SettingsPanel(ZoomSettings.class) ;
	declare @type : VisibleAssociationEnd : @SettingsPanel(VisibleAssociationEndSettings.class) ;
	declare @type : Next : @SettingsPanel(NextSettings.class) ;
	declare @type : Hierarchy : @SettingsPanel(HierarchySettings.class) ;
	declare @type : VisibleElement : @SettingsPanel(VisibleElementSettings.class) ;

}
