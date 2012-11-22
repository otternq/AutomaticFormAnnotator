package automaticformannotator.form;

import java.util.List;

public class AttributeHelper {

	/**
	 * Performs a brute force search for a specified field name
	 * 
	 * @param name The needle
	 * @param list The haystack
	 * @return The attribute
	 * @throws Exception thrown if the requested attribute is not found
	 */
	static public Attribute getAttributeByName( String name, List<Attribute> list ) throws Exception {
		
		for (Attribute a : list) {
			if (a.getName().compareTo(name) == 0) {//is this the attribute
				return a;
			}
		}
		
		throw new Exception("AttributeNotFound");
		
	}
	
}
