package automaticformannotator.form;

/**
 * Represents attributes of HTML elements
 * 
 * @author Nick Otter <otternq@gmail.com>
 *
 */
public class Attribute {

	/**
	 * The attribute being stored
	 */
	private String name;
	
	/**
	 * The value of the attribute being stored
	 */
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
