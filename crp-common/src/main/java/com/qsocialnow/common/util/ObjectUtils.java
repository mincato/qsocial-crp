package com.qsocialnow.common.util;

import org.apache.commons.lang3.ArrayUtils;

public class ObjectUtils {

    public static boolean containsAll(Object[] sourceArray, Object[] objectsToFind) {
        if (objectsToFind.length <= sourceArray.length) {
            for (Object objectToFind : objectsToFind) {
                if (!ArrayUtils.contains(sourceArray, objectToFind)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
