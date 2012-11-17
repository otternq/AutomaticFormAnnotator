package automaticformannotator.form;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a HTML form
 * 
 * @author Nick Otter <otternq@gmail.com>
 *
 */
public class Form {

	private String url;
	private Collection<Field> fields;
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
