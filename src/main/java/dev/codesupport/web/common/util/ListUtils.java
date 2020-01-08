package dev.codesupport.web.common.util;

import java.util.List;

public class ListUtils {

    private ListUtils() {

    }

    public static boolean isEmpty(List<?> objectList) {
        return objectList == null || objectList.isEmpty();
    }

}
