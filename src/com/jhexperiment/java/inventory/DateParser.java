package com.jhexperiment.java.inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
	public static String[] aFormatList = {
		"M/y", 
		"M/d/y", 
		"M-d-y",
		"d-M-y",
		"M d, y"
	};
	
	public static Date parse(String sDate) {
		Date oDate = null;
		for (String sFormat : DateParser.aFormatList)
	    {
	        try
	        {
	        	oDate = new SimpleDateFormat(sFormat).parse(sDate);
	            break;
	        }
	        catch (ParseException e) {
	        	
	        }
	    }

		
		return oDate;
	}
}
