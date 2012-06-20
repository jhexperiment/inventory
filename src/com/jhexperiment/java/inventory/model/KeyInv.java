package com.jhexperiment.java.inventory.model;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jhexperiment.java.inventory.InvalidKeyItemException;
import com.jhexperiment.java.inventory.JhDate;

@Entity
public class KeyInv implements Comparable<KeyInv> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String location;
  private String description;
  private String keyId;
  private int  inStock;
  private String custodian;
  private Date issuedDate;
  private Date returnedDate;
  private String status;
  private String notes;
  private Date lastEditDate;
  private String lastEditUser;
   
  
  public KeyInv(String location, String description, String keyId, int inStock, String custodian, 
          Date issuedDate, Date returnedDate, String status, String notes, Date lastEditDate, 
          String lastEditUser) {
    this.location = location;
    this.description = description;
    this.keyId = keyId;
    this.inStock = inStock;
    this.custodian = custodian;
    this.issuedDate = issuedDate;
    this.returnedDate = returnedDate;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
  }
  
  public KeyInv(Long id, String location, String description, String keyId, int inStock, 
          String custodian, Date issuedDate, Date returnedDate, String status, String notes, 
          Date lastEditDate, String lastEditUser) {
    this.id = id;
    this.location = location;
    this.description = description;
    this.keyId = keyId;
    this.inStock = inStock;
    this.custodian = custodian;
    this.issuedDate = issuedDate;
    this.returnedDate = returnedDate;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
  }
  
  public KeyInv(String[] aData) throws InvalidKeyItemException {
      int iIndex = 1;
      int iFieldCount = 12;
      try {
        this.id = Long.parseLong(aData[iIndex++]);
      }
      catch (Exception e) {
        
      }
      
      /*
      for (int i = 2; i <= iFieldCount; i++) {
        if ("".equals(aData[i])) {
          throw new InvalidKeyItemException();
        }
      }
      */
      
      this.location = aData[iIndex++];
      this.description = aData[iIndex++];
      this.keyId = aData[iIndex++];
      
      try {
        this.inStock = Integer.parseInt(aData[iIndex++]);
      }
      catch (Exception e) {
          
      }
 
      this.custodian = aData[iIndex++];
      this.issuedDate = (Date) JhDate.parse(aData[iIndex++]);
      this.returnedDate = (Date) JhDate.parse(aData[iIndex++]);
      this.status = aData[iIndex++];
      this.notes = aData[iIndex++];
      this.lastEditDate = (Date) JhDate.parse(aData[iIndex++]);
      this.lastEditUser = aData[iIndex++];
    
    }
  
  public void set(String key, Object value) {
      /* TODO: re-order based on most requested after profiling. */
      /* TODO: validation and exception raising. */
      
      if ("location".equals(key)) {
        this.location = (String) value;
      }
      else if ("description".equals(key)) {
        this.description = (String) value;
      }
      else if ("key-id".equals(key)) {
        this.keyId = (String) value;
      }
      else if ("in-stock".equals(key)) {
        this.inStock = (Integer) value;
      }
      else if ("custodian".equals(key)) {
        this.custodian = (String) value;
      }
      else if ("issued-date".equals(key)) {
        this.issuedDate = new Date((String) value);
      }
      else if ("return-date".equals(key)) {
        this.returnedDate = new Date((String) value);
      }
      else if ("status".equals(key)) {
        this.status = (String) value;
      }
      else if ("notes".equals(key)) {
        this.notes = (String) value;
      }
      else if ("last-edit-date".equals(key)) {
        this.lastEditDate = new Date((String) value);
      }
      else if ("last-edit-user".equals(key)) {
        this.lastEditUser = (String) value;
      }
    }
    
  public Object getData(String key) {
    
    if ("location".equals(key)) {
      return this.location;
    }
    else if ("description".equals(key)) {
      return this.description;
    }
    else if ("key-id".equals(key)) {
      return this.keyId;
    }
    else if ("in-stock".equals(key)) {
      return this.inStock;
    }
    else if ("custodian".equals(key)) {
      return this.custodian;
    }
    else if ("status".equals(key)) {
      return this.status;
    }
    else if ("issued-date".equals(key)) {
      return this.issuedDate;
    }
    else if ("return-date".equals(key)) {
      return this.returnedDate;
    }
    else if ("notes".equals(key)) {
       return this.notes;
    }
    else if ("last-edit-date".equals(key)) {
      return this.lastEditDate;
    }
    else if ("last-edit-user".equals(key)) {
      return this.lastEditUser;
    }

    return null;
  }
  
  public int compareTo(KeyInv invItem) {
    int same = this.description.compareTo(invItem.getDescription());
    
    return same;
  }

  public boolean equals(Object obj) {
      if (!(obj instanceof KeyInv)) {
        return false;
      }
      KeyInv invItem = (KeyInv) obj;
      return this.description.equals(invItem.getDescription());
  }
  
  public HashMap<String, Object> toJsonArray() {
    HashMap<String, Object> aReturnData = new HashMap<String, Object>();
    aReturnData.put("iId", this.id);
    aReturnData.put("sDescription", this.description);
    aReturnData.put("sKeyId", this.keyId);
    aReturnData.put("iInStock", this.inStock);
    aReturnData.put("sIssuedDate", this.issuedDate.toString());
    aReturnData.put("sReturnDate", this.returnedDate.toString());
    aReturnData.put("sLocation", this.location);
    aReturnData.put("sCustodian", this.custodian);
    aReturnData.put("sStatus", this.status);
    aReturnData.put("sNotes", this.notes);
    aReturnData.put("sLastEditDate", this.lastEditDate.toString());
    aReturnData.put("sLastEditUser", this.lastEditUser);
    return aReturnData;
  }
  
  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> aOutput = new HashMap<String, Object>();
    aOutput.put("id", this.id);
    aOutput.put("location", this.location);
    aOutput.put("description", this.description);
    aOutput.put("key-id", this.keyId);
    aOutput.put("custodian", this.custodian);
    
    aOutput.put("issued-date", this.inStock);
    String sDate = null;
    if (this.issuedDate != null) {
    	sDate = JhDate.format(this.issuedDate);
    }
    aOutput.put("return-date", sDate);
    sDate = null;
    if (this.returnedDate != null) {
    	sDate = JhDate.format(this.returnedDate);
    }
    aOutput.put("status", this.status);
    aOutput.put("notes", this.notes);
    sDate = null;
    if (this.lastEditDate != null) {
    	sDate = JhDate.format(this.lastEditDate);
    }
    aOutput.put("last-edit-user", this.lastEditUser);
    
    return aOutput;
  }

  public static HashMap<String, Object> toEmptyHashMap() {
    HashMap<String, Object> aOutput = new HashMap<String, Object>();
    aOutput.put("id", 0);
    aOutput.put("location", "");
    aOutput.put("description", "");
    aOutput.put("key-id", "");
    aOutput.put("custodian", "");
    aOutput.put("issued-date", null);
    aOutput.put("return-date", null);
    aOutput.put("status", "");
    aOutput.put("notes", "");
    aOutput.put("last-edit-date", null);
    aOutput.put("last-edit-user", "");
    
    return aOutput;
  }
	  
  public static String toCsvHeader() {
    return "action,id, location, description, key-id, in-stock, custodian, issued-date, return-date, " +
     "status,notes,last-edit-date,last-edit-user\n";
  }
	  
  public String toCsvData(String sAction) {
    String sOutput = 
      sAction + ","
      + this.id + ","
      + '"' + this.location + "\","
      + '"' + this.description + "\","
      + '"' + this.keyId + "\","
      + '"' + this.inStock + "\","
      + '"' + this.custodian + "\","
      + '"' + JhDate.format(this.issuedDate) + "\","
      + '"' + JhDate.format(this.returnedDate) + "\","
      + '"' + this.status + "\"," 
      + '"' + this.notes + "\"," 
      + '"' + JhDate.format(this.lastEditDate) + "\","
      + '"' + this.lastEditUser+ "\"\n";;
    return sOutput;
  }
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public String getLocation() {
    return this.location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getDescription() {
    return this.description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getKeyId() {
    return this.keyId;
  }
  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }
  
  public int getInStock() {
    return this.inStock;
  }
  public void getInStock(int inStock) {
    this.inStock = inStock;
  }
  
  public String getCustodian() {
    return this.custodian;
  }
  public void setCustodian(String description) {
    this.custodian = custodian;
  }
  
  public Date getIssuedDate() {
    return this.issuedDate;
  }
  public void setDecalNumber(Date issuedDate) {
    this.issuedDate = issuedDate;
  }
  
  public Date getReturnDate() {
    return this.returnedDate;
  }
  public void getReturnDate(Date returnedDate) {
    this.returnedDate = returnedDate;
  }
  
  public String getStatus() {
    return this.status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getNotes() {
    return this.notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  public Date getLastEditDate() {
    return this.lastEditDate;
  }
  public void setLastEditDate(Date lastEditDate) {
    this.lastEditDate = lastEditDate;
  }
  
  public String getLastEditUser() {
    return this.lastEditUser;
  }
  public void setLastEditUser(String lastEditUser) {
    this.lastEditUser = lastEditUser;
  }
  
  
}
