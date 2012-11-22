package automaticformannotator;

import java.io.IOException;
import javax.servlet.http.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class AutomaticFormAnnotatorServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world!");
		
		Document doc = null;
		String page = "http://www.w3schools.com/html/html_forms.asp"; 
		if (req.getParameter("page") != "")
		{
			page = req.getParameter("page");
		}
		resp.getWriter().println(req.getParameter("page"));
		
		try {
			doc = Jsoup.connect(page).get();
			Elements forms = doc.select("form[action~=.+][id~=.+]");
			resp.getWriter().println(forms.size() + " forms with action and id attributes");
			
			for (Element element : forms) {
				resp.getWriter().println("Form with id '" + element.id() + "'");
				Elements inputs = element.select("input[type~=.+][name~=.+]:not([type=submit]):not([type=hidden])");
				resp.getWriter().println(inputs.size() + " inputs in this form:");
				for (Element input : inputs)
				{
					resp.getWriter().println("Input: type '" + input.attr("type") + "', name '" + input.attr("name") + "'");
				}
				resp.getWriter().println("");
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			resp.getWriter().println("Could not connect to " + page);
		}
		catch (IllegalArgumentException e)
		{
			resp.getWriter().println("Illegal argument");
		}
	}
}
