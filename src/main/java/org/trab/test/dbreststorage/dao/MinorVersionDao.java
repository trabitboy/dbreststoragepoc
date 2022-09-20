package org.trab.test.dbreststorage.dao;

import java.util.List;

import org.trab.test.dbreststorage.dao.jdbc.MinorVersionJDTO;
import org.trab.test.dbreststorage.entity.MinorVersion;


public interface MinorVersionDao {

	MinorVersion find(long id);
	
	Long save(MinorVersion miv);
	
	Long saveMinorVersionWithXml(String name,String xml);
	
	List<MinorVersion> list();

	List<MinorVersion> listWithXmls();
	
	List<MinorVersion> findVersionsWithDocumentId(long idDoc);

	MinorVersion getLatestMinorVersionFromCuid(String cuid);
	
	List<MinorVersionJDTO> jdGetLast100Versiont(String cuid, Long limit, String orderBy);

	List<MinorVersion> jpaGetLast100Version(String cuid);

	
}
