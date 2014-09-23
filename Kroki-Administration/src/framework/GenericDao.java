package framework;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

public interface GenericDao<T extends AbstractEntity> {

   List<T> findAll();

   List<T> findByCriteria(Map<?, ?> criterias);
   
   List<T> findBySearchCriteria(Criterion... criterion);

   void save(T entity);

   void update(T entity);

   void saveOrUpdate(T entity);

   void delete(T entity);

   T merge(T entity);

   T findById(Integer id);

   void deleteById(Integer id);
   
   

}
