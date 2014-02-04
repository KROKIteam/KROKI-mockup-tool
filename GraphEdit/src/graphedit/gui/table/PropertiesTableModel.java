package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.command.ChangeAssociationPropertiesCommand;
import graphedit.command.ChangeElementCommand;
import graphedit.command.ChangeLinkCommand;
import graphedit.command.Command;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.ClassStereotypeUI;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.util.LinkingUtil;
import graphedit.util.NameTransformUtil;
import graphedit.util.Validator;
import graphedit.view.SelectionModel;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("unchecked")
public class PropertiesTableModel extends AbstractTableModel implements Observer {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = {"Property", "Value"};
	private SelectionModel selectionModel;
	private List<Entry<? extends PropertyEnums, Object>> entries;
	private ApplicationModeProperties properties = ApplicationModeProperties.getInstance();

	public void reassignSelectionModel() {
		this.selectionModel = MainFrame.getInstance().getCurrentView().getSelectionModel();
		this.selectionModel.addObserver(this);

		GraphEditModel model = MainFrame.getInstance().getCurrentView().getModel();
		model.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (selectionModel != null) {
			if (selectionModel.getSelectedElements().size() == 1) {
				makeEntries(selectionModel.getSelectedElements().get(0));
				MainFrame.getInstance().prepareTable(true);
			} else if (selectionModel.getSelectedLink() != null) {
				makeEntries(selectionModel.getSelectedLink());
				MainFrame.getInstance().prepareTable(false);
			} else {
				entries = null;
			}
		} else { 
			entries = null;
		}

		fireTableDataChanged();
	}

	private void makeEntries(GraphElement element) {

		GraphElement shortcutTo = null;
		if (element instanceof Shortcut){
			shortcutTo = ((Shortcut)element).shortcutTo();
		}

		Set<Entry<? extends PropertyEnums, Object>> entrySet = (Set<Entry<? extends PropertyEnums, Object>>) element.getEntrySet();
		entries = new ArrayList<Entry<? extends PropertyEnums,Object>>();
		HashMap<Integer, Entry<? extends PropertyEnums, Object>> orderMap = new HashMap<Integer,Entry<? extends PropertyEnums, Object>>();



		for (Entry<? extends PropertyEnums, Object> e : entrySet) {
			orderMap.put(getOrder(e.getKey().toString()), e);
		}

		for (int i=0;i<entrySet.size();i++){
			Entry<? extends PropertyEnums, Object> e = orderMap.get(i);
			if (shortcutTo != null){
				if (e.getKey() != GraphElementProperties.SIZE && e.getKey() != GraphElementProperties.POSITION )
					e.setValue(shortcutTo.getProperty((GraphElementProperties) e.getKey()));
			}
			entries.add(e);
		}

	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		return entries != null ? entries.size() : 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (entries != null) {
			return (columnIndex > 0 ? 
					getCellValue(entries.get(rowIndex)) : NameTransformUtil.transformUppercase(entries.get(rowIndex).getKey().toString())); 
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return false;

		Object value = getValueAt(rowIndex, columnIndex - 1);
		if(value instanceof String && properties.hasProperty(((String) value).replace(" ", "")))
			if (!(Boolean) properties.getPropertyValue((String) value))
				return false;

		return !(getValueAt(rowIndex, columnIndex) instanceof JButton);
	}

	/*public Class<?> getColumnClass(int columnIndex) {
		return JButton.class;
	}*/

	private Object getCellValue(Entry<? extends PropertyEnums, Object> entry) {
		if (entry.getKey() == GraphElementProperties.SIZE) {
			Dimension2D dimension = (Dimension2D)entry.getValue();
			return "(" + dimension.getWidth() + ", " + dimension.getHeight() + ")";
		} else if (entry.getKey() == GraphElementProperties.POSITION) {
			Point2D point = (Point2D)entry.getValue();
			return "(" + point.getX() + ", " + point.getY() + ")";
		} else if (entry.getKey() == GraphElementProperties.COLOR) {
			Color color = (Color)entry.getValue();
			return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
		} else if (entry.getKey() == GraphElementProperties.METHODS) {
			return entry.getValue();
		} else if (entry.getKey() == GraphElementProperties.ATTRIBUTES) {
			return entry.getValue();
		}
		return entry.getValue();
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (value == null || getValueAt(rowIndex, columnIndex)==null)
			return;
		if (getValueAt(rowIndex, columnIndex).equals(value)) {
			return;
		}
		if (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(GraphElementProperties.NAME.toString()))) {
			if (Validator.classHasName((String) value)) {
				Dialogs.showErrorMessage("Name already exists!", "Error");
			} else if (!Validator.isJavaIdentifier((String) value)) {
				Dialogs.showErrorMessage("Not a valid identifier name!", "Error");
			} else {
				ChangeElementCommand command = new ChangeElementCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedElements().get(0), (String)value, GraphElementProperties.NAME);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
		} else if  (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(GraphElementProperties.STEREOTYPE.toString()))) {
			String valueStr;
			if (value instanceof ClassStereotypeUI)
				valueStr = ((ClassStereotypeUI)value).toString();
			else
				valueStr = (String) value;
			ChangeElementCommand command = new ChangeElementCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedElements().get(0), valueStr, GraphElementProperties.STEREOTYPE);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
		} else if (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.STEREOTYPE.toString()))) {
			ChangeLinkCommand command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.STEREOTYPE);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
		} else if  (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.NAME.toString()))) {
			ChangeLinkCommand command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.NAME);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
			//source cardinality
		} else if (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.SOURCE_CARDINALITY.toString()))) {
			if (!validCardinality((String)value))
				Dialogs.showErrorMessage("Invalid cardinality", "Error");
			else{

				String otherCardinality = LinkingUtil.otherCardinality((String)value,selectionModel.getSelectedLink(), true);
				Command command;
				Link link = selectionModel.getSelectedLink();
				if (!otherCardinality.equals(""))
					command = new ChangeAssociationPropertiesCommand(MainFrame.getInstance().getCurrentView(), (String)value, otherCardinality, (String) link.getProperty(LinkProperties.SOURCE_ROLE),(String) link.getProperty(LinkProperties.DESTINATION_ROLE),true,true, (AssociationLink) link);
				else
					command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.SOURCE_CARDINALITY);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
		} else if  (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.SOURCE_ROLE.toString()))) {
			ChangeLinkCommand command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.SOURCE_ROLE);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
			//destination cardinality
		} else if (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.DESTINATION_CARDINALITY.toString()))) {
			if (!validCardinality((String)value))
				Dialogs.showErrorMessage("Invalid cardinality", "Error");
			else{
				String otherCardinality = LinkingUtil.otherCardinality((String)value,selectionModel.getSelectedLink(), false);
				Command command;
				Link link = selectionModel.getSelectedLink();
				if (!otherCardinality.equals("")){
					command = new ChangeAssociationPropertiesCommand(MainFrame.getInstance().getCurrentView(), otherCardinality, (String)value,(String) link.getProperty(LinkProperties.SOURCE_ROLE),(String) link.getProperty(LinkProperties.DESTINATION_ROLE),true,true, (AssociationLink) link);
				}
				else
					command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.DESTINATION_CARDINALITY);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
		} else if (getValueAt(rowIndex, 0).equals(NameTransformUtil.transformUppercase(LinkProperties.DESTINATION_ROLE.toString()))) {
			ChangeLinkCommand command = new ChangeLinkCommand(MainFrame.getInstance().getCurrentView(), selectionModel.getSelectedLink(), (String)value, LinkProperties.DESTINATION_ROLE);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
		}		
		fireTableDataChanged();
	}


	/**
	 * Sort properties
	 * @param key
	 * @return
	 */
	private int getOrder(String key){
		if (key.equals("NAME"))
			return 0;
		else if (key.equals("POSITION")||key.equals("DESTINATION_ROLE"))
			return 1;
		else if (key.equals("SIZE")||key.equals("SOURCE_ROLE"))
			return 2;
		else if (key.equals("ATTRIBUTES")  || key.equals("SOURCE_CARDINALITY"))
			return 3;
		else if (key.equals("METHODS") ||key.equals("DESTINATION_CARDINALITY"))
			return 4;
		else if (key.equals("STEREOTYPE"))
			return 5;
		else if (key.equals("SOURCE_NAVIGABLE"))
			return 6;
		else if (key.equals("DESTINATION_NAVIGABLE"))
			return 7;
		return -1;

	}

	/**
	 * Checks if the cardinality is valid (user interface classes cannot be linked randomly)
	 * @param cardinality
	 * @return
	 */
	private boolean validCardinality(String cardinality){

		if (MainFrame.getInstance().getAppMode() != ApplicationMode.USER_INTERFACE)
			return true;
		Link link =  selectionModel.getSelectedLink();
		if (link.getSourceConnector().getRepresentedElement().element().getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()) || link.getDestinationConnector().getRepresentedElement().element().getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
			return cardinality.endsWith("1");
		else
			return cardinality.endsWith("*") || cardinality.endsWith("1"); 
	}



}
