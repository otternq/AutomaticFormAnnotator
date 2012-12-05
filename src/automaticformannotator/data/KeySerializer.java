/**
 * 
 */
package automaticformannotator.data;

import java.lang.reflect.Type;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Josh
 *
 */
public class KeySerializer implements JsonSerializer<Key> {

	@Override
	public JsonElement serialize(Key src, Type typeOfSrc,
			JsonSerializationContext context) {
		String keystring = KeyFactory.keyToString(src);
		return new JsonPrimitive(keystring);
	}

}
