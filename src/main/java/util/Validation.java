package util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validation {
    private Validation() {
    }

    public static void validateNotNull(Object obj, String errorMessage) {
        if (obj == null) {
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateNotBlank(String str, String errorMessage) {
        if (str == null || str.trim().isEmpty()) {
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateIndexInRange(int index, int size, String errorMessage) {
        if (index < 0 || index >= size) {
            log.error(errorMessage);
            throw new IndexOutOfBoundsException(errorMessage);
        }
    }
}

