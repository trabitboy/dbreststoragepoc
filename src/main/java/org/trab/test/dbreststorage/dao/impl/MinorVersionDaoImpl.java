package org.trab.test.dbreststorage.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.MinorVersionDao;
import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.entity.XmlContent;
import org.trab.test.dbreststorage.util.AbstractHibernateDao;

@Repository("MinorVersionDao")
public class MinorVersionDaoImpl extends AbstractHibernateDao implements MinorVersionDao {

	
	public MinorVersion find(long id) {
		return (MinorVersion) this.getCurrentSession().find(MinorVersion.class, id);
	}
	
	
	
	public Long save(MinorVersion mv) {
		return (Long) this.getCurrentSession().save(mv);
	}

	/**
	 * cascade all works only with objects attached to the session I think
	 */
	public Long saveMinorVersionWithXml(String name,String xml) {
		MinorVersion mv = new MinorVersion();
		mv.setName(name);
		XmlContent xc= new XmlContent();
		xc.setXml(xml);
		mv.getXmls().add(xc);
		xc.setMinorVersion(mv);
		return (Long) this.getCurrentSession().save(mv);
		
	}
	
	
	/**
	 */
	@SuppressWarnings("unchecked")
	public List<MinorVersion> list() {
		Criteria crit = this.getCurrentSession().createCriteria(MinorVersion.class);
		List<MinorVersion> toReturn = crit.list();
		
		return toReturn;
		
	}

	@SuppressWarnings("unchecked")
	public List<MinorVersion> listWithXmls() {
		Criteria crit = this.getCurrentSession().createCriteria(MinorVersion.class);
		List<MinorVersion> toReturn = crit.list();
		
		for(MinorVersion mv : toReturn) {
			Hibernate.initialize(mv.getXmls());
		}
		
		return toReturn;
		
	}

	
	@SuppressWarnings("unchecked")
	public List<MinorVersion> findVersionsWithDocumentId(long idDoc){
		List<MinorVersion> toReturn = this.getCurrentSession().createCriteria(MinorVersion.class).add(Restrictions.eq("document.id", new Long(idDoc))).list();
		return toReturn;
	}

	@Override
	public MinorVersion getLatestMinorVersionFromMavId(long mavId) {
		Criteria myCrit = this.getCurrentSession().createCriteria(MinorVersion.class);
		myCrit.add(Restrictions.eq("majorVersion.id", new Long(mavId)));
		myCrit.add(Restrictions.eq("latestVersion", true));

		List<MinorVersion> toReturn = myCrit.list();
		return toReturn.get(0);
	}



	@Override
	public MinorVersion getLatestMinorVersionFromCuid(String cuid) {
		Criteria myCrit = this.getCurrentSession().createCriteria(MinorVersion.class);
		myCrit.add(Restrictions.eq("latestVersion", true));
		myCrit=myCrit.createCriteria("majorVersion");
		myCrit=myCrit.createCriteria("document");
		myCrit.add(Restrictions.eq("cuid", cuid));
		List<MinorVersion> toReturn = myCrit.list();
		return toReturn.get(0);
	}	
}
