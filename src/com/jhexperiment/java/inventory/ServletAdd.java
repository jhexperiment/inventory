package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

import com.jhexperiment.java.inventory.dao.ElectronicInvDao;
import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.dao.HistoryDao;
import com.jhexperiment.java.inventory.dao.KeyInvDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.History;
import com.jhexperiment.java.inventory.model.KeyInv;

@SuppressWarnings("serial")
public class ServletAdd extends HttpServlet {
  
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    
    if ( userService.isUserLoggedIn()){
      
      Long id = new Long(0);
      String sInvType = req.getParameter("sInvType");
      String sLocation;
      String sDescription;
      String sCustodian;
      String sStatus;
      String sNotes;
      Date oLastEditDate = new Date();
      String sLastEditUser = user.getEmail();
      String sPoNumber;
      Date oPoDate = null;
      Date oPoRecieveDate = null;
      String sDecalNumber;
      String sPropertyNumber;
      int iQuantity;
      String sType;
      String sMake;
      String sModel;
      String sSerialNumber;
      String sFunder;
      String sKeyId;
      int iInStock;
      Date oIssuedDate = null;
      Date oReturnedDate = null;
      GeneralInv oGeneralInv;
      ElectronicInv oElectronicInv;
      KeyInv oKeyInv;
      boolean bIsElectonricItem;
      boolean bIsGenralItem;
      boolean bIsKeyItem;
      List aInvList = new ArrayList();
      HashMap<String, Object> aReturnData = new HashMap<String, Object>();
      
      if ("".equals(sInvType) || sInvType == null) {
        
      }
      else {
        sLocation = req.getParameter("sLocation");
        sDescription = req.getParameter("sDescription");
        sCustodian = req.getParameter("sCustodian");
        sStatus = req.getParameter("sStatus");
        sNotes = req.getParameter("sNotes");
        
        bIsElectonricItem = "electronics".equals(sInvType);
        bIsGenralItem = "general".equals(sInvType);
        bIsKeyItem = "keys".equals(sInvType);
        
        if (bIsGenralItem || bIsElectonricItem) {
          sPoNumber = req.getParameter("sPoNumber");
          String sPoDate = req.getParameter("sPoDate");
          if (sPoDate != null && ! "".equals(sPoDate)) {
            oPoDate = new Date(sPoDate);
          }
          String sPoRecieveDate = req.getParameter("sPoRecieveDate");
          if (sPoRecieveDate != null && ! "".equals(sPoRecieveDate)) {
            oPoRecieveDate = new Date(sPoRecieveDate);
          }
          sDecalNumber = req.getParameter("sDecalNumber");
          sPropertyNumber = req.getParameter("sPropertyNumber");
          
          if (bIsGenralItem) {
            iQuantity = Integer.parseInt(req.getParameter("iQuantity"));
            oGeneralInv = new GeneralInv(sPoNumber, oPoDate, oPoRecieveDate, sDescription,
			                            sDecalNumber, sPropertyNumber, sLocation, 
			                            sCustodian, iQuantity, sStatus, sNotes, 
			                            oLastEditDate, sLastEditUser);  
            this.addGeneralItem(oGeneralInv);
            aReturnData = oGeneralInv.toHashMap();
          } 
          else if (bIsElectonricItem) {
            sType = req.getParameter("sType");
        	sMake = req.getParameter("sMake");
            sModel = req.getParameter("sMake");
            sSerialNumber = req.getParameter("sSerialNumber");
            sFunder = req.getParameter("sFunder");
            oElectronicInv = new ElectronicInv(sPoNumber, oPoDate, oPoRecieveDate, sDescription,
				                               sType, sMake, sModel, sSerialNumber, sDecalNumber, 
				                               sPropertyNumber, sLocation, sCustodian, sFunder,
				                               sStatus, sNotes, oLastEditDate, sLastEditUser);
            this.addElectronicItem(oElectronicInv);
            aReturnData = oElectronicInv.toHashMap();
          }
        }
        else if (bIsKeyItem) {
          sKeyId = req.getParameter("sKeyId");
          iInStock = Integer.parseInt(req.getParameter("iInStock"));
          String sIssuedDate = req.getParameter("sIssuedDate");
          if (sIssuedDate != null && ! "".equals(sIssuedDate)) {
            oIssuedDate = new Date(sIssuedDate);
          }
          String sReturnedDate = req.getParameter("sReturnedDate");
          if (sReturnedDate != null && ! "".equals(sReturnedDate)) {
            oReturnedDate = new Date(sReturnedDate);
          }
          
          oKeyInv = new KeyInv(sLocation, sDescription, sKeyId, iInStock, sCustodian, 
		                      oIssuedDate, oReturnedDate, sStatus, sNotes, oLastEditDate, 
		                      sLastEditUser);
          this.addKeyItem(oKeyInv);
          //aReturnData = oKeyInv.toHashMap();
          
          
        }
        
        
        
      }
      
      Gson gson = new Gson();
      String json = gson.toJson(aReturnData);
      resp.getWriter().print(json);
      //resp.getWriter().print(id);
    }
  }
  
  private void addGeneralItem(GeneralInv generalInv) {
    try {
      GeneralInvDao.INSTANCE.add(generalInv);
    }
    catch (DuplicateInvItemException e) {
      
    }
  }
  
  private void addElectronicItem(ElectronicInv electronicInv) {
    
    try {
      ElectronicInvDao.INSTANCE.add(electronicInv);
    }
    catch (DuplicateInvItemException e) {
      
    }
    
  }
  
  private void addKeyItem(KeyInv keyInv) {
    try {
      KeyInvDao.INSTANCE.add(keyInv);
    }
    catch (DuplicateInvItemException e) {
      
    }
  }
    
  
}
