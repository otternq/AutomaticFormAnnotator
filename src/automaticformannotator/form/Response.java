package automaticformannotator.form;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Response {

	/**
	 * The key for Google App Engine DataStore
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	private Key formKey;
	
	private Date date;
	
	private Text response;
	
	private List<String> keywords;
	
	public Boolean Status;
	
	public Response() {
		this.date = new Date();
		this.Status = true;
	}

	/**
	 * @return the formKey
	 */
	public Key getFormKey() {
		return formKey;
	}

	/**
	 * @param formKey the formKey to set
	 */
	public void setFormKey(Key formKey) {
		this.formKey = formKey;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response.toString();
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = new Text(response);
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void addKeyword(String keywords) {
		this.keywords.add(keywords);
	}
	
}
