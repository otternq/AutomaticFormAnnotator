package automaticformannotator;

/**
 * @todo Need to look at converting to jsoup
 */

import automaticformannotator.form.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import automaticformannotator.data.PMF;
import automaticformannotator.form.AttributeHelper;
import automaticformannotator.form.Form;
/**
 * 
 * @author Nick Otter <otternq@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class FillFormServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query q = pm.newQuery(Form.class);
		
		try {
			
			List<Form> results = (List<Form>) q.execute();
			
			if (!results.isEmpty()) {
				
				resp.getWriter().println("There are results");
				
				for (Form p : results) {// Process result p
					resp.getWriter().println("Start Form");
					
					try {
						//get the form attributes so that they can be searched
						List<Attribute> formAttributes = p.getAttributes();
						
						//save the action and method attributes
						String action = AttributeHelper.getAttributeByName("action", formAttributes).getValue();
						String method = AttributeHelper.getAttributeByName("method", formAttributes).getValue();
						
						//a list of parameters to be submitted
						List<String> params = new ArrayList<String>();
						
						//add the default values for all form fields
						for ( Field f : p.getFields() ) {
							//get the fields attributes so that they can be searched
							List<Attribute> fieldAttributes = f.getAttributes();
							
							//get the field name and default value
							String fieldName = AttributeHelper.getAttributeByName("name", fieldAttributes).getValue();
							String fieldValue = AttributeHelper.getAttributeByName("value", fieldAttributes).getValue();
							
							//add these to the parameters list
							params.add(fieldName + "=" + URLEncoder.encode(fieldValue, "UTF-8"));
						}
						
						String requestBody = FillFormServlet.implodeArray((String[])params.toArray(), "&");
						
						HttpURLConnection con = (HttpURLConnection) new URL(action).openConnection();
						con.setRequestMethod(method);
						
						DataOutputStream wr = new DataOutputStream(con.getOutputStream ());
						//specify the parameter
						wr.writeBytes(requestBody);
						wr.flush();
						wr.close();
						
						InputStream response = con.getInputStream();
						con.disconnect();
						
					} catch (Exception e) {//an attribute is not specified
						
					}
					
				}
				
			} else {// Handle "no results" case
				
			}
			
		} finally {
			q.cancelAll();
		}
		
	}//END function doGet
	
	/**
	* Method to join array elements of type string
	* 
	* Copied from http://imwill.com/implode-string-array-java/#.UK6YNeOe-X8
	* 
	* @author Hendrik Will, imwill.com
	* @param inputArray Array which contains strings
	* @param glueString String between each array element
	* @return String containing all array elements seperated by glue string
	*/
	public static String implodeArray(String[] inputArray, String glueString) {

		/** Output variable */
		String output = "";
	
		if (inputArray.length > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputArray[0]);
	
			for (int i=1; i<inputArray.length; i++) {
				sb.append(glueString);
				sb.append(inputArray[i]);
			}
	
			output = sb.toString();
		}
	
		return output;
	}
	
}
