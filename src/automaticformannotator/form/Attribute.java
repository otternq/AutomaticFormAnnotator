package automaticformannotator.form;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents attributes of HTML elements
 * 
 * @author Nick Otter <otternq@gmail.com>
 *
 */

@PersistenceCapable
public class Attribute {
	
	/**
	 * The key for Google App Engine DataStore
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	/**
	 * The attribute being stored
	 */
	@Persistent
	private String name;
	
	/**
	 * The value of the attribute being stored
	 */
	@Persistent
	private String value;

	/**
	 * @return the name
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to store
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
