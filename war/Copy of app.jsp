<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.jhexperiment.java.inventory.google.GoogleWrapper" %>
<%@ page import="com.jhexperiment.java.inventory.dao.GeneralInvDao" %>





		
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

%>
<!DOCTYPE html>

<html>
	<head>
		<title>Inventory</title>
		<script type="text/javascript" src="/js/jquery/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript" src="/js/jquery/plugins/jquery.dataTables.js"></script>	
		<script type="text/javascript" src="/js/jquery/plugins/jquery.dataTables.editable.js"></script>		
		<script type="text/javascript" src="/js/jquery/plugins/jquery.jeditable.js"></script>		
		<script type="text/javascript" src="/js/jquery/plugins/jquery.validate.js"></script>		
		<script type="text/javascript" src="/js/jquery/plugins/jquery.tools.min.js"></script>
		<script type="text/javascript" src="/js/common.js"></script>
		<script type="text/javascript" src="/js/main.js"></script>
		<link rel="stylesheet" type="text/css" href="css/jquery/jquery-ui.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery.datatables/jui.css"/>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<meta charset="utf-8"> 
	</head>
	<body>

	<div id="appNavBar">
		<div id="mailNavItem" class="appNavLeftItem">
			<a href="http://mail.<%= googleProps.getProperty("domain")%>">Mail</a>
		</div>
		<div id="calendarNavItem" class="appNavLeftItem">
			<a href="http://calendar.<%= googleProps.getProperty("domain")%>">Calendar</a>
		</div>
		<div id="docsNavItem" class="appNavLeftItem">
			<a href="http://docs.<%= googleProps.getProperty("domain")%>">Documents</a>
		</div>
		<div id="sitesNavItem" class="appNavLeftItem">
			<a href="http://sites.<%= googleProps.getProperty("domain")%>">Sites</a>
		</div>
		<div id="inventoryNavItem" class="currentNavItem appNavLeftItem">
			<a href="/">Inventory</a>
		</div>
		<div id="moreNavItem" class="appNavLeftItem">
			<div class="text">Extras</div> 
			<span class="ui-corner-all">
				<span class="ui-icon ui-icon-triangle-1-s"></span>
			</span>
		</div>
		
		
		<div id="userNavItem" class="appNavRightItem">
			<input class="logoutUrl" type="hidden" value="<%= /*url*/ 1 %>">
			<div class="text"><%=user.getEmail() %></div>
			<span class="ui-corner-all">
				<span class="ui-icon ui-icon-triangle-1-s"></span>
			</span>
		</div>
	</div>
	<div id="header">
		<div id="logo"><img alt="logo" src="/images/maili_banner_09.jpg"/></div>
		<div id="searchBar">
			<span id="searchQuery">
				<input type="text" />
			</span>
			<button id="searchButton" class="ui-button ui-widget ui-state-default ui-corner-right ui-button-text-only" role="button" aria-disabled="false">
				<span class="text">Search</span>
				<span class="ui-corner-all">
							<span class="ui-icon ui-icon-search"></span>
				</span>
			</button>
			<img src="images/loader-small.gif" style="display:none;" />
		</div>
	</div>
	<div id="searchResultsContainer">
		<div class="label ui-widget ui-widget-header">
				&nbsp;
				<span class="text">Search Results:</span>
				<span id="icon" class="ui-state-default ui-corner-all">
					<span class="ui-icon ui-icon-arrowthickstop-1-s"></span>
				</span>
		</div>
		<div class="tableContainer ui-widget ui-widget-content">
			<table id="searchResults">
				
			</table>
		</div>
	</div>
	<div id="contentHeader">
		<div id="inventoryType" class="ui-buttonset">
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
	<div id="content" class="ui-widget ui-widget-content">
	
		<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
			<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
					<a href="#generalTab">General</a>
				</li>
				<li class="ui-state-default ui-corner-top">
					<a href="#electronicsTab">Electronics</a>
				</li>
				<li class="ui-state-default ui-corner-top">
					<a href="#keysTab">Keys</a>
				</li>
			</ul>
			
			<div id="generalTab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
				<table id="generalTable" class="inventoryTable display">
					<thead>
						<tr>
							<th class="id">ID</th>
							<th class="poNumber">PO #</th>
							<th class="description">Description</th>
							<th class="decalNumber">Decal #</th>
							<th class="propertyNumber">Property #</th>
							<th class="location">Location</th>
							<th class="custodian">Custodian</th>
							<th class="quantity">Qty</th>
							<th class="status">Status</th>
							<th class="notes">Notes</th>
						</tr>
					</thead>
					
					<tbody>
					</tbody>
				</table>
			</div>


			<div id="electronicsTab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
				<table id="electronicsTable" class="inventoryTable display">
					<thead>
						<tr class="ui-widget-header">
							<th class="id"></th>
							<th class="poNumber">PO #</th>
							<th class="description">Description</th>
							<th class="make">Make</th>
							<th class="model">Model</th>
							<th class="serialNumber">Serial #</th>
							<th class="decalNumber">Decal #</th>
							<th class="propertyNumber">Property #</th>
							<th class="location">Location</th>
							<th class="custodian">Custodian</th>
							<th class="funder">Funder</th>
							<th class="status">Status</th>
							<th class="notes">Notes</th>			
						</tr>
					<thead>
				</table>
			</div>
			
			<div id="keysTab" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
				<table id="keysTable" class="inventoryTable display">
					<thead>
						<tr class="ui-widget-header">
							<th class="id"></th>
							<th class="location">Location</th>
							<th class="description">Description</th>
							<th class="keyId">ID</th>
							<th class="inStock">In Stock</th>
							<th class="custodian">Custodian</th>
							<th class="issueDate">Issued</th>
							<th class="returnDate">Returned</th>
							<th class="status">Status</th>
							<th class="notes">Notes</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	
	<div id="footer">
		&copy; 2011 A <a href="http://www.jhexperiment.com">JH Experiment</a>&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	
	
	<form id="formAddGeneralItemRow" action="#" title="Add new General inventory item. *required">
		<input type="hidden" name="invType" id="invType" value="" />
		
		<label for="poNumber">PO #</label>
		<input type="text" name="poNumber" id="poNumber" value="" />
		<br />
		
		<label for="poDate">PO Date</label>
		<input type="date" name="poDate" id="poDate" class="date" />
		<br />
		
		<label for="poRecieveDate">PO Recieve Date</label>
		<input type="date" name="poRecieveDate" id="poRecieveDate" class="date" />
		<br />
		
		<label for="description">Description<span class="red">*</span></label>
		<input type="text" name="description" id="description" class="required" />
		<br />
		
		<label for="decalNumber">Decal #</label>
		<input type="text" name="decalNumber" id="decalNumber" />
		<br />
		
		<label for="propertyNumber">Property #</label>
		<input type="text" name="propertyNumber" id="propertyNumber" class="number" />
		<br />
		
		<label for="location">Location<span class="red">*</span></label>
		<input type="text" name="location" id="location" class="required" value="G-01" />
		<br />
		
		<label for="custodian">Custodian</label>
		<input type="text" name="custodian" id="custodian" />
		<br />
		
		<label for="quantity">Quantity<span class="red">*</span></label>
		<input type="number" name="quantity" id="quantity" class="required number" value="1" />
		<br />
		
		<label for="status">Status<span class="red">*</span></label>
		<input type="text" name="status" id="status" class="required" value="ACTIVE" />
		<br />
		
		<label for="notes">Notes</label>
		<textarea name="notes" id="notes"></textarea>
		<br />
		
		         
	</form>
	
</body>
</html>