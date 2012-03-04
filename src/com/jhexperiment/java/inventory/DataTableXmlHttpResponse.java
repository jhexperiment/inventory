package com.jhexperiment.java.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jhexperiment.java.inventory.model.GeneralInv;

public class DataTableXmlHttpResponse {
	private String sEcho;
	private Integer iTotalRecords;
	private Integer iTotalDisplayRecords;
	private List aaData;
	
	public DataTableXmlHttpResponse(String sEcho, Integer iTotalRecords, 
									Integer iTotalDisplayRecords, List aaData) {
		this.sEcho = sEcho;
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.aaData = aaData;
	}
}
