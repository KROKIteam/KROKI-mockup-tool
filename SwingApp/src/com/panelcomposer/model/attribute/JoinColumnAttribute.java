package com.panelcomposer.model.attribute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/****
 * JoinColumn, ManyToOne / ZOOM Attribute
 * @author Darko
 *
 */
public class JoinColumnAttribute extends AbsAttribute {
	
	protected Class<?> lookupClass;
	protected String zoomedBy;
	protected List<ColumnAttribute> columns;
	
	public JoinColumnAttribute() {
		columns = new ArrayList<ColumnAttribute>();
	}

	public ColumnAttribute getZoomedByAsColumn() {
		ColumnAttribute ca = null;
		Iterator<ColumnAttribute> it = columns.iterator();
		while(it.hasNext()) {
			ca = it.next();
			if(ca.getName().equals(zoomedBy))
				return ca;
		}
		return ca;
	}
	
	/***
	 * Adds column attribute linked with this zoom
	 * @param column
	 */
	public void add(ColumnAttribute column) {
		columns.add(column);
	}
	
	public void remove(ColumnAttribute column) {
		columns.remove(column);
	}
	
	public Class<?> getLookupClass() {
		return lookupClass;
	}

	public void setLookupClass(Class<?> lookupClass) {
		this.lookupClass = lookupClass;
	}

	public List<ColumnAttribute> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnAttribute> columns) {
		this.columns = columns;
	}

	public String getZoomedBy() {
		return zoomedBy;
	}

	public void setZoomedBy(String zoomedBy) {
		this.zoomedBy = zoomedBy;
	}
	
}
