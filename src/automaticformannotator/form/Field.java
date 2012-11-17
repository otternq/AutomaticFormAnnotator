package automaticformannotator.form;

import java.util.ArrayList;
import java.util.Collection;

public class Field {

	/**
	 * The type of Field being stored
	 */
	private String type;
	
	/**
	 * The attributes describing the Field
	 */
	private Collection<Attribute> attributes;
	
	public Field() {
		
		this.attributes = new ArrayList<Attribute>();
		
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
