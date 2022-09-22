package org.trab.test.dbreststorage.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.DocPackageDao;
import org.trab.test.dbreststorage.dao.DocumentDao;
import org.trab.test.dbreststorage.dao.MinorVersionDao;
import org.trab.test.dbreststorage.dao.jdbc.MetadataXmlRow;
import org.trab.test.dbreststorage.dao.jdbc.MinorVersionJDTO;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.Document;

import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.entity.XmlContent;
import org.trab.test.dbreststorage.service.DocService;

//just here for transactionality



@Service
public class DocServiceImpl implements DocService {
	
	
	
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	DocumentDao dDocumentDao;
	
	@Autowired
	DocPackageDao docPackageDao;
	

	@Autowired
	MinorVersionDao minorVersionDao;
	


	//atomic
	@Transactional(propagation =  Propagation.REQUIRES_NEW)
	public TestPkg createPackage(String xml,long minVerNum,String cuid, String cuid_batch) {

		//XmlContent xc=new XmlContent();
//		xc.setXml(xml);
//		
//
//		MinorVersion miv = new MinorVersion();
		
/*		MajorVersion mav =new MajorVersion();
		mav.getXmls().add(xc);
		xc.setMajorVersion(mav);
		mav.setName("test");

		
		doc.getMajorVersions().add(mav);
		mav.setDocument(doc);
*/

		Document doc=new Document();
		doc.setName(cuid_batch);
		doc.setCuid(cuid);
		DocPackage pkg=new DocPackage();
		pkg.setName(cuid_batch);	
		
		//		doc.get().add(mav);
		//mav.setDocument(doc);

		for (int i=1;i<=minVerNum;i++) {
			XmlContent xcmiv=new XmlContent();
			xcmiv.setXml(xml);
			MinorVersion miv1 =new MinorVersion();
			miv1.setVersion(i);
			miv1.setName("test"+i);			
			miv1.getXmls().add(xcmiv);
			xcmiv.setMinorVersion(miv1);
			if (i==1) {
				miv1.setOldestVersion(true);
			}
			if (i==minVerNum)
			{
				miv1.setLatestVersion(true);
			}
			doc.getMinorVersions().add(miv1);
			miv1.setDocument(doc);
		}
		
		
		
		

		pkg.getDocuments().add(doc);
		doc.setDocPackage(pkg);
		
		 Long created = docPackageDao.save(pkg);
		 entityManager.flush(); //sql executed, ids generated
		 
		 return new TestPkg(    created, 
				 doc.getMinorVersions().get((int) (minVerNum-1)).getId()	,
				 doc.getMinorVersions().get((int) (1)).getId()	
				 //miv.getMinorVersionNumber().get((int) (minVerNum-1)).getId()
				 );
	}
	
	//MEGATON EXPENSIVE
	@Transactional
	public DocPackage testGetAllPackageInitialized(Long id) {
		DocPackage dp= docPackageDao.findById(id);
		Hibernate.initialize(dp.getDocuments());
		for(Document d : dp.getDocuments()) {
			//Hibernate.initialize(d.getMajorVersions());
			Hibernate.initialize(d.getMinorVersions());
			for(MinorVersion miv : d.getMinorVersions()) {
				Hibernate.initialize(miv.getXmls());
				
			}
			/*
			for(MajorVersion mav : d.getMajorVersions()) {
				Hibernate.initialize(mav.getMinorVersions());
				Hibernate.initialize(mav.getXmls());


			}*/
			
		}
		
		return dp;
	}
/*
	
	@Transactional
	public Long saveMinorVersionFromMavId(long mavId, String xml) {
//		miVD
//		MinorVersion prevLatest = minorVersionDao.getLatestMinorVersionFromMavId(mavId);
		prevLatest.setLatestVersion(false);
		
		MinorVersion nlv = new MinorVersion();
		
		nlv.setLatestVersion(true);
		//TODO save xml
		minorVersionDao.save(nlv);
		entityManager.flush();
		//now for the link
		//known solution from hibernate in action
		entityManager.createNativeQuery("update minor_version set major_version_id="+mavId+" WHERE id="+nlv.getId()).executeUpdate();
		

		
		return nlv.getId();
	}
*/
	@Transactional
	public Long saveMinorVersionFromCuid(String cuid, String xml,boolean testExtraWait,boolean jpql) {
		
		System.out.println("cuid "+cuid);
		MinorVersion prevLatest = minorVersionDao.getLatestMinorVersionFromCuid(cuid);
		entityManager.lock(prevLatest, LockModeType.OPTIMISTIC);
		//with the above line, hibernate executes an extra:
		//org.hibernate.SQL                        : select version as version_ from minor_version where id =?
		//to check if object was touched in the meantime
		
		prevLatest.setLatestVersion(false); //when this item is updated on flush/transaction end, it will trigger optimistic lock check 
		
		MinorVersion nlv = new MinorVersion();
		
		nlv.setLatestVersion(true);
		XmlContent xc = new XmlContent();
		xc.setXml(xml);
		nlv.getXmls().add(xc);
		xc.setMinorVersion(nlv);
		minorVersionDao.save(nlv);
		
		entityManager.flush();
		//now for the link 
		//known solution from hibernate in action
		
		//TODO check overhead of get id ( do we get all maj version// colls?
		long mavid = prevLatest.getId();
		long docid = prevLatest.getDocument().getId();
		//TODO jpql would make it portable

		if (jpql) {
			Query q=entityManager.createQuery("UPDATE MinorVersion miv SET miv.document=?1 where miv.id=?2");
			q.setParameter(1, docid);
			System.out.println("docid "+docid);
			q.setParameter(2, nlv.getId());			
			System.out.println("nlv.getId() "+nlv.getId());
			int count=q.executeUpdate();
			System.out.println(count +" updated miv link from jpql");
//			entityManager.executeU("update minor_version set major_version_id="+mavid+",document_id="+docid+" WHERE id="+nlv.getId()).executeUpdate();
		}else {
			entityManager.createNativeQuery("update minor_version set document_id="+docid+" WHERE id="+nlv.getId()).executeUpdate();
			System.out.println("link done with direct sql");
		}
		
		//wait 10 seconds for concurrency test
		if (testExtraWait) {
			try {
				System.out.println("wait begin");
				TimeUnit.SECONDS.sleep(10);
				System.out.println("wait over");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return nlv.getId();
	}

	@Transactional(readOnly = true)
	public List<MinorVersionJDTO> getLast100(String cuid, Long limit, String orderBy) {
		return minorVersionDao.jdGetLast100Versiont(cuid, limit, orderBy);
	}

	@Transactional(readOnly = true)
	public List<MetadataXmlRow> getFirstLast(String cuid, String position) {
		return minorVersionDao.jdGetFirstLast(cuid, position);
	}
	
	@Transactional(readOnly = true)
	public List<MinorVersion> jpaGetLast100(String cuid) {
		return minorVersionDao.jpaGetLast100Version(cuid);
	}
	
	//TO-DO alternate method with manual optimistic lock implementation ^^ (for fun and profit) with sub method
	// that polls outside transaction context using propagation 'requires new'

	
}