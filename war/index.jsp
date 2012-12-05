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
	  $("#forms").html('<p>Loading forms...</p>');
	  $.getJSON("formretrieval", function(forms) {
		  var output = "";
		  currentforms = forms;
		  
		  // make the forms div html content
		  $.each(forms, function(index, form){
			  output += '<div class="form" formid="' + form.key + '" localid="' + index + '" tags="'; 
			  $.each(form.tags, function(index, tag){
          output += ' ' + tag.value + ' ';
        });
			  output += '" \>\n';
		    output += '  <div class="form-check-container">\n';
        output += '    <input type="checkbox" class="form-checkbox" />\n';
        output += '  </div>';
        output += '  <div class="form-item-container">\n'
			  output += '    <div class="form-title">' + parseURL(form.url).host + '</div>\n';
			  output += '    <div class="form-tags">\n';
			  $.each(form.tags, function(index, tag){
				  output += '      <span class="form-tag"><span class="form-tag-value">' + tag.value + '</span>';
				  output += '<a class="form-tag-delete" href="#">x</a></span>\n';
			  });
			  output += '      <input type="text" class="add-tag-input" />\n';
			  output += '      <a class="form-tag-add" href="#">Add</a>\n';
			  output += '    </div>\n';
        output += '    <div class="form-fields">\n';
        $.each(form.fields, function(index, field) {
        	var value = field.attributes[0] == null ? '(no name)' : field.attributes[0].value;
          output += '      <span class="form-field">' + value + '</span>\n';
        });
        output += '    </div>\n';
        output += '  </div>\n';
        output += '  <br clear="both" />';
			  output += '</div>\n';
		  });
		  
		  if (output == "") {
			  output += '<p>No forms in the database</p>';
		  }
		  
	    $("#forms").html(output);
	    $("#forms .form:even").css('background-color', '#ddd');
	    $("#forms .form:odd").css('background-color', '#eee');
	  });
  }
  
  $(document).ready(function(){
	  // Get the list of forms from the database
		repopulateForms();
	  
	  // delete tag from form
	  $(".form-tag-delete").live("click", function() {
    	var formkey = $(this).closest('.form').attr("formid");
    	var tag = $(this).siblings(".form-tag-value").text();
    	var deletebutton = this;
      $.post("modifytags", { formkey: formkey, action: "delete", tag: tag}, function(data){
        $(deletebutton).parent().hide();
        $("#parseresults").html(data);
        var tags = $('.form[formid="' + formkey + '"]').attr("tags");
        tags = tags.replace(" " + tag + " ", " ");
        $('.form[formid="' + formkey + '"]').attr("tags", tags);
      });
      
      return false;
    });
    
    // add tag to form
    $(".form-tag-add").live("click", function(){
    	var tagname = $(this).siblings(".add-tag-input").val(); 
    	var formkey = $(this).closest('.form').attr("formid");
      var deletebutton = this;
      
    	$.post("modifytags", { formkey: formkey, action: "add", tag: tagname}, function(data){
    		$(deletebutton).parent().prepend('<span class="form-tag">' + tagname + '<a class="form-tag-delete" href="#">x</a></span>');
    		//alert(tagname);
    		$("#parseresults").html(data);
    		var tags = $('.form[formid="' + formkey + '"]').attr("tags");
    		$('.form[formid="' + formkey + '"]').attr("tags", " " + tags + " " + tagname);
    	});
    	return false;
    });
    
    // search button key up
    $("#search-input").keyup(function() {
    	if ($(this).val() == "") {
    		$("#forms .form").show();
 	      $("#forms .form:even").css('background-color', '#ddd');
 	      $("#forms .form:odd").css('background-color', '#eee');
    	}
    	else {
    		var tag = $(this).val();
    		$('#forms .form').hide();
    		$('#forms .form[tags~="' + tag + '"]').show();
 	      $('#forms .form[tags~="' + tag + '"]:even').css('background-color', '#ddd');
 	      $('#forms .form[tags~="' + tag + '"]:odd').css('background-color', '#eee');
    	}
    });
    
    // Parse page button
    $("#parse-button").click(function() {
    	// Submit a form to the page parser
    	var url = $("#parsepageinput").val();
    	$("#parseresults").html("Parsing web page...");
    	$.get("pageparser", { page: url}, function(data) {
    		$("#parseresults").html(data);
    		repopulateForms();
    	});
    	return false;
    });
    
    // checkbox - adding to checked form list
    $(".form-checkbox").live("change", function() {
    	var localid = $(this).closest(".form").attr("localid");
    	var form = currentforms[localid];
    	
    	if ($(this).is(":checked") == true) {
        $('#selectedforms .result[formid="' + form.key + '"]').remove();
       	var output = "";
       	output += '<div class="result" formid="' + form.key + '" localid="' + localid + '">\n';
        output += '  <div class="result-title">' + parseURL(form.url).host + '</div>\n';
        output += '  <div class="result-fields">\n';
        $.each(form.fields, function(index, field) {
        	var name = field.attributes[0].value;
          output += '    <div class="result-field"><div class="result-field-name">' + name + '</div>\n';
          output += '    <div class="result-field-input">\n';
          output += '    <input type="' + field.type + '" />\n';
          output += '    </div></div>\n';
        });
        output += '  </div><br clear="both" />\n';
        output += '</div>\n';
        $("#selectedforms").append(output);
        $(".result:even").css('background-color', '#ddd');
        $(".result:odd").css('background-color', '#eee');
    	} else {
    		$('#selectedforms .result[formid="' + form.key + '"]').remove();
    		$(".result:even").css('background-color', '#ddd');
        $(".result:odd").css('background-color', '#eee');
    	}
    });
    
    // Query command
    $("#submitforms").click(function () {
    	var forms = [];
    	$('.result').each(function() {
    		var formkey = $(this).attr('formid');
    		var inputs = [];
    		var localid = $(this).attr('localid');
    		var action = currentforms[localid].attributes[1].value;
    		var method = currentforms[localid].attributes[2].value;
    		var url = currentforms[localid].url;
    		var params = "";
    		$('.result-field').each(function() {
    			var name = $(this).children('.result-field-name').text();
    			var value = $(this).find('input').val();
    			inputs.push({name: name, value: value});
    			
    			params += name + '=' + value + '&';
    		});
    		var form = {method: method, action: action, url: url, params: params, formkey: formkey};
    	  alert(JSON.stringify(form));
    		
    		$("#parseresults").html("Querying a form " + url + "...");
	      $.post('fillforms', {input: JSON.stringify(form)}, function(data) {
	    	  if (data.status == "true") {
	    		  $("#parseresults").html(data.response.value);
	    	  } else {
	    		  $("#parseresults").html(data.message);
	    	  }
	        
	        //alert(data.response.value);
	      }, "json");
    	});
    	
    	//forms.push({method: method, action: action, url: url, fields: params});

    	return false;
    });
    
    // Clear forms command
    $("#clearforms").click(function() {
    	$("#selectedforms").html("");
    	$(".form-checkbox").removeAttr("checked");
    	return false;
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
  <div id="body">
	  <div id="forms"></div>
	  <div id="selectedformactions">
      <a href="#" id="submitforms">Query</a>
      <a href="#" id="clearforms">Clear</a>
    </div>
    <div id="selectedforms-container">
	    <div id="selectedforms"></div>
	  </div>
    <br clear="all" />
    <div id="pageparser">
      <h2>Parse a web page</h2>
      <table>
        <tr>
          <td>URL:</td>
          <td><input type="text" name="page" id="parsepageinput"/></td>
          <td><a id="parse-button" href="#">Parse</a></td>
        </tr>
      </table>
      <br />
      <h2>Server response</h2>
      <div id="parseresults"></div>
    </div>
  </div>
  <div id="footer"></div>
</body>
</html>