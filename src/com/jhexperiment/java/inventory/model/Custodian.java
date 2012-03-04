package com.jhexperiment.java.inventory.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Custodian implements Comparable<Custodian> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	public Custodian(String name) {
		this.name = name;
	}
	
	public Custodian(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int compareTo(Custodian item) {
		int same = this.name.compareTo(item.getName());
		
		return same;
	}

	public boolean equals(Object obj) {
	    if (!(obj instanceof Custodian)) {
	      return false;
	    }
	    Custodian item = (Custodian) obj;
	    return this.name.equals(item.getName());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
