package com.jhexperiment.java.inventory.model;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jhexperiment.java.inventory.InvalidElectronicItemException;
import com.jhexperiment.java.inventory.JhDate;

@Entity
public class ElectronicInv implements Comparable<ElectronicInv> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String poNumber;
  private Date poDate;
  private Date poRecieveDate;
  private String description;
  private String type;
  private String make;
  private String model;
  private String serialNumber;
  private String decalNumber;
  private String propertyNumber;
  private String location;
  private String custodian;
  private String funder;
  private String status;
  private String notes;
  private Date lastEditDate;
  private String lastEditUser;
  
  
  public void set(String key, Object value) {
	    /* TODO: re-order based on most requested after profiling. */
	    /* TODO: validation and exception raising. */
	    
	    if ("po-number".equals(key)) {
	      this.poNumber = (String) value;
	    }
	    else if ("po-date".equals(key)) {
	      this.poDate = new Date((String) value);
	    }
	    else if ("po-recieve-date".equals(key)) {
	      this.poRecieveDate = new Date((String) value);
	    }
	    else if ("description".equals(key)) {
	      this.description = (String) value;
	    }
	    else if ("type".equals(key)) {
	      this.make = (String) value;
	    }
	    else if ("make".equals(key)) {
	      this.make = (String) value;
	    }
		else if ("model".equals(key)) {
	      this.model = (String) value;
	    }
	    else if ("serial-number".equals(key)) {
	      this.serialNumber = (String) value;
	    }
	    else if ("decal-number".equals(key)) {
	      this.decalNumber = (String) value;
	    }
	    else if ("property-number".equals(key)) {
	      this.propertyNumber = (String) value;
	    }
	    else if ("location".equals(key)) {
	      this.location = (String) value;
	    }
	    else if ("custodian".equals(key)) {
	      this.custodian = (String) value;
	    }
	    else if ("funder".equals(key)) {
	      this.funder = (String) value;
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
	    
    if ("po-number".equals(key)) {
      return this.poNumber;
    }
    else if ("po-date".equals(key)) {
      return this.poDate;
    }
    else if ("po-recieve-date".equals(key)) {
      return this.poRecieveDate;
    }
    else if ("description".equals(key)) {
      return this.description;
    }
    else if ("type".equals(key)) {
      return this.type;
    }
    else if ("make".equals(key)) {
      return this.make;
    }
    else if ("model".equals(key)) {
      return this.model;
    }
    else if ("serial-number".equals(key)) {
      return this.serialNumber;
    }
    else if ("decal-number".equals(key)) {
      return this.decalNumber;
    }
    else if ("property-number".equals(key)) {
      return this.propertyNumber;
    }
    else if ("location".equals(key)) {
      return this.location;
    }
    else if ("custodian".equals(key)) {
      return this.custodian;
    }
    else if ("funder".equals(key)) {
      return this.funder;
    }
    else if ("status".equals(key)) {
      return this.status;
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
	  
  
  public ElectronicInv(String poNumber, Date poDate, Date poRecieveDate, String description,
		     String type, String make, String model, String serialNumber, String decalNumber, 
             String propertyNumber, String location, String custodian, String funder,
             String status, String notes, Date lastEditDate, String lastEditUser) {
    this.poNumber = poNumber;
    this.poDate = poDate;
    this.poRecieveDate = poRecieveDate;
    this.description = description;
    this.type = type;
    this.make = make;
    this.model = model;
    this.serialNumber = serialNumber;
    this.decalNumber = decalNumber;
    this.propertyNumber = propertyNumber;
    this.location = location;
    this.custodian = custodian;
    this.funder = funder;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
  }
  
  public ElectronicInv(Long id, String poNumber, Date poDate, Date poRecieveDate, String description,
		     String type, String make, String model, String serialNumber, String decalNumber, 
             String propertyNumber, String location, String custodian, String funder,
             String status, String notes, Date lastEditDate, String lastEditUser) {
    this.id = id;
    this.poNumber = poNumber;
    this.poDate = poDate;
    this.poRecieveDate = poRecieveDate;
    this.description = description;
    this.type = type;
    this.make = make;
    this.model = model;
    this.serialNumber = serialNumber;
    this.decalNumber = decalNumber;
    this.propertyNumber = propertyNumber;
    this.location = location;
    this.custodian = custodian;
    this.funder = funder;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
  }
  
  public ElectronicInv(String[] aData) throws InvalidElectronicItemException {
    
    try {
      this.id = Long.parseLong(aData[1]);
    }
    catch (Exception e) {
	 
    }
    
    
    /*
    for (int i = 2; i <= 17; i++) {
      if ("".equals(aData[i])) {
        throw new InvalidElectronicItemException();
      }
    }
    */
    try {
	    this.poNumber = aData.length >= 3 ? aData[2] : "";
	    this.poDate = aData.length >= 4 ? (Date) JhDate.parse(aData[3]) : null;
	    this.poRecieveDate = aData.length >= 5 ? (Date) JhDate.parse(aData[4]) : null;
	    this.description = aData.length >= 6 ? aData[5] : "";
	    this.type = aData.length >= 7 ? aData[6] : "";
	    this.make = aData.length >= 8 ? aData[7] : "";
	    this.model = aData.length >= 9 ? aData[8] : "";
	    this.serialNumber = aData.length >= 10 ? aData[9] : "";
	    this.decalNumber = aData.length >= 11 ? aData[10] : "";
	    this.propertyNumber = aData.length >= 12 ? aData[11] : "";
	    this.location = aData.length >= 13 ? aData[12] : "";
	    this.custodian = aData.length >= 14 ? aData[13] : "";
	    this.funder = aData.length >= 15 ? aData[14] : "";
	    this.status = aData.length >= 16 ? aData[15] : "";
	    this.notes = aData.length >= 17 ? aData[16] : "";
	    this.lastEditDate = aData.length >= 18 ? (Date) JhDate.parse(aData[17]) : null;
	    this.lastEditUser = aData.length >= 19 ? aData[18] : "";
    }
    catch (Exception e) {
    	String tmp = e.getMessage();
    	tmp = "";
    }
  
  }
  
  public int compareTo(ElectronicInv invItem) {
    int same = this.description.compareTo(invItem.getDescription());
    
    return same;
  }

  public boolean equals(Object obj) {
      if (!(obj instanceof ElectronicInv)) {
        return false;
      }
      ElectronicInv invItem = (ElectronicInv) obj;
      return this.description.equals(invItem.getDescription());
  }
  
  public HashMap<String, Object> toHashMap() {
    HashMap<String, Object> aOutput = new HashMap<String, Object>();
    aOutput.put("id", this.id);
    aOutput.put("po-number", this.poNumber);
    String sDate = null;
    if (this.poDate != null) {
    	sDate = JhDate.format(this.poDate);
    }
    aOutput.put("po-date", sDate);
    sDate = null;
    if (this.poRecieveDate != null) {
    	sDate = JhDate.format(this.poRecieveDate);
    }
    aOutput.put("po-recieve-date", sDate);
    aOutput.put("description", this.description);
    aOutput.put("type", this.type);
    aOutput.put("make", this.make);
    aOutput.put("model", this.model);
    aOutput.put("serial-number", this.serialNumber);
    aOutput.put("decal-number", this.decalNumber);
    aOutput.put("property-number", this.propertyNumber);
    aOutput.put("location", this.location);
    aOutput.put("custodian", this.custodian);
    aOutput.put("funder", this.funder);
    aOutput.put("status", this.status);
    aOutput.put("notes", this.notes);
    sDate = null;
    if (this.lastEditDate != null) {
    	sDate = JhDate.format(this.lastEditDate);
    }
    aOutput.put("last-edit-date", sDate);
    aOutput.put("last-edit-user", this.lastEditUser);
      
    return aOutput;
  }
  public static HashMap<String, Object> toEmptyHashMap() {
    HashMap<String, Object> aOutput = new HashMap<String, Object>();
    aOutput.put("id", 0);
    aOutput.put("po-number", "");
    aOutput.put("po-date", null);
    aOutput.put("po-recieve-date", null);
    aOutput.put("description", "");
    aOutput.put("type", "");
    aOutput.put("make", "");
    aOutput.put("model", "");
    aOutput.put("serial-number", "");
    aOutput.put("decal-number", "");
    aOutput.put("property-number", "");
    aOutput.put("location", "");
    aOutput.put("custodian", "");
    aOutput.put("funder", "");
    aOutput.put("status", "");
    aOutput.put("notes", "");
    aOutput.put("last-edit-date", null);
    aOutput.put("last-edit-user", "");
  
    return aOutput;
  }
    
  public static String toCsvHeader() {
    return "action,id,po-number,po-date,po-recieve-date,description," +
     "type,make,model,serial-number,decal-number,property-number,location,custodian," +
     "funder,status,notes,last-edit-date,last-edit-user\n";
  }
  
  public String toCsvData(String sAction) {
    String sOutput = 
      sAction + ","
      + this.id + ","
      + '"' + this.poNumber + "\","
      + '"' + JhDate.format(this.poDate) + "\","
      + '"' + JhDate.format(this.poRecieveDate) + "\","
      + '"' + this.description + "\","
      + '"' + this.type + "\","
      + '"' + this.make + "\","
      + '"' + this.model + "\","
      + '"' + this.serialNumber + "\","
      + '"' + this.decalNumber + "\","
      + '"' + this.propertyNumber + "\","
      + '"' + this.location + "\","
      + '"' + this.custodian + "\","
      + '"' + this.funder + "\","
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

  public String getPoNumber() {
    return this.poNumber;
  }
  public void setPoNumber(String poNumber) {
    this.poNumber = poNumber;
  }
  public Date getPoDate() {
    return this.poDate;
  }
  public void setPoDate(Date date) {
    this.poDate = date;
  }
  public Date getPoRecieveDate() {
    return this.poRecieveDate;
  }
  public void setPoRecieveDate(Date date) {
    this.poRecieveDate = date;
  }
	  
  public String getDescription() {
    return this.description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getType() {
    return this.type;
  }
  public void setType(String type) {
    this.type = type;
  }
  
  public String getMake() {
    return this.make;
  }
  public void setMake(String make) {
    this.make = make;
  }
		  
  public String getModel() {
    return this.model;
  }
  public void setModel(String model) {
    this.model = model;
  }
  
  public String getSerialNumber() {
    return this.serialNumber;
  }
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
  
  public String getDecalNumber() {
    return this.decalNumber;
  }
  public void setDecalNumber(String decalNumber) {
    this.decalNumber = decalNumber;
  }
  
  public String getPropertyNumber() {
    return this.propertyNumber;
  }
  public void setPropertyNumber(String propertyNumber) {
    this.propertyNumber = propertyNumber;
  }
  
  public String getLocation() {
    return this.location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  
  public String getCustodian() {
    return this.custodian;
  }
  public void setCustodian(String custodian) {
    this.custodian = custodian;
  }
  
  public String getFunder() {
    return this.funder;
  }
  public void setFunder(String funder) {
    this.funder = funder;
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
