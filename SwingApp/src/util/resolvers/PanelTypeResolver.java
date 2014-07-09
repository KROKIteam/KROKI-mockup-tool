package util.resolvers;

import util.staticnames.Tags;

import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.exceptions.PanelTypeParsingException;

public class PanelTypeResolver {

	public static PanelType getType(String pType)
			throws PanelTypeParsingException {
		
		if (pType == null || pType.equals("")) {
			throw new PanelTypeParsingException("No parsing type found.");
		}
		if (pType.equals(Tags.STANDARD_PANEL)) {
			return PanelType.STANDARDPANEL;
		} else if (pType.equals(Tags.PARENT_CHILD)) {
			return PanelType.PARENTCHILDPANEL;
		}  else if (pType.equals(Tags.MANY_TO_MANY)) {
			return PanelType.MANYTOMANYPANEL;
		}
		throw new PanelTypeParsingException("No parsing type for: " + pType);
	}

}
