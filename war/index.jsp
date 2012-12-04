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
  var currentforms;
  
	//This function creates a new anchor element and uses location
	//properties (inherent) to get the desired URL data. Some String
	//operations are used (to normalize results across browsers).
	//http://james.padolsey.com/javascript/parsing-urls-with-the-dom/
	
	function parseURL(url) {
	   var a =  document.createElement('a');
	   a.href = url;
	   return {
	       source: url,
	       protocol: a.protocol.replace(':',''),
	       host: a.hostname,
	       port: a.port,
	       query: a.search,
	       params: (function(){
	           var ret = {},
	               seg = a.search.replace(/^\?/,'').split('&'),
	               len = seg.length, i = 0, s;
	           for (;i<len;i++) {
	               if (!seg[i]) { continue; }
	               s = seg[i].split('=');
	               ret[s[0]] = s[1];
	           }
	           return ret;
	       })(),
	       file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
	       hash: a.hash.replace('#',''),
	       path: a.pathname.replace(/^([^\/])/,'/$1'),
	       relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
	       segments: a.pathname.replace(/^\//,'').split('/')
	   };
	}
  
  function repopulateForms() {
	  $.getJSON("formretrieval", function(forms) {
		  var output = "";
		  currentforms = forms;
		  
		  // make the forms div html content
		  $.each(forms, function(index, form){
			  output += '<div class="form" formid="' + form.key + '" \>\n';
			  output += '  <div class="form-title">' + parseURL(form.url).host + '</div>\n';
			  output += '  <div class="form-tags">';
			  $.each(form.tags, function(index, tag){
				  output += '    <span class="form-tag"><span class="form-tag-value">' + tag.value + '</span>';
				  output += '<a class="form-tag-delete" href="#">x</a></span>\n';
			  });
			  output += '    <input type="text" class="add-tag-input" />\n';
			  output += '    <a class="form-tag-add" href="#">Add</a>';
			  output += '  </div>\n';
        output += '  <div class="result-fields">\n';
        $.each(form.fields, function(index, field) {
          output += '    <span class="result-field">' + field.type + '</span>\n';
        });
        output += '  </div>\n';
			  output += '</div>\n';
		  });
	    $("#forms").html(output);
	    
		  // make the results div html content
		  output = "";
		  $.each(forms, function(index, form){
			  output += '<div class="result" formid="' + form.key + '" localid="' + index + '" tags="';
			  $.each(form.tags, function(index, tag){
				  output += ' ' + tag.value + ' ';
			  });
			  output += '">\n';
			  output += '  <div class="result-check-container">\n';
			  output += '    <input type="checkbox" class="result-checkbox" />\n';
			  output += '  </div>';
			  output += '  <div class="result-item-container">\n';
			  output += '    <div class="result-title">' + parseURL(form.url).host + '</div>\n';
			  output += '    <div class="result-fields">\n';
			  $.each(form.fields, function(index, field) {
				  output += '      <span class="result-field">' + field.type + '</span>\n';
			  });
			  output += '    </div>\n';
			  output += '  </div>\n';
			  output += '</div>\n';
			  $("#results").html(output);
		  });
	  });
  }
  
  $(document).ready(function(){
	  // Get the list of forms from the database
	  //$.getJSON("formretrieval", function(data) {
		  repopulateForms();
	  //});
	  
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
    $(".form-tag-add").live("click", function(){
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
    		$('#results .result').hide();
    		$('#results .result[tags~="' + tag + '"]').show();
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
    		repopulateForms();
    	});
    });
    
    // checkbox - adding to checked form list
    $(".result-checkbox").live("change", function() {
    	var localid = $(this).closest(".result").attr("localid");
    	var form = currentforms[localid];
    	
    	if ($(this).is(":checked") == true) {
        $('#selectedforms .result[formid="' + form.key + '"]').remove();
       	var output = "";
       	output += '<div class="result" formid="' + form.key + '" localid="' + localid + '">\n';
        output += '  <div class="result-check-container">\n';
        output += '    <input type="checkbox" class="result-checkbox" checked />\n';
        output += '  </div>';
        output += '  <div class="result-item-container">\n';
        output += '    <div class="result-title">' + parseURL(form.url).host + '</div>\n';
        output += '    <div class="result-fields">\n';
        $.each(form.fields, function(index, field) {
          output += '      <span class="result-field">' + field.type + '</span>\n';
        });
        output += '    </div>\n';
        output += '  </div>\n';
        output += '</div>\n';
        $("#selectedforms").append(output);
    	} else {
    		$('#selectedforms .result[formid="' + form.key + '"]').remove();
    		$('#results .result[formid="' + form.key + '"] .result-checkbox').prop("checked", false);
    	}
    });
  });
  </script>
</head>

<body>
  <div id="search-bar">
    <div id="search-bar-inner">
      <span id="search-input-span"><input type="text" id="search-input" /></span>
    </div>      
  </div>
  <div id="forms">
  
  </div>
  <div id="results">
  
  </div>
  <div id="body">


    <br clear="all" />
    <div id="selectedforms">
    
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