package automaticformannotator;

import automaticformannotator.form.*;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import automaticformannotator.data.PMF;
import automaticformannotator.form.AttributeHelper;
import automaticformannotator.form.Form;

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
						String action = AttributeHelper.getAttributeByName("action", p.getAttributes()).getValue();
						String method = AttributeHelper.getAttributeByName("method", p.getAttributes()).getValue();
					} catch (Exception e) {//an attribute is not specified
						
					}
					
				}
				
			} else {// Handle "no results" case
				
			}
			
		} finally {
			q.cancelAll();
		}
		
	}
	
}
