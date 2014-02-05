package graphedit.model.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Properties<E extends PropertyEnums> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<E, Object> map;
	
	public Properties() {
		map = new HashMap<E, Object>();
	}
	
	public Object get(E key) {
		return map.get(key);
	}
	
	public Object set(E key, Object value) {
		return map.put(key, value);
	}
	
	public Set<Map.Entry<E, Object>> getEntrySet() {
		return map.entrySet();
	}
}
