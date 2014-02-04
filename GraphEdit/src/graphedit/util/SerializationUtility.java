package graphedit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Provides a single utility method. Makes use in clipboard
 * management, particularly Cut/Copy actions.
 * @author specijalac
 */
public class SerializationUtility {

	/**
	 * Method obtains deep copies for provided instances.
	 * @param obj1 represents the first object which is to be streamed
	 * @param obj2 represents the second object which is to be streamed
	 * @return an array of deserialized, objects representing deep copies
	 * of provided objects <code>obj1</code> and </code>obj2</code>
	 * @author specijalac
	 */
	public static Object[] deepCopy(Object obj1, Object obj2) {
		Object obj[] = {null, null};
		try {
			// must be the same byte stream (link and element painter)
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(obj2);
			out.writeObject(obj1);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			obj[0] = in.readObject();
			obj[1] = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

}
