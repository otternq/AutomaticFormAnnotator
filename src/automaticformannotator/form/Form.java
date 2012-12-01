package automaticformannotator.form;

import automaticformannotator.util.Tag;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
	private List<Field> fields;
	
	@Persistent
	private List<Attribute> attributes;
	
	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Tag tag) {
		this.tags.add(tag);;
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	@Persistent
	private List<Tag> tags;
	
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
	public List<Field> getFields() {
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
	public List<Attribute> getAttributes() {
		return this.attributes;
	}
	
	/**
	 * @param attribute the attribute to add
	 */
	public void addAttributes(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	
}
