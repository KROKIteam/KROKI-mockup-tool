package bp.model.util;

import java.security.InvalidParameterException;

import bp.model.data.ExecutionType;

public enum BPType {

    UNIQUE_NAME, STRING, INTEGER, BOOLEAN, EXECUTION_TYPE;

    /**
     * Generates String representation of given <code>value</code><br>
     * Type of value must match enumeration value:<br>
     * If {@link BPType} type is {@link BPType#STRING}, then value type should be {@link String}<br>
     * if {@link BPType} type is {@link BPType#UNIQUE_NAME}, then value type should be {@link String}<br>
     * If {@link BPType} type is {@link BPType#INTEGER}, then value type should be {@link Integer}<br>
     * If {@link BPType} type is {@link BPType#BOOLEAN}, then value type should be {@link Boolean}<br>
     * If {@link BPType} type is {@link BPType#EXECUTION_TYPE}, then value type should be {@link ExecutionType}<br>
     * <br>
     * If type is miss matched or value is null, {@link InvalidParameterException} will be raised
     * 
     * 
     * @param value
     *            - value, must match type, can't be null
     * @return
     */
    public String generateText(final Object value) {
        if (value == null) {
            throw new InvalidParameterException("value can't be null");
        }

        if (this == UNIQUE_NAME) {
            if (value instanceof String) {
                return (String) value;
            } else {
                throw new InvalidParameterException("value must be instance of " + String.class.toString());
            }
        }

        if (this == STRING) {
            if (value instanceof String) {
                return String.format("\"%s\"", (String) value);
            } else {
                throw new InvalidParameterException("value must be instance of " + String.class.toString());
            }
        }

        if (this == INTEGER) {
            if (value instanceof Integer) {
                return value.toString();
            } else {
                throw new InvalidParameterException("value must be instance of " + Integer.class.toString());
            }
        }

        if (this == BOOLEAN) {
            if (value instanceof Boolean) {
                return value.toString();
            } else {
                throw new InvalidParameterException("value must be instance of " + Boolean.class.toString());
            }
        }

        if (this == EXECUTION_TYPE) {
            if (value instanceof ExecutionType) {
                return ((ExecutionType) value).getName();
            } else {
                throw new InvalidParameterException("value must be instance of " + ExecutionType.class.toString());
            }
        }

        return null;
    }
}
