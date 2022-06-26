package org.trab.test.dbreststorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.Document;
import org.trab.test.dbreststorage.entity.MajorVersion;
import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.service.DocService;

@SpringBootTest
class DbreststorageApplicationTests {

	@Autowired
	DocService docService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void testAllMajorVersion() {
		docService.saveMajorVersionWithXml("test","<xml>test</test>");
		docService.allMajorVersionWithXmls(1);
	}
	
	@Test
	void testCreateTestPackage() {
		
		Long id =docService.createPackage("<xml>dummy</xml>");
		System.out.println("package created " +id);
		//let's check everything is persisted
		DocPackage fresh =docService.testGetAllPackageInitialized(id);
		System.out.println("number of document " +fresh.getDocuments().size());
		assertEquals(fresh.getDocuments().size(),1);
		Document d=fresh.getDocuments().get(0);
		assertEquals(d.getMajorVersions().size(),1);
		MajorVersion mav = d.getMajorVersions().get(0);
		assertEquals(mav.getXmls().size(),1);
		assertEquals(mav.getMinorVersions().size(),1);
		MinorVersion miv = mav.getMinorVersions().get(0);
		assertEquals(miv.getXmls().size(),1);
		
	}
	
	

}
