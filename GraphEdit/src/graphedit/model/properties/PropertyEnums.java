package graphedit.model.properties;

public interface PropertyEnums {

	public enum WorkspaceProperties implements PropertyEnums {
		NAME
	}
	
	public enum ProjectProperties implements PropertyEnums {
		NAME
	}
	
	public enum PackageProperties implements PropertyEnums {
		NAME
	}
	
	public enum DiagramProperties implements PropertyEnums {
		NAME
	}
	
	/**
	 * Point2D position;
	 * double angle;
	 * Dimension2D size;
	 * String descr;
	 * String name;
	 * String stereotype
	 * List<Attributes> attributes
	 * List<Methods> methods
	 */
	public enum GraphElementProperties implements PropertyEnums {
		NAME, POSITION, ANGLE, SIZE, DESCRIPTION, COLOR, STEREOTYPE, ATTRIBUTES, METHODS
	}


	/**
	 * Point2D position;
	 *
	 */
	
	public enum LinkNodeProperties implements PropertyEnums{
		POSITION
	}
	
	public enum LinkProperties implements PropertyEnums {
		NAME,SOURCE_CARDINALITY, DESTINATION_CARDINALITY, SOURCE_ROLE, DESTINATION_ROLE,STEREOTYPE, SOURCE_NAVIGABLE,DESTINATION_NAVIGABLE
	}
	
}
