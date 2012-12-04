/**
 * 
 */
package automaticformannotator;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import automaticformannotator.data.PMF;
import automaticformannotator.form.Form;
import automaticformannotator.util.Tag;
import automaticformannotator.util.TagHelper;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
/**
 * @author Josh
 *
 */
public class ModifyTagsServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String actionString = req.getParameter("action");
		String formKeystring = req.getParameter("formkey");
		String tagName = req.getParameter("tag");
		Key key = KeyFactory.stringToKey(formKeystring);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//resp.getWriter().println("Successful!");
		try {
			Form form = (Form) pm.getObjectById(Form.class, key);
			//resp.getWriter().println(form.getUrl());
			if (actionString.equals("delete"))
			{
				try {
					Tag tag = TagHelper.getTagByValue(tagName, form.getTags());
					
					form.getTags().remove(tag);
					resp.getWriter().println("Tag deleted");
				} catch (Exception e) {
					if (e.getMessage().equals("TagNotFound")) {
						// the tag wasn't found. 
						resp.getWriter().println("Tag not found. " + tagName);
					} else {
						throw e;
					}
				}
			}
			else if (actionString.equals("add"))
			{
				Tag tag = new Tag();
				tag.setValue(tagName);
				form.addTag(tag);
				resp.getWriter().println("Tag added");
			}
			else
			{
				resp.getWriter().println("Action not supported");
			}
		} catch (Exception e) {
			resp.getWriter().println("An error occured retrieving the form. "  + e.toString());
		} finally {
			pm.close();
		}
	}
}
