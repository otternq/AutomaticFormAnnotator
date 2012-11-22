package automaticformannotator;

import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.appengine.api.datastore.KeyFactory;

import automaticformannotator.data.PMF;
import automaticformannotator.form.Attribute;
import automaticformannotator.form.Field;
import automaticformannotator.form.Form;

@SuppressWarnings("serial")
public class AutomaticFormAnnotatorServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		Document doc = null;
		String page = req.getParameter("page");
		
		resp.getWriter().println("Trying to retrieve '" + page + "'");

		try {
			doc = Jsoup.connect(page).get();
			Elements formElements = doc.select("form[action~=.+][id~=.+]");
			resp.getWriter().println(formElements.size() + " forms with action and id attributes");
			
			ArrayList<Form> forms = new ArrayList<Form>();
			
			for (Element formElement : formElements) {
				resp.getWriter().println("Form with id '" + formElement.id() + "'");
				
				Attribute idAttr = new Attribute();
				idAttr.setName("id");
				idAttr.setValue(formElement.id());
				
				Attribute actionAttr = new Attribute();
				actionAttr.setName("action");
				actionAttr.setValue(formElement.attr("action"));
				
				Form form = new Form();
				form.addAttributes(idAttr);
				form.addAttributes(actionAttr);
				
				Elements inputs = formElement.select("input[type~=.+][name~=.+]:not([type=submit]):not([type=hidden])");
				resp.getWriter().println(inputs.size() + " inputs in this form:");
				
				for (Element input : inputs)
				{
					resp.getWriter().println("Input: type '" + input.attr("type") + "', name '" + input.attr("name") + "'");
					
					Attribute nameAttr = new Attribute();
					nameAttr.setName("name");
					nameAttr.setValue(input.attr("name"));
					
					Field inputField = new Field();
					inputField.setType(input.attr("type"));
					inputField.addAttributes(nameAttr);
					
					form.addFields(inputField);
				}
				
				forms.add(form);
				
				resp.getWriter().println("");
			}
			
			// At this point 'form' can be saved to disk.
			resp.getWriter().println("Successfully parsed web page");
			
			PersistenceManager pm = PMF.get().getPersistenceManager();

	        try 
	        {
	        	for (Form form : forms)
	        	{
	        		pm.makePersistent(form);
	        	}
	        } 
	        finally 
	        {
	            pm.close();
	        }
		}
		catch (IOException e) 
		{
			resp.getWriter().println("Could not connect to " + page);
		}
		catch (IllegalArgumentException e)
		{
			resp.getWriter().println("JSoup could not parse the string '" + page + "'");
		}
	}
}
