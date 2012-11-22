package automaticformannotator.form;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Response {

	/**
	 * The key for Google App Engine DataStore
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	private Key formKey;
	
	private String response;
	
	private List<String> keywords;

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
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
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
