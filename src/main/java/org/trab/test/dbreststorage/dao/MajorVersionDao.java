package org.trab.test.dbreststorage.dao;

import java.util.List;

import org.trab.test.dbreststorage.entity.MajorVersion;


public interface MajorVersionDao {

	Long save(MajorVersion card);
	
	Long saveMajorVersionWithXml(String name,String xml);

	
	List<MajorVersion> list();

	List<MajorVersion> listWithXmls();
	
	List<MajorVersion> findVersionsWithDocumentId(long idDoc);
}
