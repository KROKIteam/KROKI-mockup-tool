package framework;

import java.util.List;
import java.util.Map;

import framework.AbstractEntity;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractHibernateDao<T extends AbstractEntity> implements
		GenericDao<T> {

	protected static final String PERCENT = "%";

	private final Class<T> persistentClass;

	// private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public AbstractHibernateDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected AbstractHibernateDao(Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(final Integer id) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			T t = (T) getCurrentSession().get(persistentClass, id);
			transaction.commit();

			return t;
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteById(final Integer id) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			T t = (T) getCurrentSession().get(persistentClass, id);
			getCurrentSession().delete(t);
			transaction.commit();
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@Override
	public void save(final T entity) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			getCurrentSession().save(entity);
			transaction.commit();
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@Override
	public void update(final T entity) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			getCurrentSession().update(entity);
			transaction.commit();
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@Override
	public void saveOrUpdate(final T entity) {
//		Transaction transaction = null;
//		try {
//			transaction = getCurrentSession().beginTransaction();
//			getCurrentSession().saveOrUpdate(merge(entity));
//			// getCurrentSession().update(entity);
//			transaction.commit();
//		} catch (RuntimeException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			throw ex;
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(final T entity) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			T t = (T) getCurrentSession().merge(entity);
			transaction.commit();
			return t;
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@Override
	public void delete(final T entity) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			getCurrentSession().delete(entity);
			getCurrentSession().flush();
			transaction.commit();
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}
	
	public void deleteAll() {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			List<T> list = findByCriteria();
			for(Object o : list) {
				getCurrentSession().delete(o);
			}
			getCurrentSession().flush();
			transaction.commit();
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}


	@Override
	public List<T> findAll() {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			List<T> list = findByCriteria();

			transaction.commit();
			return list;
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByCriteria(final Map<?, ?> criterias) {
		final Criteria criteria = getCurrentSession().createCriteria(
				getPersistentClass());
		criteria.add(Restrictions.allEq(criterias));

		return criteria.list();

	}

	protected Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	

	@Override
	public List<T> findBySearchCriteria(Criterion... criterion) {
		Transaction transaction = null;
		try {
			transaction = getCurrentSession().beginTransaction();
			List<T> list = findByCriteria(criterion);

			transaction.commit();
			return list;
		} catch (RuntimeException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(final Criterion... criterion) {

		final Criteria crit = getCurrentSession().createCriteria(
				getPersistentClass());
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		for (final Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();

	}

	@SuppressWarnings("unchecked")
	protected T findByCriteriaUnique(final Criterion... criterion) {
		final Criteria crit = getCurrentSession().createCriteria(
				getPersistentClass());
		for (final Criterion c : criterion) {
			crit.add(c);
		}

		return (T) crit.uniqueResult();
	}

	protected int executeQuery(final String query, final String[] namedParams,
			final Object[] params) {
		final Query q = getCurrentSession().createQuery(query);

		if (namedParams != null) {
			for (int i = 0; i < namedParams.length; i++) {
				q.setParameter(namedParams[i], params[i]);
			}
		}

		return q.executeUpdate();
	}

	protected int executeQuery(final String query) {
		return executeQuery(query, null, null);
	}

	protected int executeNamedQuery(final String namedQuery,
			final String[] namedParams, final Object[] params) {
		final Query q = getCurrentSession().getNamedQuery(namedQuery);

		if (namedParams != null) {
			for (int i = 0; i < namedParams.length; i++) {
				q.setParameter(namedParams[i], params[i]);
			}
		}

		return q.executeUpdate();
	}

	protected String surroundWithPercent(final String likeParameter) {
		return PERCENT + likeParameter + PERCENT;
	}

	protected final Session getCurrentSession() {
		return HibernateUtil.getSessionfactory().getCurrentSession();
	}

	protected int executeNamedQuery(final String namedQuery) {
		return executeNamedQuery(namedQuery, null, null);
	}

}
