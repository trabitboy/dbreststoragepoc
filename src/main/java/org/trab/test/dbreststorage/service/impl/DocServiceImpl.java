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
import org.trab.test.dbreststorage.dao.MajorVersionDao;
import org.trab.test.dbreststorage.dao.MinorVersionDao;
import org.trab.test.dbreststorage.dao.jdbc.MinorVersionJDTO;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.Document;
import org.trab.test.dbreststorage.entity.MajorVersion;
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
	MajorVersionDao majorVersionDao;

	@Autowired
	MinorVersionDao minorVersionDao;
	
	
	
	@Transactional
	public List<MajorVersion> allMajorVersionWithXmls(long mvId) {
		return majorVersionDao.listWithXmls();
	}

	//atomic
	@Transactional(propagation =  Propagation.REQUIRES_NEW)
	public long saveMajorVersionWithXml(String name, String xml) {
		return majorVersionDao.saveMajorVersionWithXml(name, xml);
	}

	//atomic
	@Transactional(propagation =  Propagation.REQUIRES_NEW)
	public TestPkg createPackage(String xml,long minVerNum,String cuid) {

		XmlContent xc=new XmlContent();
		xc.setXml(xml);
//		
//
		MajorVersion mav =new MajorVersion();
		mav.getXmls().add(xc);
		xc.setMajorVersion(mav);
		mav.setName("test");

		Document doc=new Document();
		doc.setName("test");
		doc.setCuid(cuid);
		doc.getMajorVersions().add(mav);
		mav.setDocument(doc);

		
		for (int i=1;i<=minVerNum;i++) {
			XmlContent xcmiv=new XmlContent();
			xcmiv.setXml(xml);
			MinorVersion miv =new MinorVersion();
			miv.setName("test"+i);
			miv.getXmls().add(xcmiv);
			xcmiv.setMinorVersion(miv);
			mav.getMinorVersions().add(miv);
			miv.setMajorVersion(mav);
			if (i==minVerNum)
			{
				miv.setLatestVersion(true);
			}
			doc.getMinorVersions().add(miv);
			miv.setDocument(doc);
		}
		
		
		
		
		DocPackage pkg=new DocPackage();
		pkg.setName("test");	
		pkg.getDocuments().add(doc);
		doc.setDocPackage(pkg);
		
		 Long created = docPackageDao.save(pkg);
		 entityManager.flush(); //sql executed, ids generated
		 
		 return new TestPkg(created, mav.getId(), mav.getMinorVersions().get((int) (minVerNum-1)).getId());
	}
	
	//MEGATON EXPENSIVE
	@Transactional
	public DocPackage testGetAllPackageInitialized(Long id) {
		DocPackage dp= docPackageDao.findById(id);
		Hibernate.initialize(dp.getDocuments());
		for(Document d : dp.getDocuments()) {
			Hibernate.initialize(d.getMajorVersions());
			Hibernate.initialize(d.getMinorVersions());
			for(MajorVersion mav : d.getMajorVersions()) {
				Hibernate.initialize(mav.getMinorVersions());
				Hibernate.initialize(mav.getXmls());
				for(MinorVersion miv : mav.getMinorVersions()) {
					Hibernate.initialize(miv.getXmls());
					
				}

			}
			
		}
		
		return dp;
	}

	@Transactional
	public MinorVersion getMinorVersionWithMajor(long mivId) {
		MinorVersion prevLatest = minorVersionDao.find(mivId);
		Hibernate.initialize(prevLatest.getMajorVersion());
		return prevLatest;
	}
	
	@Transactional
	public Long saveMinorVersionFromMavId(long mavId, String xml) {
//		miVD
		MinorVersion prevLatest = minorVersionDao.getLatestMinorVersionFromMavId(mavId);
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

	@Transactional
	public Long saveMinorVersionFromCuid(String cuid, String xml,boolean testExtraWait,boolean jpql) {
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
		minorVersionDao.save(nlv);
		
		entityManager.flush();
		//now for the link 
		//known solution from hibernate in action
		
		//TODO check overhead of get id ( do we get all maj version// colls?
		long mavid = prevLatest.getMajorVersion().getId();
		long docid = prevLatest.getDocument().getId();
		//TODO jpql would make it portable

		if (jpql) {
			Query q=entityManager.createQuery("UPDATE MinorVersion miv SET miv.document.id=?1,miv.majorVersion.id=?2 where miv.id=?3");
			q.setParameter(1, docid);
			q.setParameter(2, mavid);
			q.setParameter(3, nlv.getId());			
			int count=q.executeUpdate();
			System.out.println(count +" updated miv from jpql");
//			entityManager.executeU("update minor_version set major_version_id="+mavid+",document_id="+docid+" WHERE id="+nlv.getId()).executeUpdate();
		}else {
			entityManager.createNativeQuery("update minor_version set major_version_id="+mavid+",document_id="+docid+" WHERE id="+nlv.getId()).executeUpdate();
			
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
	public List<MinorVersionJDTO> getLast100(String cuid) {
		return minorVersionDao.jdGetLast100Versiont(cuid);
	}

	@Transactional(readOnly = true)
	public List<MinorVersion> jpaGetLast100(String cuid) {
		return minorVersionDao.jpaGetLast100Version(cuid);
	}
	
	//TODO alternate method with manual optimistic lock implementation ^^ (for fun and profit) with sub method
	// that polls outside transaction context using propagation 'requires new'

	
}