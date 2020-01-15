package dev.codesupport.web.common.service.http;

import dev.codesupport.web.common.exception.InternalServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ObjectToUrlEncodedConverterTest {

    @Test
    public void shouldReturnFalseForCanReadApplicationFormUrlEncoded() {
        ObjectToUrlEncodedConverter converter = new ObjectToUrlEncodedConverter();

        assertFalse(converter.canRead(String.class, MediaType.APPLICATION_FORM_URLENCODED));
    }

    @Test
    public void shouldReturnTrueForCanReadApplicationFormUrlEncoded() {
        ObjectToUrlEncodedConverter converter = new ObjectToUrlEncodedConverter();

        assertTrue(converter.canWrite(String.class, MediaType.APPLICATION_FORM_URLENCODED));
    }

    @Test(expected = NotImplementedException.class)
    public void shouldThrowNotImplementedException() {
        ObjectToUrlEncodedConverter converter = new ObjectToUrlEncodedConverter();

        HttpInputMessage mockInputMessage = mock(HttpInputMessage.class);

        converter.read(String.class, mockInputMessage);
    }

    @Data
    @AllArgsConstructor
    private static class TestObject {
        private String myString;
        private Integer myInt;
        private Boolean myBool;
    }

    @Test(expected = InternalServiceException.class)
    public void shouldThrowInternalServiceExceptionOnIOException() throws IOException {
        HttpOutputMessage mockOutputMessage = mock(HttpOutputMessage.class);
        OutputStream mockOutputStream = mock(OutputStream.class);

        doThrow(IOException.class)
                .when(mockOutputStream)
                .write(any());

        doReturn(mockOutputStream)
                .when(mockOutputMessage)
                .getBody();

        ObjectToUrlEncodedConverter converter = new ObjectToUrlEncodedConverter();

        TestObject testObject = new TestObject(null, null, null);

        converter.write(testObject, MediaType.APPLICATION_FORM_URLENCODED, mockOutputMessage);
    }

    @Test
    public void shouldWriteCorrectMessageToOutputMessage() throws IOException {
        String testString = "https://www.google.com/search?q=url&oq=url&aqs=chrome..69i57j69i60l3.1739j0j7&sourceid=chrome&ie=UTF-8";
        Integer testInt = 5;
        Boolean testBool = true;

        HttpOutputMessage mockOutputMessage = mock(HttpOutputMessage.class);
        OutputStream mockOutputStream = mock(OutputStream.class);

        doReturn(mockOutputStream)
                .when(mockOutputMessage)
                .getBody();

        ObjectToUrlEncodedConverter converter = new ObjectToUrlEncodedConverter();

        TestObject testObject = new TestObject(testString, testInt, testBool);

        converter.write(testObject, MediaType.APPLICATION_FORM_URLENCODED, mockOutputMessage);

        String expectedString = "myString=https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Durl%26oq%3Durl%26aqs%3Dchrome..69i57j69i60l3.1739j0j7%26sourceid%3Dchrome%26ie%3DUTF-8&myInt=5&myBool=true";
        byte[] expectedBytes = expectedString.getBytes(StandardCharsets.UTF_8);

        verify(mockOutputStream, times(1))
                .write(expectedBytes);
    }

    @Test
    public void shouldInitializeEmptyMap() {
        ObjectToUrlEncodedConverter.UrlEncodedWriter encodedWriter = new ObjectToUrlEncodedConverter.UrlEncodedWriter();

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, Object> actual = (Map<String, Object>) ReflectionTestUtils.getField(encodedWriter, "properties");

        //ConstantConditions - This is fine for the purposes of this test.
        //noinspection ConstantConditions
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldAddPropertyToPropertyMap() {
        String propertyName = "SomeProperty";
        String propertyValue = "SomeValue";

        ObjectToUrlEncodedConverter.UrlEncodedWriter encodedWriter = new ObjectToUrlEncodedConverter.UrlEncodedWriter();

        encodedWriter.set(propertyName, "other value");
        encodedWriter.set(propertyName, propertyValue);

        Map<String, Object> expected = Collections.singletonMap(propertyName, propertyValue);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, Object> actual = (Map<String, Object>) ReflectionTestUtils.getField(encodedWriter, "properties");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertPropertiesMapToUrlEncodedString() {
        String propertyName = "myString";
        String propertyValue = "https://www.google.com/search?q=url&oq=url&aqs=chrome..69i57j69i60l3.1739j0j7&sourceid=chrome&ie=UTF-8";

        ObjectToUrlEncodedConverter.UrlEncodedWriter encodedWriter = new ObjectToUrlEncodedConverter.UrlEncodedWriter();

        Map<String, Object> properties = Collections.singletonMap(propertyName, propertyValue);

        ReflectionTestUtils.setField(encodedWriter, "properties", properties);

        String expected = "myString=https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Durl%26oq%3Durl%26aqs%3Dchrome..69i57j69i60l3.1739j0j7%26sourceid%3Dchrome%26ie%3DUTF-8";
        String actual = encodedWriter.toString();

        assertEquals(expected, actual);
    }

}
