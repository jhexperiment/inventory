package com.jhexperiment.java.inventory;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.bytecode.opencsv.CSVReader;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.jhexperiment.java.inventory.dao.ElectronicInvDao;
import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.dao.KeyInvDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;


/**
 * Servlet to handle importing csv files.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class ServletImport extends HttpServlet {
  private static final Logger log = Logger.getLogger(ServletImport.class.getName());
  
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();
    if ( userService.isUserLoggedIn()) {
      Properties appProps = new Properties();
      String path = this.getServletContext().getRealPath("/WEB-INF");
      FileInputStream appPropFile = new FileInputStream(path + "/app.properties");
      appProps.load(appPropFile);
      
      ArrayList<HashMap<String, Object>> inventoryList = new ArrayList<HashMap<String, Object>>();
      String inventoryType = new String();
      int importRecordLimit = Integer.parseInt(appProps.getProperty("importRecordLimit"));
      String recordLimit = "";
      try {
        
        
        ServletFileUpload upload = new ServletFileUpload();
        
        FileItemIterator iterator = upload.getItemIterator(req);
        while (iterator.hasNext()) {
          FileItemStream item = iterator.next();
          
          if (item.isFormField()) {
            if ("inventory-type".equals(item.getFieldName())) {
              InputStream stream = item.openStream();
              inventoryType = Streams.asString(stream);
            }
            if ("record-limit".equals(item.getFieldName())) {
              try {
                InputStream stream = item.openStream();
                String tmp = Streams.asString(stream);
                recordLimit = tmp;
                importRecordLimit = Integer.parseInt(tmp);
              }
              catch (Exception e) {} // no recordLimit supplied
            }
          } 
          else {
            log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());
            InputStream stream = item.openStream();
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            String [] aData;
            // read header line
            String [] aHeaderList = reader.readNext();;
            int count = 0;
            while ((aData = reader.readNext()) != null) {
              if ("general".equals(inventoryType)) {
                HashMap<String, Object> aJsonGeneralItem = this.processGeneral(aData);
                if (aJsonGeneralItem != null) {
                  inventoryList.add(aJsonGeneralItem);
                }
              } 
              else if ("electronics".equals(inventoryType)) {
            	  HashMap<String, Object> aJsonElectronicItem = this.processElectronic(aData);
                  if (aJsonElectronicItem != null) {
                    inventoryList.add(aJsonElectronicItem);
                  }
              } 
              else if ("keys".equals(inventoryType)) {
            	  HashMap<String, Object> aJsonKeyItem = this.processKey(aData);
                  if (aJsonKeyItem != null) {
                    inventoryList.add(aJsonKeyItem);
                  }
              }
              
              count++;
              if (count >= importRecordLimit) {
                String errMsg = "Import record limit (" + importRecordLimit + ") reached. ";
                HashMap<String, Object> aRecordLimitData = new HashMap<String, Object>();
                if ("general".equals(inventoryType)) {
                  aRecordLimitData = GeneralInv.toEmptyHashMap();
                } 
                else if ("electronics".equals(inventoryType)) {
                  aRecordLimitData = ElectronicInv.toEmptyHashMap();
                } 
                else if ("keys".equals(inventoryType)) {
                	aRecordLimitData = KeyInv.toEmptyHashMap();
                }
                aRecordLimitData.put("action", "");
                aRecordLimitData.put("error", errMsg);
                
                inventoryList.add(aRecordLimitData);
                throw new InvItemException(errMsg);
              }
            }
          }  
        }
      } 
      catch (Exception e) {
        if (e instanceof InvItemException) {
          
        }
        else {
          throw new ServletException(e);
        }
      } 
      finally {
        HashMap<String, Object> info = new HashMap<String, Object>();
        info.put("inventory-list", inventoryList);
        info.put("record-limit", importRecordLimit);
        info.put("recordLimit", recordLimit);
        info.put("inventoryType", inventoryType);
        
        Gson gson = new Gson();
        String json = gson.toJson(info);
        
        resp.getWriter().print(json);
      }
    }
  }
  
  private HashMap<String, Object> processGeneral(String[] aData){
    String action = aData[0];
    GeneralInv oGeneralInv = null;
    try {
      oGeneralInv = new GeneralInv(aData);
    }
    catch (InvalidGeneralItemException e) {
      return null;
    }
    
    HashMap<String, Object> aJson = new HashMap<String, Object>();
    
    
    if (action.equals("update")) {
      try {
        aJson = oGeneralInv.toHashMap();
        aJson.put("action", action);
        GeneralInvDao.INSTANCE.update(oGeneralInv, "import");
      }
      catch (Exception e) {
        if (e instanceof InvItemException) {
          aJson.put("error", e.getMessage());
        }
      }
    }
    else if (action.equals("add")) {
      try {
        aJson = oGeneralInv.toHashMap();
        aJson.put("action", action);
        GeneralInvDao.INSTANCE.add(oGeneralInv);
        aJson.put("id", oGeneralInv.getId());
      }
      catch (Exception e) {
        if (e instanceof DuplicateInvItemException) {
          aJson.put("error", e.getMessage());
          aJson.put("duplicate", true);
        }
        else if (e instanceof InvItemException) {
          aJson.put("error", e.getMessage());
        }
      }
    }
    else if (action.equals("remove")) {
      try {
        aJson = oGeneralInv.toHashMap();
        aJson.put("action", action);
        //GeneralInvDao.INSTANCE.remove(oGeneralInv.getId());
        /*
        Long id = new Long(absenceInfo[1]);
        absenceJson.put("id", id);
        absence = Dao.INSTANCE.remove(id);
        absenceJson.put("action", action);
        absenceJson.put("employmentType", absence.getEmploymentType());
        absenceJson.put("date", absence.getDate());
        absenceJson.put("reason", absence.getReason());
        absenceJson.put("name", absence.getName());
        absenceJson.put("hours", absence.getHours());
        absenceJson.put("formSubmitted", absence.getFormSubmitted());
        */        
      }
      catch (Exception e) {
        if (e instanceof NumberFormatException) {
          aJson.put("error", "Error: ID required when removing. ");
        }
        else if (e instanceof InvItemException) {
          aJson.put("error", "Error: ID: " + oGeneralInv.getId() + ". " + e.getMessage());
        }
      }
    }
    else if ("".equals(action) || action == null) {
      aJson = oGeneralInv.toHashMap();
      aJson.put("action", action);
      aJson.put("error", "Error: An action is required. ");
    }
    else {
      aJson = oGeneralInv.toHashMap();
      aJson.put("action", action);
      aJson.put("error", "Error: Unknown action. ");
    }
    
    return aJson;
  }
  
  private HashMap<String, Object> processElectronic(String[] aData){
	    String action = aData[0];
	    ElectronicInv oElectronicInv = null;
	    try {
		  oElectronicInv = new ElectronicInv(aData);
	    }
	    catch (InvalidElectronicItemException e) {
	      return null;
	    }
	    
	    HashMap<String, Object> aJson = new HashMap<String, Object>();
	    
	    
	    if (action.equals("update")) {
	      try {
	        aJson = oElectronicInv.toHashMap();
	        aJson.put("action", action);
	        ElectronicInvDao.INSTANCE.update(oElectronicInv, "import");
	      }
	      catch (Exception e) {
	        if (e instanceof InvItemException) {
	          aJson.put("error", e.getMessage());
	        }
	      }
	    }
	    else if (action.equals("add")) {
	      try {
	        aJson = oElectronicInv.toHashMap();
	        aJson.put("action", action);
	        ElectronicInvDao.INSTANCE.add(oElectronicInv);
	        aJson.put("id", oElectronicInv.getId());
	      }
	      catch (Exception e) {
	        if (e instanceof DuplicateInvItemException) {
	          aJson.put("error", e.getMessage());
	          aJson.put("duplicate", true);
	        }
	        else if (e instanceof InvItemException) {
	          aJson.put("error", e.getMessage());
	        }
	      }
	    }
	    else if (action.equals("remove")) {
	      try {
	        aJson = oElectronicInv.toHashMap();
	        aJson.put("action", action);
	        //GeneralInvDao.INSTANCE.remove(oGeneralInv.getId());
	        /*
	        Long id = new Long(absenceInfo[1]);
	        absenceJson.put("id", id);
	        absence = Dao.INSTANCE.remove(id);
	        absenceJson.put("action", action);
	        absenceJson.put("employmentType", absence.getEmploymentType());
	        absenceJson.put("date", absence.getDate());
	        absenceJson.put("reason", absence.getReason());
	        absenceJson.put("name", absence.getName());
	        absenceJson.put("hours", absence.getHours());
	        absenceJson.put("formSubmitted", absence.getFormSubmitted());
	        */        
	      }
	      catch (Exception e) {
	        if (e instanceof NumberFormatException) {
	          aJson.put("error", "Error: ID required when removing. ");
	        }
	        else if (e instanceof InvItemException) {
	          aJson.put("error", "Error: ID: " + oElectronicInv.getId() + ". " + e.getMessage());
	        }
	      }
	    }
	    else if ("".equals(action) || action == null) {
	      aJson = oElectronicInv.toHashMap();
	      aJson.put("action", action);
	      aJson.put("error", "Error: An action is required. ");
	    }
	    else {
	      aJson = oElectronicInv.toHashMap();
	      aJson.put("action", action);
	      aJson.put("error", "Error: Unknown action. ");
	    }
	    
	    return aJson;
	  }
  
  private HashMap<String, Object> processKey(String[] aData){
	    String action = aData[0];
	    KeyInv oKeyInv = null;
	    try {
		  oKeyInv = new KeyInv(aData);
	    }
	    catch (InvalidKeyItemException e) {
	      return null;
	    }
	    
	    HashMap<String, Object> aJson = new HashMap<String, Object>();
	    
	    
	    if (action.equals("update")) {
	      try {
	        aJson = oKeyInv.toHashMap();
	        aJson.put("action", action);
	        KeyInvDao.INSTANCE.update(oKeyInv, "import");
	      }
	      catch (Exception e) {
	        if (e instanceof InvItemException) {
	          aJson.put("error", e.getMessage());
	        }
	      }
	    }
	    else if (action.equals("add")) {
	      try {
	        aJson = oKeyInv.toHashMap();
	        aJson.put("action", action);
	        KeyInvDao.INSTANCE.add(oKeyInv);
	        aJson.put("id", oKeyInv.getId());
	      }
	      catch (Exception e) {
	        if (e instanceof DuplicateInvItemException) {
	          aJson.put("error", e.getMessage());
	          aJson.put("duplicate", true);
	        }
	        else if (e instanceof InvItemException) {
	          aJson.put("error", e.getMessage());
	        }
	      }
	    }
	    else if (action.equals("remove")) {
	      try {
	        aJson = oKeyInv.toHashMap();
	        aJson.put("action", action);
	        //GeneralInvDao.INSTANCE.remove(oGeneralInv.getId());
	        /*
	        Long id = new Long(absenceInfo[1]);
	        absenceJson.put("id", id);
	        absence = Dao.INSTANCE.remove(id);
	        absenceJson.put("action", action);
	        absenceJson.put("employmentType", absence.getEmploymentType());
	        absenceJson.put("date", absence.getDate());
	        absenceJson.put("reason", absence.getReason());
	        absenceJson.put("name", absence.getName());
	        absenceJson.put("hours", absence.getHours());
	        absenceJson.put("formSubmitted", absence.getFormSubmitted());
	        */        
	      }
	      catch (Exception e) {
	        if (e instanceof NumberFormatException) {
	          aJson.put("error", "Error: ID required when removing. ");
	        }
	        else if (e instanceof InvItemException) {
	          aJson.put("error", "Error: ID: " + oKeyInv.getId() + ". " + e.getMessage());
	        }
	      }
	    }
	    else if ("".equals(action) || action == null) {
	      aJson = oKeyInv.toHashMap();
	      aJson.put("action", action);
	      aJson.put("error", "Error: An action is required. ");
	    }
	    else {
	      aJson = oKeyInv.toHashMap();
	      aJson.put("action", action);
	      aJson.put("error", "Error: Unknown action. ");
	    }
	    
	    return aJson;
	  }
  
}
