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
public class ServletDelete extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if ( userService.isUserLoggedIn()){
			User user = userService.getCurrentUser();
			HashMap<String, Object> returnData = new HashMap<String, Object>();
			returnData.put("bError", false);
			try {
				String sDescription = "";
				
				String invType = req.getParameter("sInvType");
				String[] aIdList = req.getParameterValues("aIdList[]");
				
				if ("general".equals(invType)) {
					try {
						Date oDate = new Date();
						String sUser = user.getEmail();
						for (String sId : aIdList) {
							long id = new Long(sId);
							GeneralInv generalInv = GeneralInvDao.INSTANCE.getGeneralInv(id);
							generalInv.setLastEditDate(oDate);
							generalInv.setLastEditUser(sUser);
							generalInv.setStatus("DELETED");
							GeneralInvDao.INSTANCE.update(generalInv, "");
							
							
						}
						returnData.put("aIdList", aIdList);
						returnData.put("sLastEditDate", oDate.toString());
						returnData.put("sLastEditUser", sUser);
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
					try {
						Date oDate = new Date();
						String sUser = user.getEmail();
						for (String sId : aIdList) {
							long id = new Long(sId);
							ElectronicInv electronicInv = ElectronicInvDao.INSTANCE.getElectronicInv(id);
							electronicInv.setLastEditDate(oDate);
							electronicInv.setLastEditUser(sUser);
							electronicInv.setStatus("DELETED");
							ElectronicInvDao.INSTANCE.update(electronicInv, "");
							
							
						}
						returnData.put("aIdList", aIdList);
						returnData.put("sLastEditDate", oDate.toString());
						returnData.put("sLastEditUser", sUser);
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
					try {
						Date oDate = new Date();
						String sUser = user.getEmail();
						for (String sId : aIdList) {
							long id = new Long(sId);
							KeyInv keyInv = KeyInvDao.INSTANCE.getKeyInv(id);
							keyInv.setLastEditDate(oDate);
							keyInv.setLastEditUser(sUser);
							keyInv.setStatus("DELETED");
							KeyInvDao.INSTANCE.update(keyInv, "");
							
							
						}
						returnData.put("aIdList", aIdList);
						returnData.put("sLastEditDate", oDate.toString());
						returnData.put("sLastEditUser", sUser);
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
			catch (Exception e) {
	        	
	        }
			
			
			Gson gson = new Gson();
			String json = gson.toJson(returnData);

			resp.getWriter().print(json);

		}
	}
}
