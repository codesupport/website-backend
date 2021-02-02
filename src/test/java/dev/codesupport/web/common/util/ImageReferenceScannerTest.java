package dev.codesupport.web.common.util;

import com.google.common.collect.Sets;
import dev.codesupport.web.common.configuration.FileUploadProperties;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ImageReferenceScannerTest {

    @Test
    public void shouldCorrectlySetPattern() {
        String hostName = "http://localhost/////";

        FileUploadProperties mockProperties = mock(FileUploadProperties.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(hostName)
                .when(mockProperties)
                .getHostName();

        ImageReferenceScanner scanner = new ImageReferenceScanner(mockProperties);

        Pattern expected = Pattern.compile("\\[[^]]*]\\(http://localhost/file/v\\d+/images/(?<FileName>[^)]+)\\)");
        Pattern actual = (Pattern)ReflectionTestUtils.getField(scanner, "pattern");

        //ConstantConditions - If it's null, the test will fail
        //noinspection ConstantConditions
        assertEquals(expected.pattern(), actual.pattern());
    }

    @Test
    public void shouldFindAllMatches() {
        String fileName1 = "AfIg9d7Ahng920vDg78jd.jpg";
        String fileName2 = "Iud9g9eEHent8d7D9gHe8g8g0.png";
        String fileName3 = "i8d7HGieNlaEIdiguD8d7ghn31.gif";

        FileUploadProperties mockProperties = mock(FileUploadProperties.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn("")
                .when(mockProperties)
                .getHostName();

        ImageReferenceScanner scanner = new ImageReferenceScanner(mockProperties);

        Pattern pattern = Pattern.compile("\\[[^]]*]\\(http://localhost/file/v\\d+/images/(?<FileName>[^)]+)\\)");
        ReflectionTestUtils.setField(scanner, "pattern", pattern);

        String text = "Some leading text [im](http://localhost/file/v1/images/" + fileName1 + ") and an invalid"
                + " http://localhost/file/v3/images/fiad7A98dg8QH2380gf0UDS.gif some ensuing text followed by"
                + " an [image](http://localhost/file/v0/images/" + fileName2 + ") and finally one last valid"
                + " [i](http://localhost/file/v1/images/" + fileName3 + ") before an invalid"
                + " (http://localhost/file/v1/images/Fjd8D8f8ashdDFd8ah30g09.jpg)";

        Set<String> expected = Sets.newHashSet(fileName1, fileName2, fileName3);
        Set<String> actual = scanner.scan(text);

        assertEquals(expected, actual);
    }

}
