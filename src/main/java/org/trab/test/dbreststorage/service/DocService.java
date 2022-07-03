package org.trab.test.dbreststorage.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.MajorVersion;
import org.trab.test.dbreststorage.entity.MinorVersion;
import org.trab.test.dbreststorage.service.impl.TestPkg;

//main poc service
public interface DocService {

	public Long saveMinorVersionFromMavId(long mavId,String xml);
	public Long saveMinorVersionFromCuid(String cuid,String xml);
	
	
	public List<MajorVersion> allMajorVersionWithXmls(long mvId);

	public long saveMajorVersionWithXml(String name,String xml);
	
	public MinorVersion getMinorVersionWithMajor(long mivId);

	
	
	//MEGATON EXPENSIVE METHODS, FOR TEST SETUP
	
	
	DocPackage testGetAllPackageInitialized(Long id) ;

	/*
	 * creates test package with a document with a N version with xml provided
	 */
	public TestPkg createPackage(String xml,long minVerNum, String cuid);
	
	
	
	
}
