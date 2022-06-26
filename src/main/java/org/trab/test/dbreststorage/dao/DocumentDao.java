package org.trab.test.dbreststorage.dao;

import java.util.List;

import org.trab.test.dbreststorage.entity.Document;

public interface DocumentDao {

	void save(Document deck);
	List<Document> list();
	
	public Document findById(long idDocument);
	
	/**
	 * Returns the decks with all its cards
	 * @param idDeck
	 * @return Deck
	 */
	Document findDocumentWithMVById(long idDocument);
	
	/**
	 * Returns the deck from the owner
	 * @param playerId
	 * @return List<Deck>
	 */
	List<Document> findByPackageId(long packageId);
}
