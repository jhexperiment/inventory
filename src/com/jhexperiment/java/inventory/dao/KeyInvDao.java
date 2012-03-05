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
import com.jhexperiment.java.inventory.model.KeyInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;



public enum KeyInvDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void insertTempData() {
	  KeyInv keyInv = new KeyInv("A-01", "temp", "K001001", 1, "TEMPY, TEMP", new Date(), new Date(),
			  "ACTIVE", "notey notes", new Date(), "jhauge@maili.k12.hi.us");
	  try {
		  EntityManager em = EMFService.get().createEntityManager();
          em.persist(keyInv);
        em.refresh(keyInv);
        em.persist(keyInv);
        //em.refresh(generalInv);
        em.close();
		}
		catch (Exception e) {
			
		}
	  
  }
  
  public void update(KeyInv keyInv) throws InvItemException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.keyInvExists(keyInv)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(keyInv);
          em.refresh(keyInv);
        }
        finally {
          em.close();
        }
        
      }
      else {
        throw new InvItemException("Inventory item doesn't exist.");
      }
    }
  }
  
  public void add(KeyInv keyInv) throws DuplicateInvItemException {
    synchronized (this) {
      if (! this.keyInvExists(keyInv)) {
    	EntityManager em = EMFService.get().createEntityManager();
        em.persist(keyInv);
        em.refresh(keyInv);
        em.persist(keyInv);
        //em.refresh(generalInv);
        em.close();
      }
      else {
        // throw duplicate absence error
        throw new DuplicateInvItemException("Electronic inventory item not added.");
      }
      
      try {
		CustodianDao.INSTANCE.add(keyInv.getCustodian());
	  } catch (DuplicateCustodianException e) {
		
	  }
	  
	  try {
		LocationDao.INSTANCE.add(keyInv.getLocation());
	  } catch (DuplicateLocationException e) {
		
	  }
	  
	  try {
		StatusDao.INSTANCE.add(keyInv.getStatus());
	  } catch (DuplicateStatusException e) {
		
	  }
    }
  }
  
  public void add(String location, String description, String keyId, int inStock, String custodian, 
          Date issuedDate, Date returnedDate, String status, String notes, Date lastEditDate, 
          String lastEditUser) throws DuplicateInvItemException {
    synchronized (this) {
    	KeyInv KeyInv = 
    		new KeyInv(location, description, keyId, inStock, custodian, issuedDate, 
    					returnedDate, status, notes, lastEditDate, lastEditUser);  
      this.add(KeyInv);
    }
  }
  
  public boolean keyInvExists(KeyInv keyInv) {
    if (keyInv.getId() != null) {
      return this.keyInvExists(keyInv.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT k "
          + "FROM KeyInv k "
          + "WHERE k.location = :location "
          +   "AND k.description = :description "
          +   "AND k.keyId = :keyId "
          +   "AND k.inStock = :inStock "
          +   "AND k.custodian = :custodian "
          +   "AND k.issuedDate = :issueDate "
          +   "AND k.returnedDate = :returnDate "
          +   "AND k.status = :status "
          +   "AND k.notes = :notes "
          +   "AND k.lastEditDate = :lastEditDate "
          +   "AND k.lastEditUser = :lastEditUser ";
    Query q = em.createQuery(gql);
    q.setParameter("location", keyInv.getLocation());
    q.setParameter("description", keyInv.getDescription());
    q.setParameter("keyId", keyInv.getKeyId());
    q.setParameter("custodian", keyInv.getCustodian());
    q.setParameter("issuedDate", keyInv.getIssuedDate());
    q.setParameter("returnedDate", keyInv.getReturnDate());
    q.setParameter("status", keyInv.getStatus());
    q.setParameter("notes", keyInv.getNotes());
    q.setParameter("lastEditDate", keyInv.getLastEditDate());
    q.setParameter("lastEditUser", keyInv.getLastEditUser());
    
    List<KeyInv> keyInvList = q.getResultList();
    boolean empty = keyInvList.isEmpty();
    
    em.close();
    return ! empty; 
  }
  
  public KeyInv getKeyInv(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    KeyInv KeyInv = em.find(KeyInv.class, id);
    em.close();
    return KeyInv;
  }
  
  public boolean keyInvExists(Long id) {
	KeyInv KeyInv = null;
    EntityManager em = EMFService.get().createEntityManager();
    KeyInv = em.find(KeyInv.class, id);
    em.close();
    
    return KeyInv != null; 
  }
  
  public List<KeyInv> listKeyInv() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT e "
          + "FROM KeyInv e "
          + "WHERE e.status <> 'DELETED' ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<KeyInv> listKeyInv(String[] aSortList, String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT k "
          + "FROM KeyInv k ";
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
    List<KeyInv> KeyInvList = q.getResultList();
    
    /* Filtering */
    List<KeyInv> filteredList = new ArrayList<KeyInv>();
    for (KeyInv inv : KeyInvList) {
      if ( "DELETED".equals(inv.getStatus()) ) {
    	// exclude DELETED records
      }
      else if ( ! "".equals(sSearch) ) {
      	// compare search query
        String regex = "(?i).*" + sSearch + ".*";
        boolean matches;
        matches = inv.getLocation().matches(regex) ||
        	inv.getDescription().matches(regex) ||
        	inv.getLocation().matches(regex) ||
            inv.getKeyId().matches(regex) ||
            inv.getCustodian().matches(regex) ||
            inv.getStatus().matches(regex) ||
            inv.getNotes().matches(regex);
    
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
  
  public List<KeyInv> listKeyInv(Integer iDisplayStart, Integer iDisplayLength, String[] sColumns, 
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
          + "FROM KeyInv e "
          + sOrder + " "
          + sLimit;
    Query q = em.createQuery(gql);
    
    List<KeyInv> KeyInvList = q.getResultList();
    
    /* Filtering */
    List<KeyInv> filteredList = new ArrayList<KeyInv>();
    if ( "".equals(sSearch) ) {
      filteredList = KeyInvList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (KeyInv inv : KeyInvList) {
        matches = inv.getLocation().matches(regex) ||
        inv.getDescription().matches(regex) ||
        inv.getLocation().matches(regex) ||
        inv.getKeyId().matches(regex) ||
        inv.getCustodian().matches(regex) ||
        inv.getStatus().matches(regex) ||
        inv.getNotes().matches(regex);
      
        if (matches) {
          filteredList.add(inv);
        }
      }
    }
    
    return filteredList;
  }
  
  
}
