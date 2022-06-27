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
@Table(name = "MINOR_VERSION")
public class MinorVersion {

	@SequenceGenerator(
	        name="MINOR_VERSION_SEQUENCE_GENERATOR",
	        sequenceName="MINOR_VERSION_SEQ",
	        initialValue=1,
	        allocationSize=1
	    )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MINOR_VERSION_SEQUENCE_GENERATOR")
	@Id 
	@Column(name = "ID")
	long id;

	@Column(name = "NAME")
	String name;

	@Column(name = "MNNUMBER")
	int minorVersionNumber;
	

	public MinorVersion() {

	}

	
	@ManyToOne
	@JoinColumn(name = "MAJOR_VERSION_ID")
	MajorVersion majorVersion;
	

	public MajorVersion getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(MajorVersion majorVersion) {
		this.majorVersion = majorVersion;
	}

	public long getId() {
		return id;
	}

	public void setId(long idmiv) {
		this.id = idmiv;
	}

	
	@OneToMany(
			cascade = CascadeType.ALL
			, mappedBy = "minorVersion"
			, fetch = FetchType.LAZY
			)
	/* */
	
	// I let spring handle the entity manager so I can stay with my jpa annotations and not mix with pure Hibernate annotations
	//@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
	private List<XmlContent> xmls = new ArrayList<XmlContent>();

	
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}


	public int getMinorVersionNumber() {
		return minorVersionNumber;
	}

	public void setMinorVersionNumber(int mvn) {
		this.minorVersionNumber = mvn;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder
			.append(" name [").append(this.getName()).append("] ")

			.append("  [").append(this.getMinorVersionNumber()).append("] ")

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
