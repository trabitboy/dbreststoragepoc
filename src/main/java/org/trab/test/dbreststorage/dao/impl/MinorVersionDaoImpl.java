package org.trab.test.dbreststorage.dao.impl;

import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.MinorVersionDao;
import org.trab.test.dbreststorage.dao.jdbc.JMinorVersionDTOMapper;
import org.trab.test.dbreststorage.dao.jdbc.MinorVersionJDTO;
import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.entity.XmlContent;
import org.trab.test.dbreststorage.util.AbstractHibernateDao;

@Repository("MinorVersionDao")
public class MinorVersionDaoImpl extends AbstractHibernateDao implements MinorVersionDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	
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


	//NOT WORKING , sub criteria necessary
	@SuppressWarnings("unchecked")
	public List<MinorVersion> findVersionsWithDocumentId(long idDoc){
		List<MinorVersion> toReturn = this.getCurrentSession().createCriteria(MinorVersion.class).add(Restrictions.eq("document.id", new Long(idDoc))).list();
		return toReturn;
	}
/*
	@Override
	public MinorVersion getLatestMinorVersionFromMavId(long mavId) {
		Criteria myCrit = this.getCurrentSession().createCriteria(MinorVersion.class);
		myCrit.add(Restrictions.eq("majorVersion.id", new Long(mavId)));
		myCrit.add(Restrictions.eq("latestVersion", true));

		List<MinorVersion> toReturn = myCrit.list();
		return toReturn.get(0);
	}

*/
	
	//TODO not finished, JPA rewrite to get rid of warning
	public MinorVersion getLatestMinorVersionFromCuidJPA(String cuid) {
		CriteriaBuilder cb=entityManager.getCriteriaBuilder();
		CriteriaQuery<MinorVersion> myCrit=cb.createQuery(MinorVersion.class);

		Root<MinorVersion> root = myCrit.from(MinorVersion.class);
//		SetJoin<MinorVersion, MajorVersion> miv = root.join(Author_.books);
//		 
//		ParameterExpression<String> paramTitle = cb.parameter(String.class);
//		cq.where(cb.like(books.get(Book_.title), paramTitle));		
//		
//		
		List<MinorVersion> toReturn = entityManager.createQuery(myCrit).getResultList();
		return toReturn.get(0);
	}	

	@Override
	public MinorVersion getLatestMinorVersionFromCuid(String cuid) {
		Criteria myCrit = this.getCurrentSession().createCriteria(MinorVersion.class);
		myCrit.add(Restrictions.eq("latestVersion", true));
		//myCrit=myCrit.createCriteria("majorVersion");
		myCrit=myCrit.createCriteria("document");
		myCrit.add(Restrictions.eq("cuid", cuid));
		List<MinorVersion> toReturn = myCrit.list();
		return toReturn.get(0);
	}


	//TODO for some reason works with number id or no where (both controller and test)
	// but embedding string cuid in where does not work
	@Override
	public List<MinorVersionJDTO> jdGetLast100Versiont(String cuid) {
		
		
		//WIP works from test but not controller, lets try to rewrite it without a join
		//fetch document id, then fetch last versions
		String realSql= "	select this_.id as id1_3_5_," + 
				"	 this_.name as NAME, document1.id as DID" + 
				"	 from minor_version this_ inner join document document1 on this_.document_id=document1.id" +
//				" where document1.cuid= \"select_cuid3\" " +
//				"	 where document1_.cuid='"+cuid+"' limit 100" + without where it works
				" limit 100";
		
		
//		String  helloSql = "select NAME from MINOR_VERSION";
		
		return jdbcTemplate.query(realSql ,new JMinorVersionDTOMapper());
	}

//TODO trimmed down sql output to put above
//	select this_.id as id1_3_5_,
//	 this_.name as name4_3_5_, 
//	 from minor_version this_ inner join document document1_ on this_.document_id=document1_.id 
//	 where document1_.cuid='test_cuid3' limit 5	
	
	


	@Override
	public List<MinorVersion> jpaGetLast100Version(String cuid) {
		Criteria crit = this.getCurrentSession().createCriteria(MinorVersion.class);
		crit=crit.createCriteria("document");
		crit=crit.add(Restrictions.eq("cuid", cuid));

		crit=crit.setMaxResults(100);
		List<MinorVersion> toReturn = crit.list();
		return toReturn;
	}	
}
