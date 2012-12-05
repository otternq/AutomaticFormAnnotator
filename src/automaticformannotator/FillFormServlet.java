package automaticformannotator;

/**
 * TODO Need to look at converting to jsoup
 */

import automaticformannotator.form.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;

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

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Gson gson = new Gson();
		
		String formDataJsonString = req.getParameter("input");
		Request formData = gson.fromJson(formDataJsonString, Request.class);
		
		try {
			
			
			String url = "";
			
			if (formData.action.contains("http")) {
				url = formData.action;
			} else {
				url = formData.url + formData.action;
			}
			
			resp.getWriter().print(
					gson.toJson(
							FillFormServlet.getResponse(url, formData.method, formData.params, KeyFactory.stringToKey(formData.formkey))
					)
			);
			
		} catch (JDOUnsupportedOptionException e) {
			System.out.print("JDO is not supported");
		} catch (SocketTimeoutException e) {
			resp.getWriter().println("{'status': 'false', 'message': '"+e.getMessage()+"'}");
		} catch (ProtocolException e) {
			resp.getWriter().println("{'status': 'false', 'message': '"+e.getMessage()+"'}");
		} finally {
			//q.cancelAll();
		}
		
	}//END function doGet
	
	/**
	 * Takes a URL, Method, and Paramaters(as a string) and sends an HTTP request to a remote server
	 * @return the HTML content from the remote server
	 * @throws IOException 
	 */
	public static Response getResponse(String url, String method, String params, Key formKey) throws IOException {
		
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setDoOutput(true);
		con.setRequestMethod(method);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream ());
		//specify the parameter
		wr.writeBytes(params);
		wr.flush();
		wr.close();
		
		//retrieve the response
		InputStream response = con.getInputStream();
		
		
		//prepare response for storage in datastore
		String resString = FillFormServlet.slurp(response);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Response res = new Response();
		res.setResponse(resString);
		res.setFormKey(formKey);
		res.setParams(params);
		
		pm.makePersistent(res);
		
		con.disconnect();
		
		return res;
	}
	
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
