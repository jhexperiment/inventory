package com.jhexperiment.java.inventory.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jhexperiment.java.inventory.DuplicateHistoryException;
import com.jhexperiment.java.inventory.HistoryException;
import com.jhexperiment.java.inventory.StatusException;
import com.jhexperiment.java.inventory.model.History;
import com.jhexperiment.java.inventory.model.Status;



public enum HistoryDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void insertTmpData() {
	/*
    String[] aStatusList = {"ACTIVE", "DISPOSED", "LOST", "STOLEN"};
    for (String sStatus : aStatusList) {
      try {
        HistoryDao.INSTANCE.add(sStatus);
      }
      catch (Exception e) {
        
      }  
    }
    */
  }
  
  public void update(History oHistory) throws HistoryException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.historyExists(oHistory)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(oHistory);
          em.refresh(oHistory);
        }
        finally {
          em.close();
        }
        
      }
      else {
        throw new HistoryException("History doesn't exist.");
      }
    }
  }
  
  public void add(History oHistory) throws DuplicateHistoryException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! this.historyExists(oHistory)) {
        em.persist(oHistory);
        em.refresh(oHistory);
        em.close();
      }
      else {
        em.close();
        // throw duplicate absence error
        throw new DuplicateHistoryException("History not added.");
      }
    }
  }
  
  public void add(Long id, String type, String inventory, String user, Date date, String action) throws DuplicateHistoryException {
    synchronized (this) {
	  History oHistory = new History(id, type, inventory, user, date, action);  
      this.add(oHistory);
    }
  }
  
  public boolean historyExists(History oHistory) {
    if (oHistory.getId() != null) {
      return historyExists(oHistory.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT h "
          + "FROM History h "
          + "WHERE h.date = :date";
    Query q = em.createQuery(gql);
    q.setParameter("date", oHistory.getDate());
    
    List<History> historyList = q.getResultList();
    boolean empty = historyList.isEmpty();
    
    return ! empty; 
  }
  
  public History getHistory(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    History oHistory = em.find(History.class, id);
    em.close();
    return oHistory;
  }
  
  public boolean historyExists(Long id) {
	History oHistory = null;
    EntityManager em = EMFService.get().createEntityManager();
    oHistory = em.find(History.class, id);
    em.close();
    
    return oHistory != null; 
  }
  
  
  public List<History> listHistory() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT h "
          + "FROM History h ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<History> listStatus(String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT h "
          		+ "FROM History h ";
    Query q = em.createQuery(gql);
    
    List<History> statusList = q.getResultList();
    
    /* Filtering */
    List<History> filteredList = new ArrayList<History>();
    if ( "".equals(sSearch) ) {
      filteredList = statusList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (History oHistory : statusList) {
        matches = oHistory.getType().matches(regex) 
        		|| oHistory.getUser().matches(regex)
        		|| oHistory.getAction().matches(regex);
      
        if (matches) {
          filteredList.add(oHistory);
        }
      }
    }
    
    return filteredList;
  }
  
  
  
}
