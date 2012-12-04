<?xml version="1.0" encoding="UTF-8" ?>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="automaticformannotator.data.PMF" %>
<%@ page import="java.util.List" %>
<%@ page import="automaticformannotator.form.Form" %>
<%@ page import="automaticformannotator.form.AttributeHelper" %>
<%@ page import="automaticformannotator.form.Field" %>
<%@ page import="automaticformannotator.util.Tag" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="css/main.css">
  <script src="scripts/jquery-1.8.3.js"></script>
  <script>
  $(document).ready(function(){
	  
	  // delete tag from form
	  $(".form-tag-delete").live("click", function() {
    	var formkey = $(this).closest('.form').attr("formid");
    	var tag = $(this).siblings(".form-tag-value").text();
    	var deletebutton = this;
      $.post("modifytags", { formkey: formkey, action: "delete", tag: tag}, function(data){
        $(deletebutton).parent().hide();
        $("#parseresults").html(data);
        var tags = $('.result[formid="' + formkey + '"]').attr("tags");
        tags = tags.replace(" " + tag + " ", " ");
        $('.result[formid="' + formkey + '"]').attr("tags", tags);
      });
    });
    
    // add tag to form
    $(".form-tag-add").click(function(){
    	var tagname = $(this).siblings(".add-tag-input").val(); 
    	var formkey = $(this).closest('.form').attr("formid");
      var deletebutton = this;
      
    	$.post("modifytags", { formkey: formkey, action: "add", tag: tagname}, function(data){
    		$(deletebutton).parent().prepend('<span class="form-tag">' + tagname + '<a class="form-tag-delete" href="#">X</a></span>');
    		$("#parseresults").html(data);
    		var tags = $('.result[formid="' + formkey + '"]').attr("tags");
    		$('.result[formid="' + formkey + '"]').attr("tags", " " + tags + " " + tagname);
    	});
    });
    
    // search button key up
    $("#search-input").keyup(function() {
    	if ($(this).val() == "") {
    		$("#results").hide();
    		$("#forms").show();
    	}
    	else {
    		var tag = $(this).val();
    		$('.result').hide();
    		$('.result[tags~="' + tag + '"]').show();
    		$("#results").show();
    		$("#forms").hide();
    	}
    });
    
    // Parse page button
    $("#parse-button").click(function() {
    	// Submit a form to the page parser
    	var url = $("#parsepageinput").val();
    	alert(url);
    	$.get("pageparser", { page: url}, function(data) {
    		$("#parseresults").html(data);
    	});
    });
  });
  </script>
</head>

<body>
  <div id="body">
    <div id="search-bar">
      <div id="search-bar-inner">
        <span id="search-input-span"><input type="text" id="search-input" /></span>
      </div>      
    </div>
    <div id="forms">
    <% 
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query q = pm.newQuery("select from automaticformannotator.form.Form");

    List<Form> results;
    try {
      results = (List<Form>) q.execute();
      if (!results.isEmpty()) {
    	  for (Form form : results)
    	  {
	    	  out.println("<div class=\"form\" formid=\"" + KeyFactory.keyToString(form.getKey()) + "\">");
	    	  out.println("  <div class=\"form-title\">" + form.getUrl() + "</div>");
	    	  out.println("  <div class=\"form-tags\">");
	    	  for (Tag tag : form.getTags())
	    	  {
	    		  out.println("    <span class=\"form-tag\"><span class=\"form-tag-value\">" + tag.getValue() + "</span><a class=\"form-tag-delete\" href=\"#\">X</a></span>");
	    	  }
	    	  out.println("    <input type=\"text\" class=\"add-tag-input\" />");
	    	  out.println("    <a class=\"form-tag-add\" href=\"#\">Add</a>");
	    	  out.println("  </div>");
	    	  out.println("</div>");
    	  }
      } else {
    	  out.println("<p>No forms parsed yet.</p>");
      }
    %>
    </div>
    <div id="results">
    <%
      if (!results.isEmpty()) {
        for (Form form : results)
        {
          out.print("<div class=\"result\" formid=\"" + KeyFactory.keyToString(form.getKey()) + "\" tags=\"" );
          for (Tag t : form.getTags())
          {
        	  out.print(" " + t.getValue() + " ");
          }
          out.println("\" />");
          out.println("  <div class=\"result-check-container\">");
          out.println("    <input type=\"checkbox\" />");
          out.println("  </div>");
          out.println("  <div class=\"result-item-container\">");
          out.println("    <div class=\"result-title\">" + form.getUrl() + "</div>");
          out.println("    <div class=\"result-fields\">");
          for (Field f : form.getFields())
          {
            out.println("      <span class=\"result-field\">" + f.getType() + "</span>");
          }
          out.println("    </div>");
          out.println("  </div>");
          out.println("</div>");
        }
      } else {
        
      }
    } finally {
      pm.close();
    }
    %>
      <div class="result"><a id="build-query">Build query</a></div>
    </div>
    <br clear="all" />
    <div class="pageparser">
      <h2>Parse a web page</h2>
      <table>
        <tr>
          <td></td>
          <td><input type="text" name="page" id="parsepageinput"/></td>
          <td><a id="parse-button" href="#">Parse</a></td>
        </tr>
      </table>
      <div id="parseresults"></div>
    </div>
  </div>
</body>
</html>