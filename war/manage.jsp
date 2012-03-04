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
<%@ page import="com.jhexperiment.java.inventory.dao.StatusDao" %>
<%@ page import="com.jhexperiment.java.inventory.model.Status" %>
<%@ page import="com.jhexperiment.java.inventory.dao.LocationDao" %>
<%@ page import="com.jhexperiment.java.inventory.model.Location" %>
<%@ page import="com.jhexperiment.java.inventory.dao.CustodianDao" %>
<%@ page import="com.jhexperiment.java.inventory.model.Custodian" %>

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
%>

<!DOCTYPE html>

<html>
  <head>
    <title>Manage</title>
    <script type="text/javascript" src="/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="/js/jquery/jquery-ui.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.form.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.tools.min.js"></script>
    <script type="text/javascript" src="/js/jquery/plugins/jquery.md5.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/js/manage.js"></script>
    <link rel="stylesheet" type="text/css" href="css/jquery/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="css/manage.css"/>
    <meta charset="utf-8"> 
  </head>
 <body>
 
   <div id="status-header" class="header">Status</div>
   <div id="status-list">
<%
StatusDao oStatusDao = StatusDao.INSTANCE;
for (Status oStatus : oStatusDao.listStatus()) {
%>
      <div id="<%=oStatus.getId() %>" class="status-item item">
        <span class="text"><%=oStatus.getName()%></span>
        <span class="action-list">
	        <span class="delete action">delete</span>
	        <span class="rename action">rename</span>
        </span>
      </div>
<%
}
%>
   </div>
   
   <div id="location-header" class="header">Location</div>
   <div id="location-list">
<%
LocationDao oLocationDao = LocationDao.INSTANCE;
for (Location oLocation : oLocationDao.listLocation()) {
%>
      <div id="<%=oLocation.getId() %>" class="location-item item">
        <span class="text"><%=oLocation.getName()%></span>
        <span class="action-list">
          <span class="delete action">delete</span>
          <span class="rename action">rename</span>
        </span>
      </div>
<%
}
%>
   </div>
   
   <div id="custodian-header" class="header">Custodian</div>
   <div id="custodian-list">
<%
CustodianDao oCustodianDao = CustodianDao.INSTANCE;
for (Custodian oCustodian : oCustodianDao.listCustodian()) {
%>
      <div id="<%=oCustodian.getId() %>" class="custodian-item item">
        <span class="text"><%=oCustodian.getName()%></span>
        <span class="action-list">
          <span class="delete action">delete</span>
          <span class="rename action">rename</span>
        </span>
      </div>
<%
}
%>
   </div>
 </body>
</html>


