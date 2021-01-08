package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.web.common.configuration.FileUploadProperties;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.exception.FileTooBigException;
import dev.codesupport.web.domain.validation.annotation.ImageUploadConstraint;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ImageUploadValidatorTest {

    private static FileUploadProperties mockFileUploadProperties;
    private static FileUploadProperties.Image mockImageProperties;

    private ImageUploadValidator validatorSpy;

    @BeforeClass
    public static void init() {
        mockFileUploadProperties = mock(FileUploadProperties.class);
        mockImageProperties = mock(FileUploadProperties.Image.class);
    }

    @Before
    public void setUp() {
        Mockito.reset(
                mockFileUploadProperties,
                mockImageProperties
        );

        doReturn(mockImageProperties)
                .when(mockFileUploadProperties)
                .imageProperties();

        validatorSpy = spy(new ImageUploadValidator(mockFileUploadProperties));
        ReflectionTestUtils.setField(validatorSpy, "fileUploadProperties", mockFileUploadProperties);
    }

    @Test
    public void shouldSetValidTypesFromAnnotation() {
        String[] validTypes = { "a", "b", "c" };
        ImageUploadConstraint mockConstraint = mock(ImageUploadConstraint.class);

        doReturn(validTypes)
                .when(mockConstraint)
                .types();

        validatorSpy.initialize(mockConstraint);

        Set<String> expected = Sets.newHashSet(validTypes);
        Object actual = ReflectionTestUtils.getField(validatorSpy, "validTypes");

        assertEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfMaxSizeNotConfigured() {
        MultipartFile multipartFile = createMultipartFile("image/jpg", 10L);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(mockImageProperties)
                .getMaxSize();

        validatorSpy.isValid(multipartFile, null);
    }

    @Test(expected = FileTooBigException.class)
    public void shouldThrowFileTooBigException() {
        MultipartFile multipartFile = createMultipartFile("image/jpg", 10L);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(5L)
                .when(mockImageProperties)
                .getMaxSize();

        validatorSpy.isValid(multipartFile, null);
    }

    @Test
    public void shouldReturnFalseForIsValid() {
        MultipartFile multipartFile = createMultipartFile("image/jpg", 10L);

        ReflectionTestUtils.setField(validatorSpy, "validTypes", Collections.singleton("image/png"));

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(15L)
                .when(mockImageProperties)
                .getMaxSize();

        assertFalse(validatorSpy.isValid(multipartFile, null));
    }

    @Test
    public void shouldReturnTrueForIsValid() {
        MultipartFile multipartFile = createMultipartFile("image/jpg", 10L);

        ReflectionTestUtils.setField(validatorSpy, "validTypes", Collections.singleton("image/jpg"));

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(15L)
                .when(mockImageProperties)
                .getMaxSize();

        assertTrue(validatorSpy.isValid(multipartFile, null));
    }

    //Warnings - We don't care about these, this is a test utility method
    @SuppressWarnings({"NullableProblems", "RedundantThrows", "SameParameterValue"})
    private MultipartFile createMultipartFile(String contentType, long size) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return size;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
    }

}
