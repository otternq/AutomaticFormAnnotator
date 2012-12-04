/**
 * 
 */
package automaticformannotator;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import automaticformannotator.data.KeySerializer;
import automaticformannotator.data.PMF;
import automaticformannotator.form.Form;

/**
 * Returns a JSON object of all the forms in the database. 
 * 
 * @author Josh
 *
 */
@SuppressWarnings("serial")
public class FormRetrievalServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from automaticformannotator.form.Form");
		
	    List<Form> results;
	    try {
	      results = (List<Form>) q.execute();
	      // Call the retrieve all to pull in the field values for use
	      pm.retrieveAll(results);
	      // Write the json string
	      Gson gson = new GsonBuilder()
	              .registerTypeAdapter(Key.class,  new KeySerializer())
	    		  .create();
	      String jsonoutput = gson.toJson(results);
	      resp.getWriter().println(jsonoutput);
	    } finally {
	    	pm.close();
	    }
	}
}
