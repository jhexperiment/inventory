package com.jhexperiment.java.inventory.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jhexperiment.java.inventory.DuplicateStatusException;
import com.jhexperiment.java.inventory.StatusException;
import com.jhexperiment.java.inventory.model.Status;



public enum StatusDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void insertTmpData() {
    String[] aStatusList = {"ACTIVE", "DISPOSED", "LOST", "STOLEN"};
    for (String sStatus : aStatusList) {
      try {
        StatusDao.INSTANCE.add(sStatus);
      }
      catch (Exception e) {
        
      }  
    }
  }
  
  public void update(Status oStatus) throws StatusException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.statusExists(oStatus)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(oStatus);
          em.refresh(oStatus);
        }
        finally {
          em.close();
        }
        
      }
      else {
        throw new StatusException("Status doesn't exist.");
      }
    }
  }
  
  public void add(Status oStatus) throws DuplicateStatusException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! this.statusExists(oStatus)) {
        em.persist(oStatus);
        em.refresh(oStatus);
        em.close();
      }
      else {
        em.close();
        // throw duplicate absence error
        throw new DuplicateStatusException("Status not added.");
      }
    }
  }
  
  public void add(String sName) throws DuplicateStatusException {
    synchronized (this) {
      Status oStatus = new Status(sName);  
      this.add(oStatus);
    }
  }
  
  public boolean statusExists(Status oStatus) {
    if (oStatus.getId() != null) {
      return statusExists(oStatus.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT s "
          + "FROM Status s "
          + "WHERE s.name = :name";
    Query q = em.createQuery(gql);
    q.setParameter("name", oStatus.getName());
    
    List<Status> statusList = q.getResultList();
    boolean empty = statusList.isEmpty();
    
    return ! empty; 
  }
  
  public Status getStatus(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    Status oStatus = em.find(Status.class, id);
    em.close();
    return oStatus;
  }
  
  public boolean statusExists(Long id) {
    Status oStatus = null;
    EntityManager em = EMFService.get().createEntityManager();
    oStatus = em.find(Status.class, id);
    em.close();
    
    return oStatus != null; 
  }
  
  
  public List<Status> listStatus() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT s "
          + "FROM Status s ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<Status> listStatus(String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT s "
          		+ "FROM Status s ";
    Query q = em.createQuery(gql);
    
    List<Status> statusList = q.getResultList();
    
    /* Filtering */
    List<Status> filteredList = new ArrayList<Status>();
    if ( "".equals(sSearch) ) {
      filteredList = statusList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (Status status : statusList) {
        matches = status.getName().matches(regex);
      
        if (matches) {
          filteredList.add(status);
        }
      }
    }
    
    return filteredList;
  }
  
  
  
}
