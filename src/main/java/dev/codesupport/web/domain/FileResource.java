package dev.codesupport.web.domain;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class FileResource {

    private final byte[] data;
    private final ContentType contentType;

    public static FileResource of(File file) throws IOException {
        if (file.exists()) {
            return of(file.toPath());
        } else {
            throw new FileNotFoundException("File does not exist");
        }
    }

    public static FileResource of(Path filePath) throws IOException {
        return new FileResource(
                Files.readAllBytes(filePath),
                ContentType.valueFrom(FilenameUtils.getExtension(filePath.toString()))
        );
    }

}
