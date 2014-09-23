package framework;


import java.awt.Component;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


/**
 * Klasa u kojoj se vrsi pretraga entiteta
 * po podacima zadanim u panelu.
 * 
 */
public class SearchEngine {
	
	private IEntity entity;
	@SuppressWarnings("rawtypes")
	private JPanel fieldsPanel;
	@SuppressWarnings("rawtypes")
	private GenericTableModel model;
	
	@SuppressWarnings("rawtypes")
	public SearchEngine(IEntity entity, JPanel fieldsPanel, GenericTableModel model){
		this.entity = entity;
		this.fieldsPanel = fieldsPanel;
		this.model = model;
	}
	
	public void search(){
		System.out.println("searching...");
		Session session = HibernateUtil.getSessionfactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		Criteria criteria = session.createCriteria(entity.getClass(), "entity");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		criteria.createAlias("nalog.klijent", "klijent"); // inner join by default
//		criteria.createAlias("nalog.uredjaj", "uredjaj", Criteria.LEFT_JOIN);
		
		Field[] fields = entity.getClass().getDeclaredFields();
		Component[] components = fieldsPanel.getComponents();
		for(Field field : fields){
			for(Component cmp : components){
				if(cmp instanceof JTextField){
					if(field.getName().equals(cmp.getName())){
						String value = ((JTextField)cmp).getText().trim();
						if(!value.isEmpty()){
							if(value.endsWith("*")){
								value = value.replace("*", "%");
								criteria.add(Restrictions.like(field.getName(), value));
							}else{
								criteria.add(Restrictions.eq(field.getName(), value));
							}
						}
					}
				}else if(cmp instanceof JComboBox){
					if(field.getName().equals(cmp.getName())){
						String alias = "entity." + field.getName();
						criteria.createAlias(alias, field.getName());
						IEntity value = (IEntity) ((JComboBox) cmp).getSelectedItem();
						String id = field.getName()+".id";
						criteria.add(Restrictions.eq(id, value.getId()));
					}
				}
			}
		}
		
		List<IEntity> results = criteria.list();
		transaction.commit();
		
		model.setRowCount(0);
		for(IEntity entity : results){
			model.addRow(entity.getValues());
		}
	}

}
