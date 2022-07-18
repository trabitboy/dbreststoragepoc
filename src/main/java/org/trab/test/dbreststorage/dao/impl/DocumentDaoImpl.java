package org.trab.test.dbreststorage.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.DocumentDao;
import org.trab.test.dbreststorage.entity.Document;
import org.trab.test.dbreststorage.util.AbstractHibernateDao;

@Repository("documentDao")
@Transactional
public class DocumentDaoImpl extends AbstractHibernateDao implements DocumentDao{

	private static final Logger LOG = LoggerFactory.getLogger(DocumentDaoImpl.class) ;
	 
	public void save(final Document doc) {
		this.getCurrentSession().saveOrUpdate(doc);
	}

	@SuppressWarnings("unchecked")
	public List<Document> list() {
		LOG.debug("-- begin list");
		Criteria criteria = this.getCurrentSession().createCriteria(Document.class);
		List<Document> toReturn =  criteria.list();
		LOG.debug("-- ran list !");
		return toReturn;
	}

	public Document findById(long id) {
		return (Document) this.getCurrentSession().get(Document.class, id);
		
	}

	
	public Document findDocumentWithMVById(long idDocument){
		
		Document toReturn = (Document) this.getCurrentSession().get(Document.class, idDocument);
		
		//Hibernate.initialize(toReturn.getMajorVersions()); 
		Hibernate.initialize(toReturn.getMinorVersions());
		
		return toReturn;
	}

	/*
	 * basic example to filter with the other class's properties here :
	 * http://stackoverflow.com/questions/1787086/hibernate-query-a-foreign-key-field-with-id
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Document> findByPackageId(long packageId) {
		Criteria crit = this.getCurrentSession().createCriteria(Document.class);
		
		crit.add(Restrictions.eq("docPackage.id", packageId));
		
		return (List<Document>)(crit.list());
	}
	
	
}
