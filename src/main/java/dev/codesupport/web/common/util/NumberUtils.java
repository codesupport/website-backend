package dev.codesupport.web.common.util;

import java.util.Optional;

public class NumberUtils {

    private NumberUtils() {

    }

    public static Optional<Long> parseLong(String value) {
        return parseLong(value, 10);
    }

    public static Optional<Long> parseLong(String value, int radix) {
        return parseNumber(value, radix, -Long.MAX_VALUE, Long.MIN_VALUE);
    }

    public static Optional<Integer> parseInt(String value) {
        return parseInt(value, 10);
    }

    public static Optional<Integer> parseInt(String value, int radix) {
        return parseNumber(value, radix, -Integer.MAX_VALUE, Integer.MIN_VALUE).map(Long::intValue);
    }

    //S3776 - Complexity - Not worrying about this right now
    @SuppressWarnings({"java:S3776"})
    private static Optional<Long> parseNumber(String value, int radix, long limitMax, long limitMin) {
        Optional<Long> resultOptional;

        if (value != null) {
            if (radix >= Character.MIN_RADIX) {
                if (radix <= Character.MAX_RADIX) {
                    long result = 0;
                    boolean negative = false;
                    int i = 0;
                    int len = value.length();
                    long limit = limitMax;
                    long multmin;
                    int digit;
                    boolean invalid = false;

                    if (len > 0) {
                        char firstChar = value.charAt(0);

                        if (firstChar < '0') { // Possible leading "+" or "-"
                            if (firstChar == '-' && len != 1) {
                                negative = true;
                                limit = limitMin;
                                i++;
                            } else if (firstChar != '+') {
                                invalid = true;
                            }
                        }

                        if (!invalid) {
                            multmin = limit / radix;

                            while (!invalid && i < len) {
                                // Accumulating negatively avoids surprises near MAX_VALUE
                                digit = Character.digit(value.charAt(i++), radix);
                                invalid = (digit < 0) || (result < multmin);
                                if (!invalid) {
                                    result *= radix;
                                    invalid = (result < limit + digit);
                                    result -= digit;
                                }
                            }

                            long finalResult = negative ? result : -result;
                            resultOptional = (invalid) ? Optional.empty() : Optional.of(finalResult);
                        } else {
                            resultOptional = Optional.empty();
                        }
                    } else {
                        resultOptional = Optional.empty();
                    }
                } else {
                    resultOptional = Optional.empty();
                }
            } else {
                resultOptional = Optional.empty();
            }
        } else {
            resultOptional = Optional.empty();
        }

        return resultOptional;
    }

}
