package com.devportal.dao;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.devportal.bean.UrlMapping;

@Repository
public class UrlMappingRepository {

	private Logger LOGGER = LoggerFactory.getLogger(UrlMappingRepository.class);

	public UrlMapping findByShortCode(String shortCode, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<UrlMapping> cr = cb.createQuery(UrlMapping.class);

		Root<UrlMapping> root = cr.from(UrlMapping.class);
		cr.select(root).where(cb.equal(root.get("shortCode"), shortCode));

		try {
			return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
		} catch (NoResultException e) {
			LOGGER.error(MessageFormat.format("Data not found for short code: {0}", shortCode));
		}

		return null;
	}

	public UrlMapping findByOriginalUrl(String originalUrl, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<UrlMapping> cr = cb.createQuery(UrlMapping.class);

		Root<UrlMapping> root = cr.from(UrlMapping.class);
		cr.select(root).where(cb.equal(root.get("originalUrl"), originalUrl));

		try {
			return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
		} catch (NoResultException e) {
			LOGGER.error(MessageFormat.format("Data not found for original url: {0}", originalUrl));
		}

		return null;
	}

	public void save(UrlMapping mapping, SessionFactory sessionFactory) {
		sessionFactory.getCurrentSession().save(mapping);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	public boolean isShortCodeExist(String shortCode, SessionFactory sessionFactory) {
		return (boolean) sessionFactory.getCurrentSession()
				.createSQLQuery("select count(1) > 0 from projects.url_mapping where short_code =:shortCode")
				.setParameter("shortCode", shortCode).uniqueResult();
	}

	public List<UrlMapping> getAllMappings(int offset, int limit, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<UrlMapping> cr = cb.createQuery(UrlMapping.class);

		Root<UrlMapping> root = cr.from(UrlMapping.class);
		cr.select(root);

		return sessionFactory.getCurrentSession().createQuery(cr).setFirstResult(offset).setMaxResults(limit)
				.getResultList();
	}

	public Long getAllMappingsCount(SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> cr = cb.createQuery(Long.class);

		Root<UrlMapping> root = cr.from(UrlMapping.class);
		cr.select(cb.count(root.get("shortCode")));

		return sessionFactory.getCurrentSession().createQuery(cr).getSingleResult();
	}

	public void updateHitCount(String shortCode, int hitCount, SessionFactory sessionFactory) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<UrlMapping> cr = cb.createCriteriaUpdate(UrlMapping.class);

		Root<UrlMapping> root = cr.from(UrlMapping.class);

		cr.set("hitCount", hitCount);
		cr.where(cb.equal(root.get("shortCode"), shortCode));

		sessionFactory.getCurrentSession().createQuery(cr).executeUpdate();

	}

}
