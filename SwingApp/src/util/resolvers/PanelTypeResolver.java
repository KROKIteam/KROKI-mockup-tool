package util.resolvers;

import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.exceptions.PanelTypeParsingException;

public class PanelTypeResolver {

	public static PanelType getType(String pType)
			throws PanelTypeParsingException {
		
		if (pType == null || pType.equals("")) {
			throw new PanelTypeParsingException("No parsing type found.");
		}
		if (pType.equals("standard_panel")) {
			return PanelType.STANDARDPANEL;
		} else if (pType.equals("parent_child")) {
			return PanelType.PARENTCHILDPANEL;
		}  else if (pType.equals("many_to_many")) {
			return PanelType.MANYTOMANYPANEL;
		}
		throw new PanelTypeParsingException("No parsing type for: " + pType);
	}

}
