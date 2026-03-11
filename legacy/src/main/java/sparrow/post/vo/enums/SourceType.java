package sparrow.post.vo.enums;

public enum SourceType {
    MARKDOWN(0),
    HTML(1);

    private final int value;

    SourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SourceType fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SourceType type : SourceType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown source type: " + value);
    }
}
