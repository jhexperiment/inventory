package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

import com.jhexperiment.java.inventory.dao.ElectronicInvDao;
import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.dao.KeyInvDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;


@SuppressWarnings("serial")
public class ServletUpdate extends HttpServlet {
  
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if ( userService.isUserLoggedIn()){
      User user = userService.getCurrentUser();
      HashMap<String, Object> returnData = new HashMap<String, Object>();
      returnData.put("bError", false);
      try {
        String invType = req.getParameter("sInvType");
        String colName = req.getParameter("sColumnName");
        String value = req.getParameter("sNewValue");
        String hash = req.getParameter("sHash");
        Long id = Long.parseLong(req.getParameter("iId"));
        returnData.put("sHash", hash);
        
        
        if ("general".equals(invType)) {
          GeneralInv generalInv = GeneralInvDao.INSTANCE.getGeneralInv(id);
          generalInv.setLastEditDate(new Date());
          generalInv.setLastEditUser(user.getEmail());
          generalInv.set(colName, value);
          try {
            GeneralInvDao.INSTANCE.update(generalInv);
            returnData.put("sNewValue", generalInv.getData(colName).toString());
            returnData.put("sLastEditDate", generalInv.getLastEditDate().toString());
            returnData.put("sLastEditUser", generalInv.getLastEditUser().toString());
          } 
          catch (InvItemException e) {
            returnData.put("bError", true);
            returnData.put("sErrorMsg", "Error. " + e.getMessage());
          }
          catch (IllegalArgumentException e) {
            returnData.put("bError", true);
            returnData.put("sErrorMsg", "Error. " + e.getMessage());
          }
        }
        else if ("electronics".equals(invType)) {
          ElectronicInv electroniclInv = ElectronicInvDao.INSTANCE.getElectronicInv(id);
          electroniclInv.setLastEditDate(new Date());
          electroniclInv.setLastEditUser(user.getEmail());
          electroniclInv.set(colName, value);
          try {
            ElectronicInvDao.INSTANCE.update(electroniclInv);
            returnData.put("sNewValue", electroniclInv.getData(colName).toString());
            returnData.put("sLastEditDate", electroniclInv.getLastEditDate().toString());
            returnData.put("sLastEditUser", electroniclInv.getLastEditUser().toString());
          } 
          catch (InvItemException e) {
            returnData.put("bError", true);
            returnData.put("sErrorMsg", "Error. " + e.getMessage());
          }
          catch (IllegalArgumentException e) {
            returnData.put("bError", true);
            returnData.put("sErrorMsg", "Error. " + e.getMessage());
          }
        }
        else if ("keys".equals(invType)) {
        	KeyInv keyInv = KeyInvDao.INSTANCE.getKeyInv(id);
            keyInv.setLastEditDate(new Date());
            keyInv.setLastEditUser(user.getEmail());
            keyInv.set(colName, value);
            try {
              KeyInvDao.INSTANCE.update(keyInv);
              returnData.put("sNewValue", keyInv.getData(colName).toString());
              returnData.put("sLastEditDate", keyInv.getLastEditDate().toString());
              returnData.put("sLastEditUser", keyInv.getLastEditUser().toString());
            } 
            catch (InvItemException e) {
              returnData.put("bError", true);
              returnData.put("sErrorMsg", "Error. " + e.getMessage());
            }
            catch (IllegalArgumentException e) {
              returnData.put("bError", true);
              returnData.put("sErrorMsg", "Error. " + e.getMessage());
            }
        }
      }
      catch (NumberFormatException e) {
        returnData.put("bError", true);
        returnData.put("sErrorMsg", "Error. Missing Id.");
      }
      
      
      Gson gson = new Gson();
      String json = gson.toJson(returnData);

      resp.getWriter().print(json);

    }
  }
}
