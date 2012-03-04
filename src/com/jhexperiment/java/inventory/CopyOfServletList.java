package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;


@SuppressWarnings("serial")
public class CopyOfServletList extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if ( userService.isUserLoggedIn()){
			
			/* Paging */
			int iDisplayStart;
			int iDisplayLength;
			try {
				iDisplayStart = Integer.parseInt(req.getParameter("iDisplayStart"));
			}
			catch (Exception e) {
				iDisplayStart = 0;
			}
			try {
				iDisplayLength = Integer.parseInt(req.getParameter("iDisplayLength"));
			}
			catch (Exception e) {
				iDisplayLength = 0;
			}
			
			/* Ordering */
			String[] sColumns = req.getParameter("sColumns").split(",");
			Integer iSortCol_0;
			try {
				iSortCol_0 = Integer.parseInt(req.getParameter("iSortCol_0"));
			}
			catch (NumberFormatException e) {
				iSortCol_0 = null;
			}
			
			Integer iSortingCols;
			try {
				iSortingCols = Integer.parseInt(req.getParameter("iSortingCols"));
			}
			catch (NumberFormatException e) {
				iSortingCols = null;
			}
			Integer iColumns = new Integer(0);
			try {
				iColumns = Integer.parseInt(req.getParameter("iColumns"));
			}
			catch (NumberFormatException e) {
				iColumns = null;
			}
			HashMap<String, Object> sortCols = new HashMap<String, Object>();
			for (int i=0; i < iSortingCols; i++) {
				Integer iSortCol_i = Integer.parseInt(req.getParameter("iSortCol_" + i));
				String bSortable = req.getParameter("bSortable_" + iSortCol_i);
				if ("true".equals(bSortable)) {
					
					sortCols.put(sColumns[iSortCol_i], req.getParameter("sSortDir_" + i));
				}
				
			}
			
			/* Filtering */
			String sSearch = req.getParameter("sSearch");
			HashMap<String, Object> bSearchableList = new HashMap<String, Object>();
			HashMap<String, Object> sSearchList = new HashMap<String, Object>();
			for (int i=0; i < sColumns.length; i++) {
				bSearchableList.put("bSearchable_" + i, req.getParameter("bSearchable_" + i));
				sSearchList.put("sSearch_" + i, req.getParameter("sSearch_" + i));
			}
			
			List<GeneralInv> aaData = 
				GeneralInvDao.INSTANCE.listGeneralInv(iDisplayStart, iDisplayLength,
													  sColumns, iSortCol_0, iSortingCols,
													  sortCols, sSearch, bSearchableList,
													  sSearchList);
			
			int iTotal = aaData.size();
			int iTotalDisplayRecords = iTotal;
			DataTableXmlHttpResponse xhr = 
				new DataTableXmlHttpResponse(req.getParameter("sEcho"), 
											iTotal,
											iTotalDisplayRecords, 
											aaData);
			
			
			Gson gson = new Gson();
			String json = gson.toJson(xhr);

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
