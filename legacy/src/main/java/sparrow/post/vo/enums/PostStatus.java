package sparrow.post.vo.enums;

public enum PostStatus {
    DRAFT(0),
    PUBLISHED(1),
    ARCHIVED(2);

    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PostStatus fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PostStatus status : PostStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + value);
    }
}
