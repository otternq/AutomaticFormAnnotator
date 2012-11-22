package automaticformannotator;

import java.io.IOException;
import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
			Elements forms = doc.select("form[action~=.+][id~=.+]");
			resp.getWriter().println(forms.size() + " forms with action and id attributes");

			for (Element element : forms) {
				resp.getWriter().println("Form with id '" + element.id() + "'");

				
				Attribute idAttr = new Attribute();
				idAttr.setName("id");
				idAttr.setValue(element.id());
				
				Attribute actionAttr = new Attribute();
				actionAttr.setName("action");
				actionAttr.setValue(element.attr("action"));
				
				Form form = new Form();
				form.addAttributes(idAttr);
				form.addAttributes(actionAttr);
				
				Elements inputs = element.select("input[type~=.+][name~=.+]:not([type=submit]):not([type=hidden])");
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
				
				resp.getWriter().println("");
			}
			
			// At this point 'form' can be saved to disk.
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
