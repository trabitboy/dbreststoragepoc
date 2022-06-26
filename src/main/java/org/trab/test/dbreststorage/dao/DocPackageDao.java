package org.trab.test.dbreststorage.dao;

import org.trab.test.dbreststorage.entity.DocPackage;

public interface DocPackageDao {

	public Long save(DocPackage pkg);
	public DocPackage findById(Long id);
	public DocPackage findByName(String name);
	
	
}
