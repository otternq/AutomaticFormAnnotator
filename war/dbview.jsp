<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="automaticformannotator.data.PMF" %>
<%@ page import="java.util.List" %>
<%@ page import="automaticformannotator.form.Form" %>
<%@ page import="automaticformannotator.form.AttributeHelper" %>
<%@ page import="automaticformannotator.util.Tag" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

  <body>
    <h1>Coming Soon</h1>
  
    <h2>Add a form to the database</h2>
    <form action="pageparser" method="get">
      <table>
        <tr>
          <td>Web page to parse:</td>
          <td><input type="text" name="page" /></td>
          <td><input type="submit" value="Submit"/></td>
        </tr>
      </table>
    </form>
    
    <h2>Parsed Forms</h2>
    <ul>
    <%
    // Get the persistance manager from the static persistance manager factory
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query q = pm.newQuery("select from automaticformannotator.form.Form");

    try {
    	List<Form> results = (List<Form>) q.execute();
   	  if (!results.isEmpty()) {
   	    for (Form f : results) {
   	    	out.println("<li>");
   	    	out.println(f.getUrl() + "(");
   	    	for (Tag t : f.getTags())
   	    	{
   	    		out.print(t.getValue() + ", ");
   	    	}
   	    	out.println(")");
   	    	%>
   	    	<table>
	   	    	<form>
	   	    		<tr>
		   	    		<td>Add tag: </td>
		   	    		<td><input type="text" name="tag_name" /></td>
		   	    		<td><input type="submit" value="Submit"/>
		   	    		    <input type="hidden" name="form_name" value="<%= f.getKey() %>" /></td>
	   	    		</tr>
	   	    	</form>
   	    	</table>
   	    	<%
   	    	
   	    }
   	  } else {
   	    // Handle "no results" case
   	    %> <li>No forms have been parsed</li> <%
   	  }
   	} finally {
   	  q.closeAll();
   	}
    %>
    </ul>
    <h2>Build Query</h2>
    <form>
      <table>
        <tr>
          <td>Tag search:</td>
          <td><input type="text" name="tag_query" /></td>
          <td><input type="submit" value="Submit"/></td>
        </tr>
      </table>
    </form>
  </body>
</html>