package automaticformannotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import automaticformannotator.data.PMF;
import automaticformannotator.form.Attribute;
import automaticformannotator.form.Field;
import automaticformannotator.form.Form;

/**
 * This servlet processes a web page passed as a parameter to it. It looks
 * for forms and stores what it finds in the datastore. 
 * 
 * @author Josh Armstrong
 *
 */
@SuppressWarnings("serial")
public class PageParserServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(PageParserServlet.class.getName());
	
	/**
	 * 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String page = req.getParameter("page");
		
		log.info("Trying to parse '" + page + "'");

		try {
			// Have Jsoup try to retrieve and parse the page
			Document doc = Jsoup.connect(page).get();
			// Select all the form elements that have an action and id defined
			Elements formElements = doc.select("form[action~=.+]");
			// forms is the list of forms that we create
			ArrayList<Form> forms = new ArrayList<Form>();
			
			for (Element formElement : formElements) {
				Attribute idAttr = new Attribute();
				idAttr.setName("id");
				idAttr.setValue(formElement.id());
				
				Attribute actionAttr = new Attribute();
				actionAttr.setName("action");
				actionAttr.setValue(formElement.attr("action"));
				
				Attribute methodAttr = new Attribute();
				methodAttr.setName("method");
				if (formElement.attr("method") == null) {
					methodAttr.setValue("GET");
				} else {
					methodAttr.setValue(formElement.attr("method"));
				}
				
				Form form = new Form();
				form.setUrl(page);
				form.addAttributes(idAttr);
				form.addAttributes(actionAttr);
				form.addAttributes(methodAttr);
				
				// Pull out all the relevant inputs. This select might need to be changed
				Elements inputs = formElement.select("input[type~=.+][name~=.+]:not([type=submit]):not([type=hidden])");
				
				for (Element input : inputs) {
					Attribute nameAttr = new Attribute();
					nameAttr.setName("name");
					nameAttr.setValue(input.attr("name"));
					
					Field inputField = new Field();
					inputField.setType(input.attr("type"));
					inputField.addAttributes(nameAttr);
					
					form.addFields(inputField);
				}
				
				forms.add(form);
			}
			
			// At this point 'form' can be saved to disk.
			log.info("Successfully parsed the form. Now saving to datastore.");
			// Get the persistence manager from the static persistence manager factory
			PersistenceManager pm = PMF.get().getPersistenceManager();

	        try {
	        	for (Form form : forms) {
	        		pm.makePersistent(form);
	        	}
	        } 
	        catch (Exception e) {
	        	log.warning("Could not save form to datastore. " + e.getMessage());
	        }
	        finally {
	            pm.close();
	        }
		}
		catch (IOException e) {
			log.warning("Could not connect to " + page);
		}
		catch (IllegalArgumentException e)  {
			log.warning("JSoup could not parse the string '" + page + "'");
		}
		
		String destination = "/";
		 
		RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			log.warning("Could not forward. " + e.getMessage());
		}
	}
}
