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
			int iDisplayLength = Integer.parseInt(req.getParameter("iDisplayLength"));
			int iDisplayStart = Integer.parseInt(req.getParameter("iDisplayStart"));
			int iSelectedPage = Integer.parseInt(req.getParameter("iSelectedPage"));
			
			if ("general".equals(sType)) {
				aoData = GeneralInvDao.INSTANCE.listGeneralInv(aSortList, sSearch);
			}
			else if ("electronics".equals(sType)) {
				aoData = ElectronicInvDao.INSTANCE.listElectronicInv(aSortList, sSearch);
			}
			else if ("keys".equals(sType)) {
				aoData = KeyInvDao.INSTANCE.listKeyInv(aSortList, sSearch);
			}
			
			int iTotal = aoData.size();
			Double dPages = new Double(iTotal * 1.0 / iDisplayLength);
			int iPages = (int) Math.ceil(dPages);
			
			iDisplayLength = Math.min(iTotal, iDisplayLength);
			int iDisplayEnd = Math.min(iTotal, iDisplayStart + iDisplayLength - 1);
			aoData = aoData.subList(iDisplayStart - 1, iDisplayEnd);
			
			
			
			
			
			HashMap<String, Object> aReturnData = new HashMap<String, Object>();
			aReturnData.put("sType", sType);
			aReturnData.put("aRecordList", aoData);
			aReturnData.put("iDisplayLength", iDisplayStart + iDisplayLength - 1);
			aReturnData.put("iDisplayStart", iDisplayStart);
			aReturnData.put("iTotal", iTotal);
			aReturnData.put("iDisplayEnd", iDisplayEnd);
			aReturnData.put("iPages", iPages);
			aReturnData.put("iSelectedPage", iSelectedPage + 1);
			
			
			
			
			Gson gson = new Gson();
			String json = gson.toJson(aReturnData);

			resp.getWriter().print(json);
		}
	}
}
