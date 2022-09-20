package org.trab.test.dbreststorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trab.test.dbreststorage.dao.jdbc.MinorVersionJDTO;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.Document;
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
	void jdversions() {
		Long id =docService.createPackage("<xml>dummy</xml>",1, "jdtest", "jdtest").pkgId;
		List<MinorVersionJDTO>ret=docService.getLast100("jdtest",(long) 1,"ASC");
		for(MinorVersionJDTO d:ret) {
			System.out.println(d.name);
		}
		assertTrue(1<= ret.size());
	}

	@Test
	void testJpaVersions() {
		
		TestPkg tp = docService.createPackage("<xml></xml>", 1, "jpaversions", "test");
//		System.out.println();
		//Long mivId=docService.saveMinorVersionFromMavId(tp.mavId,"latest");
//		MinorVersion toTest = docService.getMinorVersionWithMajor(mivId);
//		System.out.println(toTest.getMajorVersion().getId());
		List<MinorVersion>ret=docService.jpaGetLast100("jpaversions");
		assertTrue(1<= ret.size());
		
	}

	
	
	
	@Test
	void contextLoads() {
	}
	
/*	@Test
	void testAllMajorVersion() {
		docService.saveMajorVersionWithXml("test","<xml>test</test>");
		docService.allMajorVersionWithXmls(1);
	}
	*/
	
	@Test
	void testCreateTestPackage() {
		
		Long id =docService.createPackage("<xml>dummy</xml>",1, null,"test").pkgId;
		System.out.println("package created " +id);
		//let's check everything is persisted
		DocPackage fresh =docService.testGetAllPackageInitialized(id);
		System.out.println("number of document " +fresh.getDocuments().size());
		assertEquals(fresh.getDocuments().size(),1);
		Document d=fresh.getDocuments().get(0);
//		assertEquals(d.getMajorVersions().size(),1);
		//MajorVersion mav = d.getMajorVersions().get(0);
//		assertEquals(mav.getXmls().size(),1);
//		assertEquals(mav.getMinorVersions().size(),1);
		MinorVersion miv = d.getMinorVersions().get(0);
		assertEquals(miv.getXmls().size(),1);
		assertEquals(miv.getLatestVersion(),true);
		
	}
	
	@Test
	void testCreateTestPackage100miv() {
		
		Long id =docService.createPackage("<xml>dummy</xml>",100, null, "test").pkgId;
		System.out.println("package created " +id);
		//let's check everything is persisted
		DocPackage fresh =docService.testGetAllPackageInitialized(id);
//		System.out.println("number of document " +fresh.getDocuments().size());
//		assertEquals(fresh.getDocuments().size(),1);
		Document d=fresh.getDocuments().get(0);
//		assertEquals(d.getMajorVersions().size(),1);
		//MajorVersion mav = d.getMajorVersions().get(0);
//		assertEquals(mav.getXmls().size(),1);
		//assertEquals(mav.getMinorVersions().size(),100);
		MinorVersion miv = d.getMinorVersions().get(99); //   mav.getMinorVersions().get(99);
		assertEquals(miv.getXmls().size(),1);
		assertEquals(miv.getLatestVersion(),true);
		
		for(MinorVersion tmiv : d.getMinorVersions()) 
		{
			assertEquals(tmiv.getXmls().size(),1);
		
		}
	}

/*
	@Test
	void testSaveLatestMinorVersionFromMavId() {
		
		TestPkg tp = docService.createPackage("<xml></xml>", 1, null);
//		System.out.println();
//		Long mivId=docService.saveMinorVersionFromMavId(tp.mavId,"latest");
    
		Long mivId=docService.saveMinorVersionFromMavId(tp.mavId,"latest");
//		MinorVersion toTest = docService.getMinorVersionWithMajor(mivId);
		//System.out.println(toTest.getMajorVersion().getId());
	}
*/
	
	@Test
	void testSaveLatestMinorVersionFromCuid() {
		
		TestPkg tp = docService.createPackage("<xml></xml>", 1, "smv", "test");
//		System.out.println();
		Long mivId=docService.saveMinorVersionFromCuid("smv", "bla",false,false);
		DocPackage pkg = docService.testGetAllPackageInitialized(tp.pkgId);
		assertEquals(pkg.getDocuments().get(0).getMinorVersions().size(),2);
		//		MinorVersion toTest = docService.getLast100(cuid)(mivId);
//		System.out.println(toTest.getMajorVersion().getId());
	}
	
	@Test
	void testSaveLatestMinorVersionFromCuidJpqlUpdateLink() {
		
		TestPkg tp = docService.createPackage("<xml></xml>", 1, "jpqlsmv", "test");
//		System.out.println();
		Long mivId=docService.saveMinorVersionFromCuid("jpqlsmv", "bla",false,true);
		DocPackage pkg = docService.testGetAllPackageInitialized(tp.pkgId);
		assertEquals(pkg.getDocuments().get(0).getMinorVersions().size(),2);
		//		MinorVersion toTest = docService.getLast100(cuid)(mivId);
//		System.out.println(toTest.getMajorVersion().getId());
	}
	
}
