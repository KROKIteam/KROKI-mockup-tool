package adapt.util.resolvers;

import adapt.core.AppCache;
import adapt.util.xml_readers.TypeComponenMappingReader;

public class ComponentTypeResolver {

	/**
	 * Gets UI component template path for specified language data type name.
	 * This class tries to read preferred template for given data type if it is available,
	 * otherwise it falls back to simple text field template.
	 */
	public static String getTemplate(String typeName) {
		String componentId = AppCache.getInstance().getComponentForType(typeName);
		String template = TypeComponenMappingReader.loadComponent(componentId);
		if(template != null) {
			return template;
		}else {
			return TypeComponenMappingReader.loadComponent("textField");
		}
	}

}
