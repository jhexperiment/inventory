package com.jhexperiment.java.inventory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JhDate {
	public static String[] aFormatList = {
		"MM-dd-yy",
		"MMM-dd-yyyy", 
		"MM/dd/yyyy",
		"MM.dd.yyyy",
		"MM/dd/yy",
		"MM-dd-yyyy",
		"MM.dd.yyyy",
		"MM.dd.yy",
		"MMMM/dd/yyyy",
		"MMMM.dd.yyyy",
		"MMMM/dd/yy",
		"MMMM.dd.yy",
		"MMMM-dd-yyyy",
		"MMMM.dd.yyyy",
		"MMMM-dd-yy",
		"MMMM.dd.yy",
		"MMM/dd/yyyy",
		"MMM.dd.yyyy",
		"MMM/dd/yy",
		"MMM.dd.yy",
		"MMM.dd.yyyy", 
		"MMM-dd-yy", 
		"MMM.dd.yy", 
		"dd/MMM/yyyy", 
		"dd.MMM.yyyy", 
		"dd/MMM/yy", 
		"dd.MMM.yy", 
		"dd-MMM-yyyy", 
		"dd.MMM.yyyy", 
		"dd-MMM-yy", 
		"dd.MMM.yy", 
		"MMMM dd, yyyy",
		"MMMM dd, yy"
	};
	
	public static Date strtotime(String sDate) {
		Date dtDate = null;
		
		for (String sFormat : JhDate.aFormatList) {
			try {
				dtDate = new SimpleDateFormat(sFormat).parse(sDate);
				break;
			}
			catch (Exception e) {
				
			}
		}
		
		return dtDate;
	}
	
	public static Date parse(String sDate) {
		
		return JhDate.strtotime(sDate);
	}
	
	public static String format(Date dtDate) {
		if (dtDate == null) {
			return "";
		}
		SimpleDateFormat oFormatter = new SimpleDateFormat(JhDate.aFormatList[0]);
		
		return oFormatter.format(dtDate);
	}
}
