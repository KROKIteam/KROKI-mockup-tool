package bp.util;

public class BPNameGenerator {

    public static final String DEFAULT_TASK_NAME = "untitledTask";
    public static final String DEFAULT_GATEWAY_NAME = "untitledGateway";
    public static final String DEFAULT_EDGE_NAME = "untitledEdge";
    public static final String DEFAULT_LANE_NAME = "untitledLane";
    public static final String DEFAULT_EVENT_NAME = "untitledEvent";

    private final UniqueNameGenerator taskNameGenerator;
    private final UniqueNameGenerator gatewayNameGenerator;
    private final UniqueNameGenerator edgeNameGenerator;
    private final UniqueNameGenerator laneNameGenerator;
    private final UniqueNameGenerator eventNameGenerator;

    public BPNameGenerator() {
        this.taskNameGenerator = new UniqueNameGenerator(DEFAULT_TASK_NAME);
        this.gatewayNameGenerator = new UniqueNameGenerator(DEFAULT_GATEWAY_NAME);
        this.edgeNameGenerator = new UniqueNameGenerator(DEFAULT_EDGE_NAME);
        this.laneNameGenerator = new UniqueNameGenerator(DEFAULT_LANE_NAME);
        this.eventNameGenerator = new UniqueNameGenerator(DEFAULT_EVENT_NAME);
    }

    public String nextTaskName() {
        return this.taskNameGenerator.generateNext();
    }

    public String nextGatewayName() {
        return this.gatewayNameGenerator.generateNext();
    }

    public String nextEdgeName() {
        return this.edgeNameGenerator.generateNext();
    }

    public String nextLaneName() {
        return this.laneNameGenerator.generateNext();
    }

    public String nextEventName() {
        return this.eventNameGenerator.generateNext();
    }
}
