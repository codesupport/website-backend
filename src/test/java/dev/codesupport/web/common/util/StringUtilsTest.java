package dev.codesupport.web.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfDelimiterNull() {
        StringUtils.joinNonNull(null, "1", "2");
    }

    @Test
    public void shouldJoinNonNullStrings() {
        String expected = "1.2";
        String actual = StringUtils.joinNonNull(".", "1", "2");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotJoinNullPrefixString() {
        String expected = "2";
        String actual = StringUtils.joinNonNull(".", null, "2");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotJoinNullSuffixString() {
        String expected = "1";
        String actual = StringUtils.joinNonNull(".", "1", null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyStringForNullStrings() {
        String expected = "";
        String actual = StringUtils.joinNonNull(".", null, null);

        assertEquals(expected, actual);
    }

}
