package com.pa.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.pa.util.EnumQualisClassification;

@Entity
public class Publication {
	
	@Id @GeneratedValue
	private Long id;
	
	private String title;
	
	@Transient
	private EnumQualisClassification qualis;
	
	private int year;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	private PublicationType publicationType;
	
	public Publication() {}
	
	public Publication(String title, int year, PublicationType typePublication) {
		this.title = title;
		this.year = year;
		this.publicationType = typePublication;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public EnumQualisClassification getQualis() {
		return qualis;
	}
	
	public void setQualis(EnumQualisClassification qualis) {
		this.qualis = qualis;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public PublicationType getPublicationType() {
		return publicationType;
	}
	
	public void setPublicationType(PublicationType publicationType) {
		this.publicationType = publicationType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
