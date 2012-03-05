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
import com.jhexperiment.java.inventory.model.GeneralInv;



public enum GeneralInvDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void insertTmpData() {
    Random randomGenerator = new Random();
    String[] descriptionList = new String[] {"Ka noho", "Kou lolouila.", "Jar'o'Peanut", "Pickle"};
    String[] locationList = new String[] {"G-01", "G-01", "G-01"};
    String[] custodianList = new String[] {"HAUGE, DISA", "MCGROIN, HOLDEN", "SKYWALKER, LUKE"};
    String[] statusList = new String[] {"ACTIVE", "DISPOSED", "STOLEN", "LOST"};
    String[] notesList = new String[] {"A'ole maopopo.", "Haha, you funny.", "Apple bottoms.", "Fancy pants."};
    for(int i=0; i < 10; i++) {
      GeneralInv inv = new GeneralInv(
        String.format("PX%05d", randomGenerator.nextInt(100000)), 
        new Date(2011, 2, 2), 
        new Date(2011, 2, 10),
        descriptionList[randomGenerator.nextInt(descriptionList.length)],
        String.format("257-%04d", randomGenerator.nextInt(10000)),
        String.format("257%06d", randomGenerator.nextInt(1000000)),
        locationList[randomGenerator.nextInt(locationList.length)],
        custodianList[randomGenerator.nextInt(custodianList.length)],
        randomGenerator.nextInt(100) + 1, 
        statusList[randomGenerator.nextInt(statusList.length)], 
        notesList[randomGenerator.nextInt(notesList.length)],
        new Date(2011, 1, 31),
        "kanaka@maili.k12.hi.us"
      );
      try {
        this.add(inv);
      } catch (DuplicateInvItemException e) {
        e.printStackTrace();
      }
    }
    
  }
  
  public void update(GeneralInv generalInv) throws InvItemException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.generalInvExists(generalInv)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(generalInv);
          em.refresh(generalInv);
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
  
  public void add(GeneralInv generalInv) throws DuplicateInvItemException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! generalInvExists(generalInv)) {
        em.persist(generalInv);
        em.refresh(generalInv);
        em.persist(generalInv);
        //em.refresh(generalInv);
        em.close();
      }
      else {
        em.close();
        // throw duplicate absence error
        throw new DuplicateInvItemException("General inventory item not added.");
      }
      
      try {
  		CustodianDao.INSTANCE.add(generalInv.getCustodian());
  	  } catch (DuplicateCustodianException e) {
  		
  	  }
  	  
  	  try {
		LocationDao.INSTANCE.add(generalInv.getLocation());
	  } catch (DuplicateLocationException e) {
		
	  }
	  
	  try {
		StatusDao.INSTANCE.add(generalInv.getStatus());
	  } catch (DuplicateStatusException e) {
		
	  }
    }
  }
  
  public void add(String poNumber, Date poDate, Date poRecieveDate, String description,
              String decalNumber,String propertyNumber, String location, 
              String custodian, int quantity, String status, String notes, 
              Date lastEditDate, String lastEditUser) throws DuplicateInvItemException {
    synchronized (this) {
      GeneralInv generalInv = new GeneralInv(poNumber, poDate, poRecieveDate, description,
                             decalNumber, propertyNumber, location, custodian, 
                             quantity, status, notes, lastEditDate, 
                             lastEditUser);  
      this.add(generalInv);
    }
  }
  
  public boolean generalInvExists(GeneralInv generalInv) {
    if (generalInv.getId() != null) {
      return generalInvExists(generalInv.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT g "
          + "FROM GeneralInv g "
          + "WHERE g.poNumber = :poNumber "
          +   "AND g.poDate = :poDate "
          +   "AND g.poRecieveDate = :poRecieveDate "
          +   "AND g.description = :description "
          +   "AND g.decalNumber = :decalNumber "
          +   "AND g.propertyNumber = :propertyNumber "
          +   "AND g.location = :location "
          +   "AND g.custodian = :custodian "
          +   "AND g.quantity = :quantity "
          +   "AND g.status = :status "
          +   "AND g.notes = :notes "
          +   "AND g.lastEditDate = :lastEditDate "
          +   "AND g.lastEditUser = :lastEditUser ";
    Query q = em.createQuery(gql);
    q.setParameter("poNumber", generalInv.getPoNumber());
    q.setParameter("poDate", generalInv.getPoDate());
    q.setParameter("poRecieveDate", generalInv.getPoRecieveDate());
    q.setParameter("description", generalInv.getDescription());
    q.setParameter("decalNumber", generalInv.getDecalNumber());
    q.setParameter("propertyNumber", generalInv.getPropertyNumber());
    q.setParameter("location", generalInv.getLocation());
    q.setParameter("custodian", generalInv.getCustodian());
    q.setParameter("quantity", generalInv.getQuantity());
    q.setParameter("status", generalInv.getStatus());
    q.setParameter("notes", generalInv.getNotes());
    q.setParameter("lastEditDate", generalInv.getLastEditDate());
    q.setParameter("lastEditUser", generalInv.getLastEditUser());
    
    List<GeneralInv> generalInvList = q.getResultList();
    boolean empty = generalInvList.isEmpty();
    
    return ! empty; 
  }
  
  public GeneralInv getGeneralInv(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    GeneralInv generalInv = em.find(GeneralInv.class, id);
    em.close();
    return generalInv;
  }
  
  public boolean generalInvExists(Long id) {
    GeneralInv generalInv = null;
    EntityManager em = EMFService.get().createEntityManager();
    generalInv = em.find(GeneralInv.class, id);
    em.close();
    
    return generalInv != null; 
  }
  
  
  public List<GeneralInv> listGeneralInv() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT g "
          + "FROM GeneralInv g "
          + "WHERE g.status <> 'DELETED' ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<GeneralInv> listGeneralInv(String[] aSortList, String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT g "
          + "FROM GeneralInv g ";
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
    List<GeneralInv> generalInvList = q.getResultList();
    
    /* Filtering */
    List<GeneralInv> filteredList = new ArrayList<GeneralInv>();
    for (GeneralInv inv : generalInvList) {
      if ( "DELETED".equals(inv.getStatus()) ) {
    	// exclude DELETED records
      }
      else if ( ! "".equals(sSearch) ) {
      	// compare search query
        String regex = "(?i).*" + sSearch + ".*";
        boolean matches;
        matches = inv.getDescription().matches(regex) ||
            inv.getCustodian().matches(regex) ||
            inv.getDecalNumber().matches(regex) ||
            inv.getLocation().matches(regex) ||
            inv.getNotes().matches(regex) ||
            inv.getPoNumber().matches(regex) ||
            inv.getPropertyNumber().matches(regex) ||
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
  
  public List<GeneralInv> listGeneralInv(Integer iDisplayStart, Integer iDisplayLength, String[] sColumns, 
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
    String gql = "SELECT g "
          + "FROM GeneralInv g "
          + sOrder + " "
          + sLimit;
    Query q = em.createQuery(gql);
    
    List<GeneralInv> generalInvList = q.getResultList();
    
    /* Filtering */
    List<GeneralInv> filteredList = new ArrayList<GeneralInv>();
    if ( "".equals(sSearch) ) {
      filteredList = generalInvList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (GeneralInv inv : generalInvList) {
        matches = inv.getDescription().matches(regex) ||
              inv.getCustodian().matches(regex) ||
              inv.getDecalNumber().matches(regex) ||
              inv.getLocation().matches(regex) ||
              inv.getNotes().matches(regex) ||
              inv.getPoNumber().matches(regex) ||
              inv.getPropertyNumber().matches(regex) ||
              inv.getStatus().matches(regex);
      
        if (matches) {
          filteredList.add(inv);
        }
      }
    }
    
    return filteredList;
  }
  
  
}
