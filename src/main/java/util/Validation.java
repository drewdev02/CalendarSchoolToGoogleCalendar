package util;

public class Validation {
    public static void validateNotNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateNotBlank(String str, String errorMessage) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateIndexInRange(int index, int size, String errorMessage) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(errorMessage);
        }
    }
}

