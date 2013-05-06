package com.bfds.saec.batch.stale_date;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class StaleDateJpaPagingItemReader<T> extends
		AbstractPagingItemReader<T> {
	private EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager;

	private final Map<String, Object> jpaPropertyMap = new HashMap<String, Object>();

	private String queryString;

	private JpaQueryProvider queryProvider;

	private Map<String, Object> parameterValues;

	public StaleDateJpaPagingItemReader() {
		setName(ClassUtils.getShortName(StaleDateJpaPagingItemReader.class));
	}

	/**
	 * Create a query using an appropriate query provider (entityManager OR
	 * queryProvider).
	 */
	private Query createQuery() {
		if (queryProvider == null) {
			return entityManager.createQuery(queryString);
		} else {
			return queryProvider.createQuery();
		}
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * The parameter values to be used for the query execution.
	 * 
	 * @param parameterValues
	 *            the values keyed by the parameter named used in the query
	 *            string.
	 */
	public void setParameterValues(Map<String, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (queryProvider == null) {
			Assert.notNull(entityManagerFactory);
			Assert.hasLength(queryString);
		}
		// making sure that the appropriate (JPA) query provider is set
		else {
			Assert.isTrue(queryProvider != null,
					"JPA query provider must be set");
		}
	}

	/**
	 * @param queryString
	 *            JPQL query string
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @param queryProvider
	 *            JPA query provider
	 */
	public void setQueryProvider(JpaQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
	}

	@Override
	protected void doOpen() throws Exception {
		super.doOpen();

		entityManager = entityManagerFactory
				.createEntityManager(jpaPropertyMap);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException(
					"Unable to obtain an EntityManager");
		}
		// set entityManager to queryProvider, so it participates
		// in JpaPagingItemReader's managed transaction
		if (queryProvider != null) {
			queryProvider.setEntityManager(entityManager);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doReadPage() {

		// EntityTransaction tx = entityManager.getTransaction();
		// if(tx.isActive()) {
		// tx.commit();
		// }
		// tx.begin();
		//
		// entityManager.flush();
		// entityManager.clear();

		Query query = createQuery().setFirstResult(getPage() * getPageSize())
				.setMaxResults(getPageSize());

		if (parameterValues != null) {
			for (Map.Entry<String, Object> me : parameterValues.entrySet()) {
				query.setParameter(me.getKey(), me.getValue());
			}
		}

		if (results == null) {
			results = new CopyOnWriteArrayList<T>();
		} else {
			results.clear();
		}
		results.addAll(query.getResultList());

		// tx.commit();
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}

	@Override
	protected void doClose() throws Exception {
		entityManager.close();
		super.doClose();
	}

}
