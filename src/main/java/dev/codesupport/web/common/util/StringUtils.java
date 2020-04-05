package dev.codesupport.web.common.util;

import lombok.NonNull;

import java.util.StringJoiner;

/**
 * Utilities for common String manipulation logic
 */
public class StringUtils {

    private StringUtils() {

    }

    /**
     * Joins {@link CharSequence}s together to a delimited string.
     * <p>Null values are ignored and not added to the string</p>
     *
     * @param delimiter The delimiter to use for concatenation
     * @param elements  The elements to concatenate
     * @return The delimited string, nix the null values.  If all values null, returns an empty string.
     */
    public static String joinNonNull(@NonNull CharSequence delimiter, CharSequence... elements) {
        StringJoiner joiner = new StringJoiner(delimiter);

        for (CharSequence cs : elements) {
            if (cs != null) {
                joiner.add(cs);
            }
        }

        return joiner.toString();
    }

}
