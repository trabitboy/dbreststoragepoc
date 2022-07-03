package org.trab.test.dbreststorage.dao;

import java.util.List;

import org.trab.test.dbreststorage.entity.Document;

public interface DocumentDao {

	void save(Document doc);
	List<Document> list();
	
	public Document findById(long idDocument);
	
	Document findDocumentWithMVById(long idDocument);
	
	List<Document> findByPackageId(long packageId);
}
