/**
 * 
 */
package automaticformannotator.util;

import java.util.List;


/**
 * @author Josh
 *
 */
public class TagHelper {
	/*
	 * Returns a tag with the specified value form the list of tags
	 */
	static public Tag getTagByValue(String tag, List<Tag> list) throws Exception {
		
		for (Tag a : list) {
			if (a.getValue().compareTo(tag) == 0) {
				return a;
			}
		}
		
		throw new Exception("TagNotFound");
	}
}
