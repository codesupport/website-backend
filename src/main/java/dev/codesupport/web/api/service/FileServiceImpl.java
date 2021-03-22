package dev.codesupport.web.api.service;

import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.domain.ContentType;
import dev.codesupport.web.domain.FileReference;
import dev.codesupport.web.domain.FileResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    public static final String IMAGE_STORAGE_DIR = "/tmp/codesupport/upload/images/articles/";
    public static final String ARTICLE_COVER_IMAGE_STORAGE_DIR = "/tmp/codesupport/upload/images/articles/cover/";

    @Override
    public FileResource getArticleImage(String fileName) {
        return getFile(IMAGE_STORAGE_DIR, fileName);
    }

    @Override
    public FileResource getArticleCoverImage(String fileName) {
        return getFile(ARTICLE_COVER_IMAGE_STORAGE_DIR, fileName);
    }

    public FileResource getFile(String storageDirectory, String fileName) {
        FileResource fileResource;

        try {
            fileResource = FileResource.of(new File(storageDirectory + fileName));
        } catch (IOException e) {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return fileResource;
    }

    @Override
    public FileReference storeArticleImage(MultipartFile file) {
        return storeFile(IMAGE_STORAGE_DIR, file);
    }

    @Override
    public FileReference storeArticleCoverImage(MultipartFile file) {
        return storeFile(ARTICLE_COVER_IMAGE_STORAGE_DIR, file);
    }

    public FileReference storeFile(String storageDirectory, MultipartFile file) {
        ContentType contentType = ContentType.valueFrom(file.getContentType());

        FileReference fileReference = new FileReference();
        fileReference.setId(RandomStringUtils.randomAlphanumeric(20));
        fileReference.setName(fileReference.getId() + "." + contentType.getExtension());
        fileReference.setContentType(contentType);
        fileReference.setFileSizeB(file.getSize());

        try {
            File imageFile = new File(storageDirectory + fileReference.getName());
            FileUtils.writeByteArrayToFile(imageFile, file.getBytes());
        } catch (IOException e) {
            throw new ServiceLayerException("Unable to create file.", e);
        }

        return fileReference;
    }

}
