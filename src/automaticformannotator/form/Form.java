package automaticformannotator.form;

import java.util.ArrayList;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Collection;

/**
 * Represents a HTML form
 * 
 * @author Nick Otter <otternq@gmail.com>
 *
 */
@PersistenceCapable
public class Form {
	
	/**
	 * The key for Google App Engine DataStore
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	@Persistent
	private String url;
	
	@Persistent
	private Collection<Field> fields;
	
	@Persistent
	private Collection<Attribute> attributes;
	
	public Form() {
		this.fields = new ArrayList<Field>();
		this.attributes = new ArrayList<Attribute>();
	}
	
	/**
	 * @return the url where this form was located
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * @param url the url where this form was located
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the field to store
	 */
	public Collection<Field> getFields() {
		return this.fields;
	}
	
	/**
	 * @param fields the fields to set
	 */
	public void addFields(Field field) {
		this.fields.add(field);
	}
	
	/**
	 * @return the attributes
	 */
	public Collection<Attribute> getAttributes() {
		return this.attributes;
	}
	
	/**
	 * @param attribute the attribute to add
	 */
	public void addAttributes(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	
}
