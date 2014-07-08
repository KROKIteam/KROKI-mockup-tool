package bp.util;

public class UniqueNameGenerator {

    private Integer index;
    private final String name;

    public UniqueNameGenerator(final String name) {
        this.name = name;
        this.index = 0;
    }

    public String getName() {
        return this.name;
    }

    public Integer getIndex() {
        return this.index;
    }

    public String generateNext() {
        return this.name + this.index++;
    }
}
