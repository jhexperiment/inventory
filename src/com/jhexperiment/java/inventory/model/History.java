package com.jhexperiment.java.inventory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class History implements Comparable<History> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String type;
	private String inventory;
	private String user;
	private Date date;
	private String action;
	
	public History(String user) {
		this.user = user;
	}
	
	public History(String user, Date date, String action) {
		this.user = user;
		this.date = date;
		this.action = action;
	}
	
	public History(String type, String inventory, String user, Date date, String action) {
		this.type = type;
		this.inventory = inventory;
		this.user = user;
		this.date = date;
		this.action = action;
	}
	
	public History(Long id, String type, String inventory, String user, Date date, String action) {
		this.id = id;
		this.type = type;
		this.inventory = inventory;
		this.user = user;
		this.date = date;
		this.action = action;
	}
	
	public int compareTo(History item) {
		int same = this.date.compareTo(item.getDate());
		
		return same;
	}

	public boolean equals(Object obj) {
	    if (!(obj instanceof History)) {
	      return false;
	    }
	    History item = (History) obj;
	    return this.date.equals(item.getDate());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getInventory() {
		return this.inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	
	public String getUser() {
		return this.user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public Date getDate() {
		return this.date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getAction() {
		return this.action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
