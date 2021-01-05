package dev.codesupport.web.common.util;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

//S5976 - ParameterizedTests - Not doing that right now.
@SuppressWarnings({"java:S5976"})
public class NumberUtilsTest {

    @Test
    public void shouldCorrectlyParseValidLong() {
        Optional<Long> expected = Optional.of(10L);

        Optional<Long> actual = NumberUtils.parseLong(expected.get().toString());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseValidNegativeLong() {
        Optional<Long> expected = Optional.of(-10L);

        Optional<Long> actual = NumberUtils.parseLong(expected.get().toString());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseEmptyLong() {
        Optional<Long> expected = Optional.empty();

        Optional<Long> actual = NumberUtils.parseLong("");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseNullLong() {
        Optional<Long> expected = Optional.empty();

        Optional<Long> actual = NumberUtils.parseLong(null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseInvalidLong() {
        Optional<Long> expected = Optional.empty();

        Optional<Long> actual = NumberUtils.parseLong("apple");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseTooLargeLong() {
        Optional<Long> expected = Optional.empty();

        Optional<Long> actual = NumberUtils.parseLong("9223372036854775808");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseTooSmallLong() {
        Optional<Long> expected = Optional.empty();

        Optional<Long> actual = NumberUtils.parseLong("-9223372036854775809");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseValidInteger() {
        Optional<Integer> expected = Optional.of(10);

        Optional<Integer> actual = NumberUtils.parseInt(expected.get().toString());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseValidNegativeInteger() {
        Optional<Integer> expected = Optional.of(-10);

        Optional<Integer> actual = NumberUtils.parseInt(expected.get().toString());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseNullInteger() {
        Optional<Integer> expected = Optional.empty();

        Optional<Integer> actual = NumberUtils.parseInt(null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseEmptyInteger() {
        Optional<Integer> expected = Optional.empty();

        Optional<Integer> actual = NumberUtils.parseInt("");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseInvalidInteger() {
        Optional<Integer> expected = Optional.empty();

        Optional<Integer> actual = NumberUtils.parseInt("apple");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseTooLargeInteger() {
        Optional<Integer> expected = Optional.empty();

        Optional<Integer> actual = NumberUtils.parseInt("2147483648");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseTooSmallInteger() {
        Optional<Integer> expected = Optional.empty();

        Optional<Integer> actual = NumberUtils.parseInt("-2147483649");

        assertEquals(expected, actual);
    }

}
