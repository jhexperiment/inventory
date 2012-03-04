package com.jhexperiment.java.inventory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.jhexperiment.java.inventory.dao.ElectronicInvDao;
import com.jhexperiment.java.inventory.dao.GeneralInvDao;
import com.jhexperiment.java.inventory.dao.KeyInvDao;
import com.jhexperiment.java.inventory.model.ElectronicInv;
import com.jhexperiment.java.inventory.model.GeneralInv;
import com.jhexperiment.java.inventory.model.KeyInv;


/**
 * Servlet to handle exporting absences to a csv file.
 * @author jhxmonkey
 *
 */
@SuppressWarnings("serial")
public class ServletExport extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		if ( userService.isUserLoggedIn()){
			
			String sInventoryType = req.getParameter("inventory-type");
			String sHeader = "";
			String sData = "";
			if ("general".equals(sInventoryType)) {
				sHeader = GeneralInv.toCsvHeader();
				sData = this.getGeneralData();
			}
			else if ("electronics".equals(sInventoryType)) {
				sHeader = ElectronicInv.toCsvHeader();
				sData = this.getElectronicData();
			}
			else if ("keys".equals(sInventoryType)) {
				sHeader = KeyInv.toCsvHeader();
				sData = this.getKeyData();
			}
			
			String sFilename = sInventoryType.replace(' ', '-').toLowerCase();
			resp.setHeader("Content-Type","application/vnd.ms-excel; name='excel'");
			resp.addHeader("Content-Disposition","filename=" + sFilename + ".csv");
			resp.addHeader("Pragma","no-cache");
			resp.addHeader("Expires","0");
			resp.getWriter().print(sHeader + sData);
		}
	}
	
	private String getGeneralData() {
		GeneralInvDao dao = GeneralInvDao.INSTANCE;
		List<GeneralInv> aInventoryList = dao.listGeneralInv();
		String sData = "";
		
		for (GeneralInv oInvItem : aInventoryList) {
			sData += oInvItem.toCsvData("update");
		}
		
		return sData;
	}
	private String getElectronicData() {
		ElectronicInvDao dao = ElectronicInvDao.INSTANCE;
		List<ElectronicInv> aInventoryList = dao.listElectronicInv();
		String sData = "";
		
		for (ElectronicInv oInvItem : aInventoryList) {
			sData += oInvItem.toCsvData("update");
		}
		
		return sData;
	}
	
	private String getKeyData() {
		KeyInvDao dao = KeyInvDao.INSTANCE;
		List<KeyInv> aInventoryList = dao.listKeyInv();
		String sData = "";
		
		for (KeyInv oInvItem : aInventoryList) {
			sData += oInvItem.toCsvData("update");
		}
		
		return sData;
	}
	
}
