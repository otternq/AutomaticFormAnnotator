package automaticformannotator.form;

import java.util.ArrayList;
import java.util.Collection;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents HTML Form Fields
 * @author Nick Otter <otternq@gmail.com>
 *
 */
@PersistenceCapable
public class Field {
	
	/**
	 * The key for Google App Engine DataStore
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	/**
	 * The type of Field being stored
	 */
	@Persistent
	private String type;
	
	/**
	 * The attributes describing the Field
	 */
	@Persistent
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
