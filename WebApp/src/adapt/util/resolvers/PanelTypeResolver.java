package adapt.util.resolvers;

import adapt.enumerations.PanelType;
import adapt.exceptions.PanelTypeParsingException;
import adapt.util.staticnames.Tags;

public class PanelTypeResolver {

	public static PanelType getType(String typeName) throws PanelTypeParsingException {
		
		if(typeName == null || typeName.equals("")) {
			throw new PanelTypeParsingException("No parsing type found.");
		}
		if(typeName.equals(Tags.STANDARD_PANEL) || typeName.equals("STANDARDPANEL")) {
			return PanelType.STANDARDPANEL;
		}else if(typeName.equals(Tags.PARENT_CHILD) || typeName.equals("PARENTCHILDPANEL")) {
			return PanelType.PARENTCHILDPANEL;
		}else if(typeName.equals(Tags.MANY_TO_MANY) || typeName.equals("MANYTOMANYPANEL")) {
			return PanelType.MANYTOMANYPANEL;
		}
		throw new PanelTypeParsingException("No parsing type for name: " + typeName);
	}
	
	public String reverseTypeName(PanelType type) {
		String panelType = null;
		switch (type) {
		case STANDARDPANEL:
			panelType = Tags.STANDARD_PANEL;
			break;
		case PARENTCHILDPANEL:
			panelType = Tags.PARENT_CHILD;
			break;
		case MANYTOMANYPANEL:
			panelType = Tags.MANY_TO_MANY;
			break;
		default:
			break;
		}
		return panelType;
	}
}
