package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class ServletList extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if ( userService.isUserLoggedIn()){
			
			List aoData = new ArrayList();
			String sType = req.getParameter("sType");
			String[] aSortList = req.getParameterValues("aSortList[]");
			String sSearch = req.getParameter("sSearch");
			
			if ("general".equals(sType)) {
				aoData = GeneralInvDao.INSTANCE.listGeneralInv(aSortList, sSearch);
			}
			else if ("electronics".equals(sType)) {
				aoData = ElectronicInvDao.INSTANCE.listElectronicInv(aSortList, sSearch);
			}
			else if ("keys".equals(sType)) {
				aoData = KeyInvDao.INSTANCE.listKeyInv(aSortList, sSearch);
			}
			
			HashMap<String, Object> aReturnData = new HashMap<String, Object>();
			aReturnData.put("sType", sType);
			aReturnData.put("aRecordList", aoData);
			
			
			Gson gson = new Gson();
			String json = gson.toJson(aReturnData);

			resp.getWriter().print(json);
		}
	}
}
