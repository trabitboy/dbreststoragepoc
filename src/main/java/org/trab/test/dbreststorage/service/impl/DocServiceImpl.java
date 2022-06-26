package org.trab.test.dbreststorage.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.dao.DocPackageDao;
import org.trab.test.dbreststorage.dao.DocumentDao;
import org.trab.test.dbreststorage.dao.MajorVersionDao;
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
	DocumentDao dDocumentDao;
	
	@Autowired
	DocPackageDao docPackageDao;
	
	@Autowired
	MajorVersionDao majorVersionDao;

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
	public Long createPackage(String xml) {

		XmlContent xc=new XmlContent();
		xc.setXml(xml);
//		
		MinorVersion miv =new MinorVersion();
		miv.getXmls().add(xc);
		xc.setMinorVersion(miv);
//
		MajorVersion mav =new MajorVersion();
		mav.getXmls().add(xc);
		xc.setMajorVersion(mav);
		mav.getMinorVersions().add(miv);
		miv.setMajorVersion(mav);
		mav.setName("test");

		
		Document doc=new Document();
		doc.setName("test");
		doc.getMajorVersions().add(mav);
		mav.setDocument(doc);
		
		DocPackage pkg=new DocPackage();
		pkg.setName("test");	
		pkg.getDocuments().add(doc);
		doc.setDocPackage(pkg);
		
		return docPackageDao.save(pkg);

	}
	
	//MEGATON EXPENSIVE
	@Transactional
	public DocPackage testGetAllPackageInitialized(Long id) {
		DocPackage dp= docPackageDao.findById(id);
		Hibernate.initialize(dp.getDocuments());
		for(Document d : dp.getDocuments()) {
			Hibernate.initialize(d.getMajorVersions());
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
	
	
	//TODO
	public static void logPackage(DocPackage dp) {
		
	
	}
}