package com.jhexperiment.java.inventory.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jhexperiment.java.inventory.DuplicateLocationException;
import com.jhexperiment.java.inventory.LocationException;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.Location;



public enum LocationDao {
  INSTANCE;
  
  public void clearDatabase() {
    List<Location> locationList = LocationDao.INSTANCE.listLocation();
    EntityManager em = EMFService.get().createEntityManager();
    for (Location location : locationList) {
      try {
    	Location loc = em.find(Location.class, location.getId());
    	em.remove(loc);
      }
      catch (Exception e) {
        
      }
    }
    em.close();
  }
  
  public void insertTmpData() {
    String[] aBuildingList = {"A", "B", "C"};
    DecimalFormat oFormatter = new DecimalFormat("00");
    for (String sBuilding : aBuildingList) {
      for (int i = 1; i <= 6; i++) {
        try {
          LocationDao.INSTANCE.add(sBuilding + "-" + oFormatter.format(i));
        }
        catch (Exception e) {
          
        }  
      }
    }
  }
  
  public void findUniqueLocationFromInventory() {
    List<GeneralInv> generalInvList = GeneralInvDao.INSTANCE.listGeneralInv();
    for (GeneralInv oInv : generalInvList) {
      try {
        LocationDao.INSTANCE.add(oInv.getLocation());
      }
      catch (Exception e) {
        String tmp = "";
        tmp = tmp + "";
      }
    }
  }
  
  public void update(Location oLocation) throws LocationException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.locationExists(oLocation)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(oLocation);
          em.refresh(oLocation);
        }
        finally {
          em.close();
        }
        
      }
      else {
        throw new LocationException("Location doesn't exist.");
      }
    }
  }
  
  public void add(Location oLocation) throws DuplicateLocationException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! this.locationExists(oLocation)) {
        em.persist(oLocation);
        em.refresh(oLocation);
        em.close();
      }
      else {
        em.close();
        throw new DuplicateLocationException("Location not added.");
      }
    }
  }
  
  public void add(String sName) throws DuplicateLocationException {
    synchronized (this) {
      Location oLocation = new Location(sName);  
      this.add(oLocation);
    }
  }
  
  public boolean locationExists(Location oLocation) {
    if (oLocation.getId() != null) {
      return locationExists(oLocation.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT l "
               + "FROM Location l "
               + "WHERE l.name = :name";
    Query q = em.createQuery(gql);
    q.setParameter("name", oLocation.getName());
    
    List<Location> locationList = q.getResultList();
    boolean empty = locationList.isEmpty();
    
    return ! empty; 
  }
  
  public Location getLocation(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    Location oLocatoin = em.find(Location.class, id);
    em.close();
    return oLocatoin;
  }
  
  public boolean locationExists(Long id) {
    Location oLocation = null;
    EntityManager em = EMFService.get().createEntityManager();
    oLocation = em.find(Location.class, id);
    em.close();
    
    return oLocation != null; 
  }
  
  
  public List<Location> listLocation() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT l "
          + "FROM Location l ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  
  public List<Location> listLocation(String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT l "
          + "FROM Location l ";
    Query q = em.createQuery(gql);
    
    List<Location> locationList = q.getResultList();
    
    /* Filtering */
    List<Location> filteredList = new ArrayList<Location>();
    if ( "".equals(sSearch) ) {
      filteredList = locationList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (Location loc : locationList) {
        matches = loc.getName().matches(regex);
      
        if (matches) {
          filteredList.add(loc);
        }
      }
    }
    
    return filteredList;
  }
  
  
}
