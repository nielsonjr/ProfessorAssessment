package com.pa.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Curriculo {
	@Id
	private Long id;
	
	private String name;
	private int concludedOrientations;
	private int onGoingOrientations;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Publication> publications;
	
	private Date lastUpdate;
	
	public Curriculo() {}
	
	public Curriculo(String name, Date lastUpdate) {
		this.name = name;
		this.lastUpdate = lastUpdate;
		
		this.publications = new HashSet<Publication>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getConcludedOrientations() {
		return concludedOrientations;
	}
	
	public void setConcludedOrientations(int orientations) {
		this.concludedOrientations = orientations;
	}
	
	public int getOnGoingOrientations() {
		return onGoingOrientations;
	}

	public void setOnGoingOrientations(int onGoingOrientations) {
		this.onGoingOrientations = onGoingOrientations;
	}

	public Set<Publication> getPublications() {
		return publications;
	}
	
	public void setPublications(Set<Publication> publications) {
		this.publications = publications;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
