package org.trab.test.dbreststorage.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.trab.test.dbreststorage.entity.DocPackage;
import org.trab.test.dbreststorage.entity.MajorVersion;

//main poc service
public interface DocService {

	
	/*
	 * creates test package with a document with a minor version with xml provided
	 */
	public Long createPackage(String xml);
	
	
	public List<MajorVersion> allMajorVersionWithXmls(long mvId);

	public long saveMajorVersionWithXml(String name,String xml);
	//MEGATON EXPENSIVE
	DocPackage testGetAllPackageInitialized(Long id) ;

//	MajorVersion getMajorVersion(long mvId);
	
}
