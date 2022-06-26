package org.trab.test.dbreststorage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




/**
 *
 */

@Entity
@Table(name = "DOCUMENT")
public class Document {

	@SequenceGenerator(
	        name="DOCUMENT_SEQUENCE_GENERATOR",
	        sequenceName="DOCUMENT_SEQ",
	        initialValue=1,
	        allocationSize=1
	    )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DOCUMENT_SEQUENCE_GENERATOR")
	@Id
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	/**
	 * owning side of the relationship with player
	 */
	@ManyToOne(optional=true)
	@JoinColumn(name = "PACKAGE_ID", nullable = true, unique = false, updatable = true)
	private DocPackage docPackage;
	
	
	
	/*
	 * Uncomment the annotation for BI-Directional relationship !
	 * 
	 * 'mappedBy' – means “I am NOT on the OWNING side”, I am mapped by Card from
	 * the other side of the relationship. It will also not create the database
	 * column which makes sense, I would expect a foreign key on the CARD
	 * table instead.
	 */
	/* */
	@OneToMany(
			cascade = CascadeType.ALL
			, mappedBy = "document"
			, fetch = FetchType.LAZY
			)
	/* */
	
	// I let spring handle the entity manager so I can stay with my jpa annotations and not mix with pure Hibernate annotations
	//@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<MajorVersion> majorVersions = new ArrayList<MajorVersion>();


	public Document() {
	}

	public Document(String name) {
		this.name = name;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DocPackage getDocPackage() {
		return docPackage;
	}

	public void setDocPackage(DocPackage pkg) {
		this.docPackage = pkg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Document : [").append(this.getId()).append("] name [")
				.append(this.getName()).append("]");
		return sb.toString();
	}


	public void setMajorVersions(List<MajorVersion> mvs) {
		this.majorVersions = mvs;
	}

	public List<MajorVersion> getMajorVersions() {
		return this.majorVersions;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/* */
}
