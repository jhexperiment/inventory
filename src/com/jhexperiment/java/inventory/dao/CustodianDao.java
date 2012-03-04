package com.jhexperiment.java.inventory.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jhexperiment.java.inventory.CustodianException;
import com.jhexperiment.java.inventory.DuplicateCustodianException;
import com.jhexperiment.java.inventory.model.Custodian;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.Status;



public enum CustodianDao {
  INSTANCE;
  
  public void clearDatabase() {
    
  }
  
  public void insertTmpData() {
	List<GeneralInv> generalInvList = GeneralInvDao.INSTANCE.listGeneralInv();
	for (GeneralInv inv : generalInvList) {
		try {
			this.add(inv.getCustodian());
		}
		catch (Exception e) {
			
		}
	}
  }
  
  public void update(Custodian oCustodian) throws CustodianException {
    synchronized (this) {
      /* TODO: test which is better, this or this.generalInvExists(generalInv.getId()) */
      if (this.custodianExists(oCustodian)) {
        EntityManager em = EMFService.get().createEntityManager();
        try {
          em.persist(oCustodian);
          em.refresh(oCustodian);
        }
        finally {
          em.close();
        }
        
      }
      else {
        throw new CustodianException("Custodian doesn't exist.");
      }
    }
  }
  
  public void add(Custodian oCustodian) throws DuplicateCustodianException {
    synchronized (this) {
      EntityManager em = EMFService.get().createEntityManager();
      if (! this.custodianExists(oCustodian)) {
        em.persist(oCustodian);
        em.refresh(oCustodian);
        em.close();
      }
      else {
        em.close();
        // throw duplicate absence error
        throw new DuplicateCustodianException("Custodian not added.");
      }
    }
  }
  
  public void add(String sName) throws DuplicateCustodianException {
    synchronized (this) {
      Custodian oCustodian = new Custodian(sName);  
      this.add(oCustodian);
    }
  }
  
  public boolean custodianExists(Custodian oCustodian) {
    if (oCustodian.getId() != null) {
      return custodianExists(oCustodian.getId());
    }
    
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT c "
          + "FROM Custodian c "
          + "WHERE c.name = :name";
    Query q = em.createQuery(gql);
    q.setParameter("name", oCustodian.getName());
    
    List<Custodian> custodianList = q.getResultList();
    boolean empty = custodianList.isEmpty();
    
    return ! empty; 
  }
  
  public Custodian getCustodian(Long id) {
    EntityManager em = EMFService.get().createEntityManager();
    Custodian oCustodian = em.find(Custodian.class, id);
    em.close();
    return oCustodian;
  }
  
  public boolean custodianExists(Long id) {
    Custodian oCustodian = null;
    EntityManager em = EMFService.get().createEntityManager();
    oCustodian = em.find(Custodian.class, id);
    em.close();
    
    return oCustodian != null; 
  }
  
  
  public List<Custodian> listCustodian() {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT c "
          + "FROM Custodian c ";
    Query q = em.createQuery(gql);
    
    return q.getResultList();
  }
  
  public List<Custodian> listCustodian(String sSearch) {
    EntityManager em = EMFService.get().createEntityManager();
    String gql = "SELECT c "
              + "FROM Custodian c ";
    Query q = em.createQuery(gql);
    
    List<Custodian> custodianList = q.getResultList();
    
    /* Filtering */
    List<Custodian> filteredList = new ArrayList<Custodian>();
    if ( "".equals(sSearch) ) {
      filteredList = custodianList;
    }
    else {
      String regex = "(?i).*" + sSearch + ".*";
      boolean matches;
      for (Custodian oCustodian : custodianList) {
        matches = oCustodian.getName().matches(regex);
      
        if (matches) {
          filteredList.add(oCustodian);
        }
      }
    }
    
    return filteredList;
  }
  
  
}
