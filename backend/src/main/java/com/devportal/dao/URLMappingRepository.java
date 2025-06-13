package com.devportal.dao;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.devportal.bean.URLMapping;
import com.devportal.constants.URLConstants;
import com.devportal.util.Util;

@Repository
public class URLMappingRepository {

	public URLMapping findByShortCode(String shortCode, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<URLMapping> cr = cb.createQuery(URLMapping.class);

		Root<URLMapping> root = cr.from(URLMapping.class);
		cr.select(root).where(cb.equal(root.get(URLConstants.SHORT_CODE), shortCode));

		try {
			return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
		} catch (NoResultException e) {
			Util.printError(MessageFormat.format("Data not found for short code: {0}", shortCode));
		}

		return null;
	}

	public URLMapping findByOriginalUrl(String originalUrl, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<URLMapping> cr = cb.createQuery(URLMapping.class);

		Root<URLMapping> root = cr.from(URLMapping.class);
		cr.select(root).where(cb.equal(root.get(URLConstants.ORIGINAL_URL), originalUrl));

		try {
			return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
		} catch (NoResultException e) {
			Util.printError(MessageFormat.format("Data not found for original url: {0}", originalUrl));
		}

		return null;
	}

	public void save(URLMapping mapping, SessionFactory sessionFactory) {
		sessionFactory.getCurrentSession().save(mapping);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	public boolean isShortCodeExist(String shortCode, SessionFactory sessionFactory) {
		return (boolean) sessionFactory.getCurrentSession()
				.createSQLQuery("select count(1) > 0 from projects.url_mapping where short_code =:shortCode")
				.setParameter(URLConstants.SHORT_CODE, shortCode).uniqueResult();
	}

	public List<URLMapping> getAllURLMappings(int offset, int limit, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<URLMapping> cr = cb.createQuery(URLMapping.class);

		Root<URLMapping> root = cr.from(URLMapping.class);
		cr.select(root);

		cr.orderBy(cb.asc(root.get(URLConstants.CREATED_AT)));

		return sessionFactory.getCurrentSession().createQuery(cr).setFirstResult(offset).setMaxResults(limit)
				.getResultList();
	}

	public Long getAllURLMappingsCount(SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> cr = cb.createQuery(Long.class);

		Root<URLMapping> root = cr.from(URLMapping.class);
		cr.select(cb.count(root.get(URLConstants.SHORT_CODE)));

		return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
	}

	public void updateHitCount(String shortCode, SessionFactory sessionFactory) {
		sessionFactory.getCurrentSession()
				.createSQLQuery(
						"update projects.url_mapping set hit_count = hit_count + 1 where short_code =:shortCode")
				.setParameter(URLConstants.SHORT_CODE, shortCode).executeUpdate();
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	public String findUrlValue(String selectField, String filterField, String filterValue,
			SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);

		Root<URLMapping> root = query.from(URLMapping.class);
		query.select(root.get(selectField)).where(cb.equal(root.get(filterField), filterValue));

		try {
			return sessionFactory.getCurrentSession().createQuery(query).getSingleResult();
		} catch (NoResultException e) {
			Util.printError(MessageFormat.format("Data not found for {0} using field {1}, with filter value {2}",
					selectField, filterField, filterValue));
		}

		return null;
	}

	public void updateHitCount(String shortCode, Long count, SessionFactory sessionFactory) {
		sessionFactory.getCurrentSession()
				.createSQLQuery(
						"update projects.url_mapping set hit_count = hit_count + :count where short_code =:shortCode")
				.setParameter("count", count).setParameter(URLConstants.SHORT_CODE, shortCode).executeUpdate();
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

}
