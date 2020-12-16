package dev.codesupport.web.api.service;

import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.domain.FileReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    public static final String IMAGE_STORAGE_DIR = "/tmp/codesupport/upload/images/";

    private final Map<String, String> FILE_EXTENSION;

    public FileServiceImpl() {
        FILE_EXTENSION = new HashMap<>();
        FILE_EXTENSION.put("image/png", "png");
        FILE_EXTENSION.put("image/jpg", "jpg");
        FILE_EXTENSION.put("image/jpeg", "jpg");
        FILE_EXTENSION.put("image/svg+xml", "svg");
    }

    @Override
    public void getImage(String fileId) {

    }

    @Override
    public FileReference storeImage(MultipartFile file) {
        FileReference fileReference = new FileReference();
        fileReference.setId(RandomStringUtils.randomAlphanumeric(20));
        fileReference.setContentType(file.getContentType());
        fileReference.setFileSizeB(file.getSize());

        try {
            File imageFile = new File(IMAGE_STORAGE_DIR + fileReference.getId() + "." + FILE_EXTENSION.get(file.getContentType()));
            FileUtils.writeByteArrayToFile(imageFile, file.getBytes());
        } catch (IOException e) {
            throw new ServiceLayerException("Unable to create file.", e);
        }

        return fileReference;
    }

}
