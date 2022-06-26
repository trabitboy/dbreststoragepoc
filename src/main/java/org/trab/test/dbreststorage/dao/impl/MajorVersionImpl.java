package org.trab.test.dbreststorage.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.MajorVersionDao;
import org.trab.test.dbreststorage.entity.MajorVersion;
import org.trab.test.dbreststorage.entity.XmlContent;
import org.trab.test.dbreststorage.util.AbstractHibernateDao;

@Repository("majorVersionDao")
@Transactional
public class MajorVersionImpl extends AbstractHibernateDao implements MajorVersionDao {

	public Long save(MajorVersion mv) {
		return (Long) this.getCurrentSession().save(mv);
	}

	/**
	 * cascade all works only with objects attached to the session I think
	 */
	public Long saveMajorVersionWithXml(String name,String xml) {
		MajorVersion mv = new MajorVersion();
		mv.setName(name);
		XmlContent xc= new XmlContent();
		xc.setXml(xml);
		mv.getXmls().add(xc);
		xc.setMajorVersion(mv);
		return (Long) this.getCurrentSession().save(mv);
		
	}
	
	
	/**
	 */
	@SuppressWarnings("unchecked")
	public List<MajorVersion> list() {
		Criteria crit = this.getCurrentSession().createCriteria(MajorVersion.class);
		List<MajorVersion> toReturn = crit.list();
		
		return toReturn;
		
	}

	@SuppressWarnings("unchecked")
	public List<MajorVersion> listWithXmls() {
		Criteria crit = this.getCurrentSession().createCriteria(MajorVersion.class);
		List<MajorVersion> toReturn = crit.list();
		
		for(MajorVersion mv : toReturn) {
			Hibernate.initialize(mv.getXmls());
		}
		
		return toReturn;
		
	}

	
	@SuppressWarnings("unchecked")
	public List<MajorVersion> findVersionsWithDocumentId(long idDoc){
		List<MajorVersion> toReturn = this.getCurrentSession().createCriteria(MajorVersion.class).add(Restrictions.eq("document.id", new Long(idDoc))).list();
		return toReturn;
	}	
}
