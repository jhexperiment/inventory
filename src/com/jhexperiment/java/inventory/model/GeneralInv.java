package com.jhexperiment.java.inventory.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jhexperiment.java.inventory.InvalidGeneralItemException;
import com.jhexperiment.java.inventory.JhDate;

@Entity
public class GeneralInv implements Comparable<GeneralInv> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long DT_RowId;
  private String poNumber;
  private Date poDate;
  private Date poRecieveDate;
  private String description;
  private String decalNumber;
  private String propertyNumber;
  private String location;
  private String custodian;
  private int  quantity;
  private String status;
  private String notes;
  private Date lastEditDate;
  private String lastEditUser;
  
  public void set(String key, Object value) {
    /* TODO: re-order based on most requested after profiling. */
    /* TODO: validation and exception raising. */
    
    if ("DT_RowId".equals(key)) {
      this.DT_RowId = (Long) value;
    }
    else if ("po-number".equals(key)) {
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
    else if ("quantity".equals(key)) {
      this.quantity = (Integer) value;
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
    
    if ("DT_RowId".equals(key)) {
      return this.DT_RowId;
    }
    else if ("po-number".equals(key)) {
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
    else if ("quantity".equals(key)) {
      return this.quantity;
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
  
  public GeneralInv(String poNumber, Date poDate, Date poRecieveDate, String description,
            String decalNumber,String propertyNumber, String location, String custodian, 
            int quantity, String status, String notes, Date lastEditDate, 
            String lastEditUser) {
    this.poNumber = poNumber;
    this.poDate = poDate;
    this.poRecieveDate = poRecieveDate;
    this.description = description;
    this.decalNumber = decalNumber;
    this.propertyNumber = propertyNumber;
    this.location = location;
    this.custodian = custodian;
    this.quantity = quantity;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
  }
  
  public GeneralInv(Long id, String poNumber, Date poDate, Date poRecieveDate, String description,
            String decalNumber,String propertyNumber, String location, String custodian, 
            int quantity, String status, String notes, Date lastEditDate, 
            String lastEditUser) {
    this.id = id;
    this.poNumber = poNumber;
    this.poDate = poDate;
    this.poRecieveDate = poRecieveDate;
    this.description = description;
    this.decalNumber = decalNumber;
    this.propertyNumber = propertyNumber;
    this.location = location;
    this.custodian = custodian;
    this.quantity = quantity;
    this.status = status;
    this.notes = notes;
    this.lastEditDate = lastEditDate;
    this.lastEditUser = lastEditUser;
    
  }
  
  public GeneralInv(String[] aData) throws InvalidGeneralItemException {
    try {
      this.id = Long.parseLong(aData[1]);
    }
    catch (Exception e) {
      
    }
    /*
    for (int i = 2; i <= 14; i++) {
      if ("".equals(aData[i])) {
        throw new InvalidGeneralItemException();
      }
    }
    */
    this.poNumber = aData[2];
    this.poDate = (Date) JhDate.parse(aData[3]);
    this.poRecieveDate = (Date) JhDate.parse(aData[4]);
    this.description = aData[5];
    this.decalNumber = aData[6];
    this.propertyNumber = aData[7];
    this.location = aData[8];
    this.custodian = aData[9];
    try {
      this.quantity = Integer.parseInt(aData[10]);
    }
    catch (Exception e) {
      
    }
    this.status = aData[11];
    this.notes = aData[12];
    this.lastEditDate = (Date) JhDate.parse(aData[13]);
    this.lastEditUser = aData[14];
  
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
    aOutput.put("decal-number", this.decalNumber);
    aOutput.put("property-number", this.propertyNumber);
    aOutput.put("location", this.location);
    aOutput.put("custodian", this.custodian);
    aOutput.put("quanitity", this.quantity);
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
    aOutput.put("po-number", "");
    aOutput.put("po-date", null);
    aOutput.put("po-recieve-date", null);
    aOutput.put("description", "");
    aOutput.put("decal-number", "");
    aOutput.put("property-number", "");
    aOutput.put("location", "");
    aOutput.put("custodian", "");
    aOutput.put("quanitity", "");
    aOutput.put("status", "");
    aOutput.put("notes", "");
    aOutput.put("last-edit-date", null);
    aOutput.put("last-edit-user", "");
    
    return aOutput;
  }
  
  public static String toCsvHeader() {
    return "action,id,po-number,po-date,po-recieve-date,description," +
     "decal-number,property-number,location,custodian,quanitity," +
     "status,notes,last-edit-date,last-edit-user\n";
  }
  
  public String toCsvData(String sAction) {
    String sOutput = 
      sAction + ","
      + this.id + ","
      + '"' + this.poNumber + "\","
      + '"' + JhDate.format(this.poDate) + "\","
      + '"' + JhDate.format(this.poRecieveDate) + "\","
      + '"' + this.description + "\","
      + '"' + this.decalNumber + "\","
      + '"' + this.propertyNumber + "\","
      + '"' + this.location + "\","
      + '"' + this.custodian + "\","
      + this.quantity + ","
      + '"' + this.status + "\"," 
      + '"' + this.notes + "\"," 
      + '"' + JhDate.format(this.lastEditDate) + "\","
      + '"' + this.lastEditUser+ "\"\n";;
    return sOutput;
  }
  
  public int compareTo(GeneralInv invItem) {
    int same = this.description.compareTo(invItem.getDescription());
    
    return same;
  }

  public boolean equals(Object obj) {
      if (!(obj instanceof GeneralInv)) {
        return false;
      }
      GeneralInv invItem = (GeneralInv) obj;
      return this.description.equals(invItem.getDescription());
  }
  
  public HashMap<String, Object> toJsonArray() {
    HashMap<String, Object> aReturnData = new HashMap<String, Object>();
    aReturnData.put("iId", this.id);
    aReturnData.put("sDescription", this.description);
    aReturnData.put("sPoNumber", this.poNumber);
    aReturnData.put("sPoDate", this.poDate.toString());
    aReturnData.put("sPoRecieveDate", this.poRecieveDate.toString());
    aReturnData.put("sDecalNumber", this.decalNumber);
    aReturnData.put("sPropertyNumber", this.propertyNumber);
    aReturnData.put("sLocation", this.location);
    aReturnData.put("sCustodian", this.custodian);
    aReturnData.put("iQuantity", this.quantity);
    aReturnData.put("sStatus", this.status);
    aReturnData.put("sNotes", this.notes);
    aReturnData.put("sLastEditDate", this.lastEditDate.toString());
    aReturnData.put("sLastEditUser", this.lastEditUser);
    return aReturnData;
  }
  
  public ArrayList<Object> toDataTableXmlHttpResponse() {
    ArrayList<Object> output = new ArrayList<Object>();
    output.add(this.id);
    output.add(this.DT_RowId);
    output.add(this.poNumber);
    output.add(this.description);
    output.add(this.decalNumber);
    output.add(this.propertyNumber);
    output.add(this.location);
    output.add(this.custodian);
    output.add(this.quantity);
    output.add(this.status);
    output.add(this.notes);
    return output;
  }
  
  public Long getDT_RowId() {
    return DT_RowId;
  }
  
  public void setDT_RowId() {
     this.DT_RowId = new Long(id);
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
    this.poDate = poDate;
  }
  public Date getPoRecieveDate() {
    return this.poRecieveDate;
  }
  public void setPoRecieveDate(Date poRecieveDate) {
    this.poRecieveDate = poRecieveDate;
  }
  
  public String getDescription() {
    return this.description;
  }
  public void setDescription(String description) {
    this.description = description;
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
  
  public int getQuantity() {
    return this.quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
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
