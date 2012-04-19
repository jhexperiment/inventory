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
<%@ page import="com.jhexperiment.java.inventory.dao.HistoryDao" %>
<%@ page import="com.jhexperiment.java.inventory.model.History" %>

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
    <script type="text/javascript" src="/js/jquery/plugins/jquery.extend.js"></script>
    <script type="text/javascript" src="/js/history.js"></script>
    <link rel="stylesheet" type="text/css" href="css/jquery/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="css/common.css"/>
    <link rel="stylesheet" type="text/css" href="css/history.css"/>
    <meta charset="utf-8"> 
  </head>
 <body>
 
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
   
   <div id="search-container">
     <span class="record-count"></span>
     <input id="search-query" type="text">
     <span id="search-button"  type="button" class="ui-button ui-widget ui-state-default ui-corner-all">
       Search
     </span>
   </div>
   
	 <table id="history-list">  
	   <tr>
	     <th id="id">ID</th>
	     <th id="type">Type</th>
	     <th id="invenotry">Inventory</th>
	     <th id="user">User</th>
	     <th id="date">Date</th>
	     <th id="action">Action</th>
	   </tr>
<%
HistoryDao oHistoryDao = HistoryDao.INSTANCE;
for (History oHistory : oHistoryDao.listHistory()) {
%>
      <tr>
        <td id="id">
          <%=oHistory.getId() %>
        </td>
        <td id="type">
          <%=oHistory.getType() %>
        </td>
        <td id="inventory">
          <%=oHistory.getInventory() %>
        </td>
        <td id="user">
          <%=oHistory.getUser() %>
        </td>
        <td id="date">
          <%=oHistory.getDate().toString() %>
        </td>
        <td id="action">
          <%=oHistory.getAction() %>
        </td>
      </tr>
<%
}
%>
    </table>
   
 </body>
</html>


