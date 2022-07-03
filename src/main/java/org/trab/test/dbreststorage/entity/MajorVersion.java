package org.trab.test.dbreststorage.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name = "MAJOR_VERSION")
public class MajorVersion {

	@SequenceGenerator(
	        name="MAJOR_VERSION_SEQUENCE_GENERATOR",
	        sequenceName="MAJOR_VERSION_SEQ",
	        initialValue=1,
	        allocationSize=1
	    )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAJOR_VERSION_SEQUENCE_GENERATOR")
	@Id 
	@Column(name = "ID")
	long id;

	

	@Column(name = "NAME")
	String name;

	@Column(name = "MVNUMBER")
	int majorVersionNumber;
	
	/*
	 * @ManyToOne annotation defines a single-valued association to another
	 * entity class that has many-to-one multiplicity. It is not normally
	 * necessary to specify the target entity explicitly since it can usually be
	 * inferred from the type of the object being referenced.
	 * 
	 * @JoinColumn indicate the OWNING side of the relationship, it is
	 * responsible for updating the database column. 
	 * 
	 * 
	 * NB : To properly map a unidirectional One-to-Many relationship, you only
	 * need to use the @ManyToOne annotation !!
	 */
	@ManyToOne
	@JoinColumn(name = "DOCUMENT_ID")
	private Document document;

	public MajorVersion() {
		this.latestVersion=false;
		this.oldestVersion=false;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	@OneToMany(
			cascade = CascadeType.ALL
			, mappedBy = "majorVersion"
			, fetch = FetchType.LAZY
			)
	/* */
	
	// I let spring handle the entity manager so I can stay with my jpa annotations and not mix with pure Hibernate annotations
	//@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<MinorVersion> minorVersions= new ArrayList<MinorVersion>();

	
	public List<MinorVersion> getMinorVersions() {
		return minorVersions;
	}

	public void setMinorVersions(List<MinorVersion> minorVersions) {
		this.minorVersions = minorVersions;
	}


	@OneToMany(
			cascade = CascadeType.ALL
			, mappedBy = "majorVersion"
			, fetch = FetchType.LAZY
			)
	/* */
	
	// I let spring handle the entity manager so I can stay with my jpa annotations and not mix with pure Hibernate annotations
	//@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<XmlContent> xmls = new ArrayList<XmlContent>();

	@Column(name = "MNLATEST")
	Boolean latestVersion;

	@Column(name = "MNOLDEST")
	Boolean oldestVersion;

	
	
	
	public Boolean getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(Boolean latestVersion) {
		this.latestVersion = latestVersion;
	}

	public Boolean getOldestVersion() {
		return oldestVersion;
	}

	public void setOldestVersion(Boolean oldestVersion) {
		this.oldestVersion = oldestVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document doc) {
		this.document = doc;
	}

	public int getMajorVersionNumber() {
		return majorVersionNumber;
	}

	public void setMajorVersionNumber(int energy) {
		this.majorVersionNumber = energy;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder
			.append(" name [").append(this.getName()).append("] ")

			.append("  [").append(this.getMajorVersionNumber()).append("] ")

			;

		return sBuilder.toString();
	}

	public List<XmlContent> getXmls() {
		return xmls;
	}

	public void setXmls(List<XmlContent> xmls) {
		this.xmls = xmls;
	}
}
