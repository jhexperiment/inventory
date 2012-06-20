<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.jhexperiment.java.inventory.google.GoogleWrapper" %>
<%@ page import="com.jhexperiment.java.inventory.dao.GeneralInvDao" %>
<%@ page import="com.jhexperiment.java.inventory.dao.KeyInvDao" %>
<%@ page import="com.jhexperiment.java.inventory.dao.LocationDao" %>
<%@ page import="com.jhexperiment.java.inventory.dao.CustodianDao" %>
<%@ page import="com.jhexperiment.java.inventory.dao.StatusDao" %>

    
<%
// Get logged in user.
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

ArrayList<String> userList = new ArrayList<String>();
Properties appProps = new Properties();
Properties googleProps = new Properties();
try {
  String path = this.getServletContext().getRealPath("/WEB-INF");
  
  // Get google properties
  FileInputStream googlePropFile = new FileInputStream(path + "/google.properties");
  googleProps.load(googlePropFile);        
  
  // Get genral app properties
  FileInputStream appPropFile = new FileInputStream(path + "/app.properties");
  appProps.load(appPropFile);        
  
  String authorizedGroup = googleProps.getProperty("authorizedGroup");
  // Get list of users authorized to access app
  userList = GoogleWrapper.INSTANCE.getUserList(authorizedGroup, googleProps);
}
catch (Exception e) {
  if (e instanceof FileNotFoundException) {
    // no google.properties file found.
    response.sendRedirect("404.html"); 
  }
  if (e instanceof IOException) {
    response.sendRedirect("404.html");
  }
}


// Check that user has logged in.
String url = userService.createLoginURL(request.getRequestURI());
if ( userService.isUserLoggedIn() ){
  // user IS logged in, genertate logout url
  url = userService.createLogoutURL(request.getRequestURI()); // logout url
  if (! userList.contains(user.getEmail())) {
    // redirect to logout url (log user out) if not authorized to view app
    response.sendRedirect(url); 
  }
}
else {
  // user is NOT logged in, redirect to login screen
  response.sendRedirect(url);  
}
// Passed all validations, load app.


//GeneralInvDao.INSTANCE.insertTmpData();
//LocationDao.INSTANCE.findUniqueLocationFromInventory();
//LocationDao.INSTANCE.clearDatabase();
//LocationDao.INSTANCE.insertTmpData();
//CustodianDao.INSTANCE.insertTmpData();
//StatusDao.INSTANCE.insertTmpData();

//KeyInvDao.INSTANCE.insertTempData();
%>
<!DOCTYPE html>

<html>
  <head>
    <title>Inventory</title>
    <script type="text/javascript" src="/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="/js/jquery/jquery-ui.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.form.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.tools.min.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.md5.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.extend.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>
    <link rel="stylesheet" type="text/css" href="css/jquery/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="css/common.css"/>
    <link rel="stylesheet" type="text/css" href="css/main.css"/>
    <meta charset="utf-8"> 
  </head>
  <body>
	  <input type="hidden" id="importRecordLimit" value="<%= appProps.getProperty("importRecordLimit") %>">

    <div id="modal">
      <div class="wrapper">
	      <div class="message-box">
	        <div class="message">&nbsp;</div>
	        <img src="/images/progress.bar.loader.gif" />
	      </div>
      </div>
    </div>
    
    <div id="progress-box" class="state-hide">
      <div class="message-box">
         <div class="message">&nbsp;</div>
         <img src="/images/progress.bar.loader.gif" />
       </div>
    </div>

	  <div id="app-nav-bar">
	    <div id="mail-nav-item" class="app-nav-left-item">
	      <a href="http://mail.<%= googleProps.getProperty("domain")%>">Mail</a>
	    </div>
	    <div id="calendar-nav-item" class="app-nav-left-item">
	      <a href="http://calendar.<%= googleProps.getProperty("domain")%>">Calendar</a>
	    </div>
	    <div id="docs-nav-item" class="app-nav-left-item">
	      <a href="http://docs.<%= googleProps.getProperty("domain")%>">Documents</a>
	    </div>
	    <div id="sites-nav-item" class="app-nav-left-item">
	      <a href="http://sites.<%= googleProps.getProperty("domain")%>">Sites</a>
	    </div>
	    <div id="inventory-nav-item" class="current-nav-item app-nav-left-item">
	      <a href="/">Inventory</a>
	    </div>
	    <div id="more-nav-item" class="app-nav-left-item">
	      <div class="text">Extras</div> 
	      <span class="ui-corner-all">
	        <span class="ui-icon ui-icon-triangle-1-s"></span>
	      </span>
	    </div>
	    
	    
	    <div id="user-nav-item" class="app-nav-right-item">
	      <input class="logout-url" type="hidden" value="<%= /*url*/ 1 %>">
	      <div class="text"><%=user.getEmail() %></div>
	      <span class="ui-corner-all">
	        <span class="ui-icon ui-icon-triangle-1-s"></span>
	      </span>
	    </div>
	  </div>
	  <div id="header">
	    <div id="logo"><img alt="logo" src="/images/maili_banner_09.jpg"/></div>
	  </div>
	  
	  
	  <div id="content-header">
	    <div id="inventory-type" class="ui-buttonset">
	        <label id="general" class="ui-state-active ui-button ui-widget ui-state-default ui-corner-left" role="button">
	          <span class="ui-button-text">General</span>
	          <span class="ui-corner-all">
	            <span class="ui-icon ui-icon-lightbulb"></span>
	          </span>
	        </label>
	        <label id="electronics" class="ui-button ui-widget ui-state-default " role="button" >
	          <span class="ui-button-text">Electronics</span>
	          <span class="ui-corner-all">
	            <span class="ui-icon ui-icon-print"></span>
	          </span>
	        </label>
	        <label id="keys" class="ui-button ui-widget ui-state-default ui-corner-right" role="button">
	          <span class="ui-button-text">Keys</span>
	          <span class="ui-corner-all">
	            <span class="ui-icon ui-icon-key"></span>
	          </span>
	        </label>
	      </div>
	  </div>
	  
	  <div id="result-container">
      <div class="label ui-widget ui-widget-header">
          <span id="clear-button" class="ui-button ui-widget ui-state-default ui-corner-all" type="button">
            Clear
          </span>
          <div class="title">
            <span class="text">Results:</span>
            <span class="count"></span>
          </div>
          <span id="icon" class="ui-state-default ui-corner-all">
            <span class="ui-icon ui-icon-arrowthickstop-1-s"></span>
          </span>
      </div>
      <div class="table-container ui-widget ui-widget-content">
        <table id="result">
          
        </table>
      </div>
    </div>
	  
	  <div id="content" class="ui-widget ui-widget-content">
	  
	    <div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	      <div id="search-container">
	        <span class="record-count"></span>
	      
	        <span id="add-button" type="button" class="ui-button ui-widget ui-state-default ui-corner-all">
	          Add
	        </span>
	        <span id="delete-button"  type="button" class="ui-button ui-widget ui-state-disabled ui-corner-all">
	          Delete
	        </span>
	        
<%
int[] aRecordCount = { 10, 25, 50, 100, 200 };
%>
	        <span class="show-count">
	          Show
	          <select>
<%
  for (int iCount : aRecordCount ) {
%>
              <option><%=iCount %></option>
<%
  }
%>
	          </select>
	          entries
	        </span>
	        
	        <input id="search-query" type="text">
	        <span id="search-button"  type="button" class="ui-button ui-widget ui-state-default ui-corner-all">
	          Search
	        </span>
	      </div>
	      
	      <div id="general-tab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
	        <table id="general-table" class="inventory-table display">
	          <thead>
	            <tr class="ui-widget-header">
	              <th id="info-btn">
	                <input type="checkbox" />
	              </th>
	              <th id="id">ID</th>
	              <th id="po-number" class="sortable">
	                PO #
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="poNumber" />
	              </th>
	              <th id="description" class="sortable">
	                Description
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="description" />
	              </th>
	              <th id="decal-number" class="sortable">
	                Decal #
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="decalNumber" />
	               </th>
	              <th id="property-number" class="sortable">
	                Property #
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="propertyNumber" />
	              </th>
	              <th id="location" class="sortable">
	                Location
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="location" />
	              </th>
	              <th id="custodian" class="sortable">
	                Custodian
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="custodian" />
	              </th>
	              <th id="quantity" class="sortable">
	                Qty
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="quantity" />
	              </th>
	              <th id="status" class="sortable">
	                Status
	                <span class="ui-icon ui-icon-carat-2-n-s"></span>
	                <input class="server-sort-name" type="hidden" value="status" />
	              </th>
	              <th id="notes">Notes</th>
	            </tr>
	          </thead>
	          
	          <tbody>
	          </tbody>
	        </table>
	      </div>
	
	
	      <div id="electronics-tab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
	        <table id="electronics-table" class="inventory-table display">
	          <thead>
	            <tr class="ui-widget-header">
	              <th id="info-btn">
	                <input type="checkbox" />
	              </th>
	              <th id="id">ID</th>
	              <th id="po-number" class="sortable">
                  PO #
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="poNumber" />
                </th>
                <th id="description" class="sortable">
                  Description
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="description" />
                </th>
                <th id="make" class="sortable">
                  Make
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="make" />
                </th>
                <th id="model" class="sortable">
                  Model
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="model" />
                </th>
                <th id="serial-number" class="sortable">
                  Serial #
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="serialNumber" />
                </th>
                <th id="decal-number" class="sortable">
                  Decal #
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="decalNumber" />
                 </th>
                <th id="property-number" class="sortable">
                  Property #
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="propertyNumber" />
                </th>
                <th id="location" class="sortable">
                  Location
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="location" />
                </th>
                <th id="custodian" class="sortable">
                  Custodian
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="custodian" />
                </th>
                <th id="funder" class="sortable">
                  Funder
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="funder" />
                </th>
                <th id="status" class="sortable">
                  Status
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="status" />
                </th>
                <th id="notes">Notes</th>  
	            </tr>
	          <thead>
	          <tbody>
	          </tbody>
	        </table>
	      </div>
	      
	      <div id="keys-tab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
	        <table id="keys-table" class="inventory-table display">
	          <thead>
	            <tr class="ui-widget-header">
	              <th id="info-btn">
                  <input type="checkbox" />
                </th>
                <th id="id">ID</th>
                <th id="location" class="sortable">
                  Location
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="location" />
                </th>
                <th id="description" class="sortable">
                  Description
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="description" />
                </th>
                <th id="key-id" class="sortable">
                  ID
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="keyId" />
                </th>
                <th id="in-stock" class="sortable">
                  In Stock
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="inStock" />
                </th>
                <th id="custodian" class="sortable">
                  Custodian
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="custodian" />
                </th>
                <th id="status" class="sortable">
                  Status
                  <span class="ui-icon ui-icon-carat-2-n-s"></span>
                  <input class="server-sort-name" type="hidden" value="status" />
                </th>
                <th id="notes">Notes</th>  
	            </tr>
	          </thead>
	          <tbody>
            </tbody>
	        </table>
	      </div>
	    </div>
	  </div>
	  <div id="pagination" class="fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix">
	    <div id="display-info" class="">
	      Showing 
	      <span class="start">??</span> 
        to 
	      <span class="length">??</span> 
        of 
        <span class="total">??</span> 
        entries
      </div>
      <div id="paginate" class="dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers">
        <a id="first" class="first ui-corner-tl ui-corner-bl ui-button ui-state-default ui-state-disabled" tabindex="0">
          First
        </a>
        <a id="previous" class="previous ui-button ui-state-default ui-state-disabled" tabindex="0">
          Previous
        </a>
        <span>
          <a class="fg-button ui-button ui-state-default ui-state-disabled" tabindex="0">1</a>
        </span>
        <a id="next" class="next ui-button ui-state-default ui-state-disabled" tabindex="0">
          Next
        </a>
        <a id="last" class="last ui-corner-tr ui-corner-br ui-button ui-state-default ui-state-disabled" tabindex="0">
          Last
        </a>
      </div>
    </div>
	  <div id="footer">
	    &copy; 2011 A <a href="http://www.jhexperiment.com">JH Experiment</a>&nbsp;&nbsp;&nbsp;&nbsp;
	  </div>
	  
	  
	  <div id="general-item-dialog" class="dialog" title="Add new General inventory item.">
      <div class="disclaimer"><span class="red">*required</span></div>
      
      <div id="description" class="info-item">
        <span class="label">Description/Name<span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required" />
        </span>
      </div>
      
      <div id="po-number" class="info-item">
        <span class="label">PO #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="po-date" class="info-item">
        <span class="label">PO Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="po-recieve-date" class="info-item">
        <span class="label">PO Recieve Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="decal-number" class="info-item">
        <span class="label">Decal #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="property-number" class="info-item">
        <span class="label">Property #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="location" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Location<span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" />
        </span>
      </div>
      
      <div id="custodian" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Custodian</span>
        <span class="input">
          <input type="text" class="suggestable" />
        </span>
      </div>
      
      <div id="quantity" class="info-item">
        <span class="label">Quantity<span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required" value="1" />
        </span>
      </div>
      
      <div id="status" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Status<span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" value="ACTIVE" />
        </span>
      </div>
      
      <div id="notes" class="info-item">
        <span class="label">Notes</span>
        <span class="input">
          <textarea></textarea>
        </span>
      </div>
      
    </div>
    
    <div id="electronics-item-dialog" class="dialog" title="Add new Electronic inventory item.">
      <div class="disclaimer"><span class="red">*required</span></div>
      
      <div id="description" class="info-item">
        <span class="label">Description/Name <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required" />
        </span>
      </div>
      
      <div id="make" class="info-item">
        <span class="label">Make</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="model" class="info-item">
        <span class="label">Model</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="serial-number" class="info-item">
        <span class="label">Serial #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="po-number" class="info-item">
        <span class="label">PO #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="po-date" class="info-item">
        <span class="label">PO Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="po-recieve-date" class="info-item">
        <span class="label">PO Recieve Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="decal-number" class="info-item">
        <span class="label">Decal #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="property-number" class="info-item">
        <span class="label">Property #</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="location" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Location <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" />
        </span>
      </div>
      
      <div id="custodian" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Custodian</span>
        <span class="input">
          <input type="text" class="suggestable" />
        </span>
      </div>
      
      <div id="funder" class="info-item">
        <span class="label">Funder</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="status" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Status <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" value="ACTIVE" />
        </span>
      </div>
      
      <div id="notes" class="info-item">
        <span class="label">Notes</span>
        <span class="input">
          <textarea></textarea>
        </span>
      </div>
      
    </div>
    
    <div id="keys-item-dialog" class="dialog" title="Add new Key inventory item.">
      <div class="disclaimer"><span class="red">*required</span></div>
      
      <div id="location" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Location <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" />
        </span>
      </div>
      
      <div id="description" class="info-item">
        <span class="label">Description/Name <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required" />
        </span>
      </div>
      
      <div id="key-id" class="info-item">
        <span class="label">ID</span>
        <span class="input">
          <input type="text" />
        </span>
      </div>
      
      <div id="in-stock" class="info-item">
        <span class="label">In Stock</span>
        <span class="input">
          <input type="checkbox" />
        </span>
      </div>
      
      <div id="custodian" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Custodian</span>
        <span class="input">
          <input type="text" class="suggestable" />
        </span>
      </div>
      
      <div id="issued-date" class="info-item">
        <span class="label">Issued Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="returned-date" class="info-item">
        <span class="label">Returned Date</span>
        <span class="input">
          <input type="text" class="datepicker" />
        </span>
      </div>
      
      <div id="status" class="info-item">
        <div class="icon ui-state-default ui-corner-all">
          <span class="ui-icon ui-icon-triangle-1-s"></span>
        </div>
        <span class="label">Status <span class="red">*</span></span>
        <span class="input">
          <input type="text" class="required suggestable" value="ACTIVE" />
        </span>
      </div>
      
      <div id="notes" class="info-item">
        <span class="label">Notes</span>
        <span class="input">
          <textarea></textarea>
        </span>
      </div>
      
    </div>
	  
	</body>
</html>