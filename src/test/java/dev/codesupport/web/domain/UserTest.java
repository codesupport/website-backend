package dev.codesupport.web.domain;

import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void shouldReturnCorrectValidationIssuesForMissingParameters() {
        User user = new User();

        List<ValidationIssue> validationIssues = user.validate();

        List<String> expected = new ArrayList<>();
        expected.add("ValidationIssue(id=null, type=MISSING, propertyName=username, message=Missing parameter)");
        expected.add("ValidationIssue(id=null, type=MISSING, propertyName=password, message=Missing parameter)");
        expected.add("ValidationIssue(id=null, type=MISSING, propertyName=email, message=Missing parameter)");
        expected.add("ValidationIssue(id=null, type=MISSING, propertyName=addedBy, message=Missing parameter)");

        List<String> actual = new ArrayList<>();

        for (ValidationIssue validationIssue : validationIssues) {
            printExpected(validationIssue.toString());
            actual.add(validationIssue.toString());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectValidationIssuesForInvalidParameters() {
        User user = UserBuilder.builder()
                .id(1L)
                .username("fred")
                .password("alpha")
                .email("booga@.com")
                .addedBy(0L)
                .avatarLink("fred.jpg")
                .joinDate(1L)
                .buildDomain();

        List<ValidationIssue> validationIssues = user.validate();

        List<String> expected = new ArrayList<>();
        expected.add("ValidationIssue(id=1, type=INVALID, propertyName=username, message=Invalid parameter (Must be 4-15 Alphanumeric figures))");
        expected.add("ValidationIssue(id=1, type=INVALID, propertyName=password, message=Invalid parameter (Must be >10 Alphanumeric figures))");
        expected.add("ValidationIssue(id=1, type=INVALID, propertyName=email, message=Invalid parameter (Must be valid email))");
        expected.add("ValidationIssue(id=1, type=MISSING, propertyName=addedBy, message=Missing parameter)");

        List<String> actual = new ArrayList<>();

        for (ValidationIssue validationIssue : validationIssues) {
            printExpected(validationIssue.toString());
            actual.add(validationIssue.toString());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNoValidationIssues() {
        User user = UserBuilder.builder()
                .id(1L)
                .username("freddy")
                .password("alphabetsoup")
                .email("booga@booga.com")
                .addedBy(2L)
                .avatarLink("fred.jpg")
                .joinDate(1L)
                .buildDomain();

        List<ValidationIssue> validationIssues = user.validate();

        assertEquals(Collections.emptyList(), validationIssues);
    }

    //This is just to make it easier to grab expected data for tests
    private void printExpected(String toString) {
        //ConstantConditions - this is a toggle
        //noinspection ConstantConditions
        if (false) {
            System.out.println("expected.add(\"" + toString + "\");");
        }
    }
}
