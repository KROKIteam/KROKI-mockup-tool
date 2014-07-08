package bp.model.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public enum BPKeyWords {

    PROCESS("process", BPType.UNIQUE_NAME),
    NAME("name", BPType.STRING),
    DESCRIPTION("description", BPType.STRING),
    DATA("data", BPType.STRING),
    TASK("task", BPType.UNIQUE_NAME),
    ACTOR("actor", BPType.STRING),
    LANE_ACTOR("laneActor", BPType.UNIQUE_NAME),
    MIN_INPUT("minInput", BPType.INTEGER),
    AUTO_ASSIGN("autoAssign", BPType.BOOLEAN),
    MULTIPLE_EXECUTION("multipleExecution", BPType.INTEGER),
    MULTIPLE_EXECUTION_TYPE("multipleExecutionType", BPType.EXECUTION_TYPE),
    LOOP_EXPRESSION("loopExpression", BPType.STRING),
    USER_TASK("userTask", BPType.UNIQUE_NAME),
    IMPLEMENTATION("implementation", BPType.STRING),
    SYSTEM_TASK("systemTask", BPType.UNIQUE_NAME),
    EDGE("edge", BPType.UNIQUE_NAME),
    SOURCE("source", BPType.UNIQUE_NAME),
    TARGET("target", BPType.UNIQUE_NAME),
    CONDITIONAL_EDGE("conditionalEdge", BPType.UNIQUE_NAME),
    CONDITION("condition", BPType.STRING),
    GATEWAY("gateway", BPType.UNIQUE_NAME),
    LANE("lane", BPType.UNIQUE_NAME),
    PARENT("parent", BPType.UNIQUE_NAME),
    START("start", BPType.UNIQUE_NAME),
    SIGNAL_START("signalStart", BPType.UNIQUE_NAME),
    SIGNAL_NAME("signalName", BPType.STRING),
    DATA_FORMAT("dataFormat", BPType.STRING),
    MESSAGE_START("messageStart", BPType.UNIQUE_NAME),
    TIMER_START("timerStart", BPType.UNIQUE_NAME),
    TIME_FORMAT("timeFormat", BPType.STRING),
    CONDITIONAL_START("conditionalStart", BPType.UNIQUE_NAME),
    ERROR_START("errorStart", BPType.UNIQUE_NAME),
    ERROR_NAME("errorName", BPType.STRING),
    MULTIPLE_START("multipleStart", BPType.UNIQUE_NAME),
    END("end", BPType.UNIQUE_NAME),
    MESSAGE_END("messageEnd", BPType.UNIQUE_NAME),
    MAX_TRIGGERS("maxTriggers", BPType.INTEGER),
    MESSAGE("message", BPType.STRING),
    SIGNAL_END("signalEnd", BPType.UNIQUE_NAME),
    SIGNAL_DATA("signalData", BPType.STRING),
    ERROR_END("errorEnd", BPType.UNIQUE_NAME),
    ERROR_DATA("errorData", BPType.STRING),
    MULTIPLE_END("multipleEnd", BPType.UNIQUE_NAME),
    TIMER_CATCH_EVENT("timerCatchEvennt", BPType.UNIQUE_NAME),
    CONDITIONAL_CATCH_EVENT("conditionalCatchEvent", BPType.UNIQUE_NAME),
    MESSAGE_CATCH_EVENT("messageCatchEvent", BPType.UNIQUE_NAME),
    SIGNAL_CATCH_EVENT("signalCatchEvent", BPType.UNIQUE_NAME),
    MESSAGE_THROW_EVENT("messageThrowEvent", BPType.UNIQUE_NAME),
    SIGNAL_THROW_EVENT("signalThrowEvent", BPType.UNIQUE_NAME),
    LINK_CATCH_EVENT("linkCatchEvent", BPType.UNIQUE_NAME),
    LINK_THROW_EVENT("linkThrowEvent", BPType.UNIQUE_NAME),
    LINK("link", BPType.UNIQUE_NAME),
    MESSAGE_ACTIVITY_EVENT("messageActivityEvent", BPType.UNIQUE_NAME),
    STOP_ACTIVITY("stopActivity", BPType.BOOLEAN),
    SIGNAL_ACTIVITY_EVENT("signalActivityEvent", BPType.UNIQUE_NAME),
    TIMER_ACTIVITY_EVENT("timerActivityEvent", BPType.UNIQUE_NAME),
    CONDITIONAL_ACTIVITY_EVENT("conditionalActivityEvent", BPType.UNIQUE_NAME),
    ERROR_ACTIVITY_EVENT("errorActivityEvent", BPType.UNIQUE_NAME),
    SUBPROCESS("subProcess", BPType.UNIQUE_NAME),
    TRUE("true", null),
    FALSE("false", null),
    PARALLEL("parallel", null),
    SEQUENTIAL("sequential", null),
    UNIQUE_NAME("", null),
    ROOT("", null);


    private final String name;
    private final BPType type;

    private static final Pattern keyWordsPattern;

    static {
        final StringBuilder sb = new StringBuilder();
        for (final BPKeyWords v : BPKeyWords.values()) {
            if (!v.getName().isEmpty()) {
                sb.append(v.getName());
                sb.append("|");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        keyWordsPattern = Pattern.compile(sb.toString());
    }

    private BPKeyWords(final String name, final BPType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public BPType getType() {
        return this.type;
    }

    public static String[] getKeyWords() {
        final Set<String> keyWords = new HashSet<>();
        for (final BPKeyWords v : BPKeyWords.values()) {
            if (!v.getName().isEmpty()) {
                keyWords.add(v.getName());
            }
        }
        return keyWords.toArray(new String[0]);
    }

    public static boolean isKeyWord(final String keyWord) {
        return keyWordsPattern.matcher(keyWord).matches();
    }

}
