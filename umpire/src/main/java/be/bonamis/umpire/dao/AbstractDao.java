package be.bonamis.umpire.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import be.bonamis.umpire.entity.Entity;



/**
 * This is the abstract DAO that must be extended by ALL the DAO. This class contains all the CRUD operations.
 * 
 * @author Gaetano De Stefano
 * 
 * @param <T>
 * @param <P>
 */
@Transactional(readOnly = true)
public abstract class AbstractDao<T extends Entity, P extends Serializable> implements Dao<T, P> {

    private final static String WHITESPACE = " ";
    private final static String SELECT = "select";

    /**
     * To obtains the current {@link Session}, prefer the {@link AbstractDao#getSession()} method
     */
    @Autowired
    private SessionFactory sessionFactory;
    private final Class<T> classT;

    @SuppressWarnings("unchecked")
    public AbstractDao() {

	Class<?> daoClass;
	if (this.getClass().getSuperclass() == AbstractDao.class) {
	    // We are instantiated without CGLIB.
	    daoClass = this.getClass();
	} else {
	    // Spring instantiates as CGLIB class extending our concrete DAO class.
	    Class<?> cglibConcreteDaoClass = this.getClass();
	    daoClass = cglibConcreteDaoClass.getSuperclass();
	}

	ParameterizedType parameterizedType = (ParameterizedType) daoClass.getGenericSuperclass();

	classT = (Class<T>) parameterizedType.getActualTypeArguments()[0];

    }

    @Override
    @Transactional(readOnly = false)
    public void delete(T entity) {
	getSession().delete(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T find(Integer id) {
	return (T) getSession().get(classT, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
	return (List<T>) getSession().createQuery("from " + classT.getName()).list();
    }

    @Override
    @Transactional(readOnly = false)
    public void save(T entity) {
	getSession().saveOrUpdate(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public T merge(T entity) {
	return (T) getSession().merge(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void flush() {
	getSession().flush();
    }

    @Override
    public void clear() {
	getSession().clear();
    }

    @Override
    public void refresh(T entity) {
	getSession().refresh(entity);
    }

    @Override
    public void evict(T entity) {
	getSession().evict(entity);
    }

    protected void delete(final Class<? extends Entity> entity, String identifierName, Object identifier) {
	getSession()
		.createQuery(
			new StringBuilder().append("delete from ").append(entity.getName()).append(" e ").append("where e.").append(identifierName)
				.append("=:id").toString())

		.setParameter("id", identifier).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> select(final Class<E> type) {
	Query query = getSession().createQuery("from " + type.getName());
	return (List<E>) query.list();
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> E select(final Class<? extends Entity> entity, String identifierName, Object identifier) {
	return (E) getSession()
		.createQuery(
			new StringBuilder().append("from ").append(entity.getName()).append(" e ").append("where e.").append(identifierName).append("=:id")
				.toString())

		.setParameter("id", identifier).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> select(final String hsql, final Parameter... parameters) {
	Query query = getSession().createQuery(prepareSelect(hsql));
	if (parameters != null && parameters.length > 0) {
	    for (final Parameter parameter : parameters) {
		query.setParameter(parameter.name, parameter.value);
	    }
	}

	return (List<E>) query.list();
    }

    private String prepareSelect(final String hsqlQuery) {
	final StringBuilder prepared = new StringBuilder(hsqlQuery.trim());
	String firstWord = prepared.substring(0, prepared.indexOf(WHITESPACE)).trim();
	if (!SELECT.equalsIgnoreCase(firstWord)) {
	    prepared.insert(0, SELECT + " ");
	}
	return prepared.toString();
    }

    protected Parameter p(final String name, final Object value) {
	return new Parameter(name, value);
    }

    protected static class Parameter {
	private final String name;
	private final Object value;

	private Parameter(final String name, final Object value) {
	    this.value = value;
	    this.name = name;
	}
    }

    /**
     * Create a {@link FindResult} from a given {@link Criteria}. The result contains the total rows and the result itself.
     * 
     * @param criteria
     *            The query
     * @param distinct
     *            The row identifier. She is used to execute a count distinct
     */
    @SuppressWarnings("unchecked")
    protected <R> FindResult<R> getResults(Criteria criteria, String distinct, Order order, int first, int max) {
	final int total = ((Long) criteria.setProjection(Projections.countDistinct(distinct)).uniqueResult()).intValue();

	criteria.setProjection(null);
	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

	criteria.addOrder(order);
	criteria.setFirstResult(first);
	criteria.setMaxResults(max);
	return new FindResult<R>(total, criteria.list());
    }

    protected Session getSession() {
	return sessionFactory.getCurrentSession();
    }
}
