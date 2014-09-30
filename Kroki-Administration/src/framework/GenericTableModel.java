package framework;

import java.util.List;

import javax.swing.table.DefaultTableModel;

public class GenericTableModel<T extends AbstractEntity> extends DefaultTableModel {
	
	private GenericDao<T> dao;
	
	public GenericTableModel(GenericDao<T> dao, String[] columnLabels) {
		super(columnLabels, 0);
		this.dao = dao;
		
	}
	
	public void fillData(){
		@SuppressWarnings("unchecked")
		List<AbstractEntity> allElements = (List<AbstractEntity>) dao.findAll();
		for(AbstractEntity e : allElements){
			addRow(e.getValues());
		}
	}
	
	public void insertRow(T entity){
		dao.save(entity);
		addRow(entity.getValues());
	}
	
	public void deleteRow(int row){
		Integer id = (Integer) getValueAt(row, 0);
		dao.deleteById(id);
		removeRow(row);
	}
	
	public void updateRow(T entity, int row){
		dao.merge(entity);
		Object[] values = entity.getValues();
		for(int i = 0; i < values.length; i++){
			setValueAt(values[i], row, i);
		}
	}

	public GenericDao<T> getDao() {
		return dao;
	}


	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
