package automaticformannotator;

/**
 * TODO Need to look at converting to jsoup
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
import java.util.logging.Logger;

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Text;

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
	private static final Logger log = Logger.getLogger(PageParserServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query q = pm.newQuery(Form.class);
		
		try {
			
			List<Form> results = (List<Form>) q.execute();
			
			if (!results.isEmpty()) {
				
				resp.getWriter().println("There are results");
				
				for (Form p : results) {// Process result p
					log.info("Starting Form");
					
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
							log.info("handling fields");
							
							//get the fields attributes so that they can be searched
							List<Attribute> fieldAttributes = f.getAttributes();
							
							String fieldName = "";
							String fieldValue = "";
							
							try {
								//get the field name and default value
								fieldName = AttributeHelper.getAttributeByName("name", fieldAttributes).getValue();
								fieldValue = AttributeHelper.getAttributeByName("value", fieldAttributes).getValue();
								
								//add these to the parameters list
								params.add(fieldName + "=" + URLEncoder.encode(fieldValue, "UTF-8"));
							} catch (Exception ae) {
								if (ae.getMessage().compareTo("AttributeNotFound") == 0) {
									log.info("could not find attribute");
								}
							}
							
							
						}
						
						String temp[] = new String[8];
						
						String requestBody = FillFormServlet.implodeArray(params.toArray(temp), "&");
						resp.getWriter().println(p.getUrl() + action);
						HttpURLConnection con = (HttpURLConnection) new URL(p.getUrl() + action).openConnection();
						con.setDoOutput(true);
						con.setRequestMethod(method);
						
						DataOutputStream wr = new DataOutputStream(con.getOutputStream ());
						//specify the parameter
						wr.writeBytes(requestBody);
						wr.flush();
						wr.close();
						
						//retrieve the response
						InputStream response = con.getInputStream();
						
						
						//prepare response for storage in datastore
						String resString = this.slurp(response);
						
						
						Response res = new Response();
						res.setResponse(resString);
						res.setFormKey(p.getKey());
						
						//store the response
						pm.makePersistent(res);
						
						con.disconnect();
						
					} catch (Exception e) {//an attribute is not specified
						resp.getWriter().println("There was an exception "+ e.getMessage() + " ");
						e.printStackTrace();
					}
					
				}
				
			} else {// Handle "no results" case
				
			}
			
		} catch (JDOUnsupportedOptionException e) {
			System.out.print("JDO is not supported");
			
		} finally {
			//q.cancelAll();
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
	
	/**
	 * Converts an InputStream to a string
	 * @param in The InputStream to convert
	 * @return string
	 * @throws IOException
	 */
	public static String slurp (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
	
}
