package dev.codesupport.web.common.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ListUtilsTest {

    @Test
    public void shouldReturnTrueForNullList() {
        //ConstantConditions - This is what we're testing.
        //noinspection ConstantConditions
        assertTrue(
                ListUtils.isEmpty(null)
        );
    }

    @Test
    public void shouldReturnTrueForEmptyList() {
        assertTrue(
                ListUtils.isEmpty(new ArrayList<>())
        );
    }

    @Test
    public void shouldReturnFalseForNonEmptyList() {
        List<String> myList = new ArrayList<>(1);
        myList.add("test");

        assertFalse(
                ListUtils.isEmpty(myList)
        );
    }

}
