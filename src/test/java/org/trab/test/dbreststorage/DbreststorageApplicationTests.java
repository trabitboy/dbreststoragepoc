package org.trab.test.dbreststorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.Document;
import org.trab.test.dbreststorage.entity.MajorVersion;
import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.jdbc.template.JdbcTest;
import org.trab.test.dbreststorage.service.DocService;
import org.trab.test.dbreststorage.service.impl.TestPkg;

@SpringBootTest
class DbreststorageApplicationTests {

	@Autowired
	DocService docService;
	
	@Autowired
	JdbcTest jtest;
	
	
	@Test
	void jdhello() {
		jtest.doSomething();
		
	}
	
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
		
		Long id =docService.createPackage("<xml>dummy</xml>",1, null).pkgId;
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
		assertEquals(miv.getLatestVersion(),true);
		
	}
	
	@Test
	void testCreateTestPackage100miv() {
		
		Long id =docService.createPackage("<xml>dummy</xml>",100, null).pkgId;
		System.out.println("package created " +id);
		//let's check everything is persisted
		DocPackage fresh =docService.testGetAllPackageInitialized(id);
//		System.out.println("number of document " +fresh.getDocuments().size());
//		assertEquals(fresh.getDocuments().size(),1);
		Document d=fresh.getDocuments().get(0);
//		assertEquals(d.getMajorVersions().size(),1);
		MajorVersion mav = d.getMajorVersions().get(0);
//		assertEquals(mav.getXmls().size(),1);
		assertEquals(mav.getMinorVersions().size(),100);
		MinorVersion miv = mav.getMinorVersions().get(99);
		assertEquals(miv.getXmls().size(),1);
		assertEquals(miv.getLatestVersion(),true);
		
		for(MinorVersion tmiv : mav.getMinorVersions()) {
			assertEquals(tmiv.getXmls().size(),1);
		
		}
	}
	
	@Test
	void testSaveLatestMinorVersionFromMavId() {
		
		TestPkg tp = docService.createPackage("<xml></xml>", 1, null);
//		System.out.println();
		Long mivId=docService.saveMinorVersionFromMavId(tp.mavId,"latest");
		MinorVersion toTest = docService.getMinorVersionWithMajor(mivId);
		System.out.println(toTest.getMajorVersion().getId());
	}

}
