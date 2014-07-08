package bp.model.data;

public enum ExecutionType {

    PARALLEL("parallel"), SEQUENTIAL("sequential");

    private String name;

    private ExecutionType(String name) {
        this.name = name;
    }
    
    private ExecutionType() { }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
		this.name = name;
	}

	public static ExecutionType getEnumValue(String value) {
        if (value == null)
            return null;

        String s = value.toLowerCase();
        for (ExecutionType et : ExecutionType.values()) {
            if (s.equals(et.getName()))
                return et;
        }

        return null;
    }
}
