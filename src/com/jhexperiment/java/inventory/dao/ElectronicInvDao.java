package com.jhexperiment.java.inventory.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jhexperiment.java.inventory.DuplicateCustodianException;
import com.jhexperiment.java.inventory.DuplicateInvItemException;
import com.jhexperiment.java.inventory.DuplicateLocationException;
import com.jhexperiment.java.inventory.DuplicateStatusException;
import com.jhexperiment.java.inventory.InvItemException;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.History;



public enum ElectronicInvDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void update(ElectronicInv electronicInv, String colName) throws InvItemException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.electronicInvExists(electronicInv)) {
    	ElectronicInv oOrigInv = this.getElectronicInv(electronicInv.getId());
      	
    	EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(electronicInv);
          em.refresh(electronicInv);
        }
        finally {
          em.close();
        }
        
        String sAction = "";
        String sType = "";
        if ("DELETED".equals(electronicInv.getStatus())) {
        	sType = "DELETE";
        	sAction = 
	    		"DELETED '" + electronicInv.getDescription() +  
	        	"' FROM 'electronics'" +
	        	" BY " + electronicInv.getLastEditUser();
        	
        }
        else {
        	sType = "UPDATE";
        	sAction = 
        		"UPDATED 'electronics' " + 
            	" SET '" + colName + 
            	"' FROM '" + oOrigInv.getData(colName) + 
            	"' TO '" + electronicInv.getData(colName) +
            	"' BY " + electronicInv.getLastEditUser();
        }
        
        try {
	        History oHistory = new History(sType, "electronic", electronicInv.getLastEditUser(), new Date(), sAction);
	    	HistoryDao.INSTANCE.add(oHistory);
        }
        catch (Exception e) {
        	
        }
        
      }
      else {
        throw new InvItemException("Inventory item doesn't exist.");
      }
    }
  }
  
  public void add(ElectronicInv electronicInv) throws DuplicateInvItemException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! this.electronicInvExists(electronicInv)) {
        em.persist(electronicInv);
        em.refresh(electronicInv);
        em.persist(electronicInv);
        //em.refresh(generalInv);
        em.close();
      }
      else {
        em.close();
        // throw duplicate absence error
        throw new DuplicateInvItemException("Electronic inventory item not added.");
      }
      
      
      try {
	        String sAction = 
	    		"ADD '" + electronicInv.getDescription() +  
	        	"' TO 'electronics' " +
	        	"' BY " + electronicInv.getLastEditUser();
	    	History oHistory = new History("ADD", "electornics", electronicInv.getLastEditUser(), new Date(), sAction);
	    	HistoryDao.INSTANCE.add(oHistory);
      }
      catch (Exception e) {
      	
      }
      
      try {
  		CustodianDao.INSTANCE.add(electronicInv.getCustodian());
  	  } catch (DuplicateCustodianException e) {
  		
  	  }
  	  
  	  try {
		LocationDao.INSTANCE.add(electronicInv.getLocation());
	  } catch (DuplicateLocationException e) {
		
	  }
	  
	  try {
		StatusDao.INSTANCE.add(electronicInv.getStatus());
	  } catch (DuplicateStatusException e) {
		
	  }
    }
  }
  
  public void add(String poNumber, Date poDate, Date poRecieveDate, String description,
		  String type, String make, String model, String serialNumber, String decalNumber, 
          String propertyNumber, String location, String custodian, String funder,
          String status, String notes, Date lastEditDate, String lastEditUser) throws DuplicateInvItemException {
    synchronized (this) {
    	ElectronicInv electronicInv = 
    		new ElectronicInv(poNumber, poDate, poRecieveDate, description, type, make, model,
    						 serialNumber, decalNumber, propertyNumber, location, custodian, 
    						 funder, status, notes, lastEditDate, 
                             lastEditUser);  
      this.add(electronicInv);
    }
  }
  
  public boolean electronicInvExists(ElectronicInv electronicInv) {
    if (electronicInv.getId() != null) {
      return this.electronicInvExists(electronicInv.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT e "
          + "FROM ElectronicInv e "
          + "WHERE "
          +   "e.description = :description "
          +   "AND e.poNumber = :poNumber "
          +   "AND e.poDate = :poDate "
          +   "AND e.poRecieveDate = :poRecieveDate "
          +   "AND e.type = :type "
          +   "AND e.make = :make "
          +   "AND e.model = :model "
          +   "AND e.serialNumber = :serialNumber "
          +   "AND e.decalNumber = :decalNumber "
          +   "AND e.propertyNumber = :propertyNumber "
          +   "AND e.location = :location "
          +   "AND e.custodian = :custodian "
          +   "AND e.funder = :funder "
          +   "AND e.status = :status "
          +   "AND e.notes = :notes "
          +   "AND e.lastEditDate = :lastEditDate "
          +   "AND e.lastEditUser = :lastEditUser "
          ;
    Query q = em.createQuery(gql);
    boolean empty = true;
    try {
    	q.setParameter("poNumber", electronicInv.getPoNumber());
	    q.setParameter("description", electronicInv.getDescription());
	    q.setParameter("poDate", electronicInv.getPoDate());
	    q.setParameter("poRecieveDate", electronicInv.getPoRecieveDate());
	    q.setParameter("type", electronicInv.getType());
	    q.setParameter("make", electronicInv.getMake());
	    q.setParameter("model", electronicInv.getModel());
	    q.setParameter("serialNumber", electronicInv.getSerialNumber());
	    q.setParameter("decalNumber", electronicInv.getDecalNumber());
	    q.setParameter("propertyNumber", electronicInv.getPropertyNumber());
	    q.setParameter("location", electronicInv.getLocation());
	    q.setParameter("custodian", electronicInv.getCustodian());
	    q.setParameter("funder", electronicInv.getFunder());
	    q.setParameter("status", electronicInv.getStatus());
	    q.setParameter("notes", electronicInv.getNotes());
	    q.setParameter("lastEditDate", electronicInv.getLastEditDate());
	    q.setParameter("lastEditUser", electronicInv.getLastEditUser());
	    
    	List<ElectronicInv> electronicInvList = q.getResultList();
    	empty = electronicInvList.isEmpty();
    }
    catch (Exception e) {
    	String tmp  = e.getMessage();
    }
    
    
    return ! empty; 
  }
  
  public ElectronicInv getElectronicInv(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    ElectronicInv electronicInv = em.find(ElectronicInv.class, id);
    em.close();
    return electronicInv;
  }
  
  public boolean electronicInvExists(Long id) {
	ElectronicInv electronicInv = null;
    EntityManager em = EMFService.get().createEntityManager();
    electronicInv = em.find(ElectronicInv.class, id);
    em.close();
    
    return electronicInv != null; 
  }
  
  public List<ElectronicInv> listElectronicInv() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT e "
          + "FROM ElectronicInv e "
          + "WHERE e.status <> 'DELETED' ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<ElectronicInv> listElectronicInv(String[] aSortList, String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT e "
          + "FROM ElectronicInv e ";
          //+ "WHERE g.status <> 'DELETED' ";
    
    if (aSortList != null) {
      gql += "ORDER BY ";
      for (int i = 0; i < aSortList.length; i++) {
        gql += aSortList[i];
        if (i < (aSortList.length - 1)) {
          gql += ", ";
        }
      }
    }
    
    Query q = em.createQuery(gql);
    List<ElectronicInv> electronicInvList = q.getResultList();
    
    /* Filtering */
    List<ElectronicInv> filteredList = new ArrayList<ElectronicInv>();
    for (ElectronicInv inv : electronicInvList) {
      if ( "DELETED".equals(inv.getStatus()) ) {
    	// exclude DELETED records
      }
      else if ( ! "".equals(sSearch) ) {
      	// compare search query
        String regex = "(?i).*" + sSearch + ".*";
        boolean matches;
        matches = inv.getPoNumber().matches(regex) ||
        	inv.getDescription().matches(regex) ||
        	inv.getType().matches(regex) ||
        	inv.getMake().matches(regex) ||
        	inv.getModel().matches(regex) ||
        	inv.getSerialNumber().matches(regex) ||
        	inv.getDecalNumber().matches(regex) ||
            inv.getPropertyNumber().matches(regex) ||
        	inv.getLocation().matches(regex) ||
            inv.getCustodian().matches(regex) ||
            inv.getFunder().matches(regex) ||
            inv.getNotes().matches(regex) ||
            inv.getStatus().matches(regex);
    
        if (matches) {
          filteredList.add(inv);
        }
      }
      else {
    	// otherwise include record
   	    filteredList.add(inv);
      }
    }
    
    return filteredList;
  }
  
  public List<ElectronicInv> listElectronicInv(Integer iDisplayStart, Integer iDisplayLength, String[] sColumns, 
                       Integer iSortCol_0, Integer iSortingCols, 
                       HashMap<String, Object> sortCols, String sSearch, 
                       HashMap<String, Object> bSearchableList, 
                       HashMap<String, Object> sSearchList) {
    /* Paging */
    String sLimit = "";
    if ( iDisplayStart != null && iDisplayLength != -1 ) {
      sLimit = "LIMIT " + iDisplayStart + ", " + iDisplayLength;
    }
    
    /* Ording */
    String sOrder = "";
    if ( iSortCol_0 != null ) {
      sOrder = "ORDER BY ";
      Iterator i = sortCols.entrySet().iterator();
      while (i.hasNext()) {
        Map.Entry e = (Map.Entry) i.next();
        sOrder += e.getKey() + " " + e.getValue();
        if (i.hasNext()) {
          sOrder += ", ";
        }
      }
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT e "
          + "FROM ElectronicInv e "
          + sOrder + " "
          + sLimit;
    Query q = em.createQuery(gql);
    
    List<ElectronicInv> electronicInvList = q.getResultList();
    
    /* Filtering */
    List<ElectronicInv> filteredList = new ArrayList<ElectronicInv>();
    if ( "".equals(sSearch) ) {
      filteredList = electronicInvList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (ElectronicInv inv : electronicInvList) {
        matches = inv.getPoNumber().matches(regex) ||
	    	inv.getDescription().matches(regex) ||
	    	inv.getType().matches(regex) ||
	    	inv.getMake().matches(regex) ||
	    	inv.getModel().matches(regex) ||
	    	inv.getSerialNumber().matches(regex) ||
	    	inv.getDecalNumber().matches(regex) ||
	        inv.getPropertyNumber().matches(regex) ||
	    	inv.getLocation().matches(regex) ||
	        inv.getCustodian().matches(regex) ||
	        inv.getFunder().matches(regex) ||
	        inv.getNotes().matches(regex) ||
	        inv.getStatus().matches(regex);
      
        if (matches) {
          filteredList.add(inv);
        }
      }
    }
    
    return filteredList;
  }
  
  
}
