package dev.codesupport.web.common.util;

import dev.codesupport.web.common.configuration.FileUploadProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
public class ImageReferenceScanner {

    private static final String FILE_NAME = "FileName";

    private final Pattern pattern;

    public ImageReferenceScanner(FileUploadProperties fileUploadProperties) {
        String regEx = StringUtils.stripEnd(fileUploadProperties.getHostName(), "/")
                .replace(".", "\\.");
        pattern = Pattern.compile("\\[[^]]*]\\(" + regEx + "/file/v\\d+/images/(?<" + FILE_NAME + ">[^)]+)\\)");
    }

    public Set<String> scan(String text) {
        Set<String> values = new HashSet<>();
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            values.add(matcher.group(FILE_NAME));
        }

        return values;
    }

}
