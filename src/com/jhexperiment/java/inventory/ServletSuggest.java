package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

import com.jhexperiment.java.inventory.dao.CustodianDao;
import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.dao.LocationDao;
import com.jhexperiment.java.inventory.dao.StatusDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;


@SuppressWarnings("serial")
public class ServletSuggest extends HttpServlet {
  
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if ( userService.isUserLoggedIn()){
      
      List aData = new ArrayList();
      String sType = req.getParameter("sType");
      String sHash = req.getParameter("sHash");
      String sSearch = req.getParameter("sSearch");
      
      
      if ("location".equals(sType)) {
        aData = LocationDao.INSTANCE.listLocation(sSearch);
      }
      else if ("custodian".equals(sType)) {
        aData = CustodianDao.INSTANCE.listCustodian(sSearch);
      }
      else if ("status".equals(sType)) {
        aData = StatusDao.INSTANCE.listStatus(sSearch);
      }
      
      HashMap<String, Object> aReturnData = new HashMap<String, Object>();
      aReturnData.put("sType", sType);
      aReturnData.put("sHash", sHash);
      aReturnData.put("aRecordList", aData);
      
      
      Gson gson = new Gson();
      String json = gson.toJson(aReturnData);
      
      resp.setContentType("application/json");
      resp.getWriter().print(json);
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
    /*
    try {
      GeneralInvDao.INSTANCE.add(electronicInv);
    }
    catch (DuplicateInvItemException e) {
      
    }
    */
  }
  
  private void addKeyItem(KeyInv keyInv) {
    /*
    try {
      GeneralInvDao.INSTANCE.add(keyInv);
    }
    catch (DuplicateInvItemException e) {
      
    }
    */
  }
  
}
