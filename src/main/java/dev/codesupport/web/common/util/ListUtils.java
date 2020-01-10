package dev.codesupport.web.common.util;

import java.util.List;

/**
 * Utilities for List based common logic
 */
public class ListUtils {

    private ListUtils() {

    }

    /**
     * Nullsafe way to check if a list is empty.
     *
     * @param objectList The list to check
     * @return True if the list is null or empty, False otherwise.
     */
    public static boolean isEmpty(List<?> objectList) {
        return objectList == null || objectList.isEmpty();
    }

}
