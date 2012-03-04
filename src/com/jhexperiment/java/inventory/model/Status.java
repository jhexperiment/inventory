package com.jhexperiment.java.inventory.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Status implements Comparable<Status> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	public Status(String name) {
		this.name = name;
	}
	
	public Status(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int compareTo(Status item) {
		int same = this.name.compareTo(item.getName());
		
		return same;
	}

	public boolean equals(Object obj) {
	    if (!(obj instanceof Status)) {
	      return false;
	    }
	    Status item = (Status) obj;
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
