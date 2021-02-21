package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.FileService;
import dev.codesupport.web.domain.FileReference;
import dev.codesupport.web.domain.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileControllerImpl implements FileController {

    private final FileService service;

    @Autowired
    public FileControllerImpl(FileService service) {
        this.service = service;
    }

    //TODO: It would be great to just return a file resource object if this can be figured out.
    @Override
    public ResponseEntity<?> getArticleImage(@PathVariable String fileName) {
        FileResource fileResource = service.getArticleImage(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, fileResource.getContentType().getType());
        return new ResponseEntity<>(fileResource.getData(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getArticleCoverImage(@PathVariable String fileName) {
        FileResource fileResource = service.getArticleCoverImage(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, fileResource.getContentType().getType());
        return new ResponseEntity<>(fileResource.getData(), headers, HttpStatus.OK);
    }

    @Override
    public FileReference storeArticleImage(MultipartFile file) {
        return service.storeArticleImage(file);
    }

    @Override
    public FileReference storeArticleCoverImage(MultipartFile file) {
        return service.storeArticleCoverImage(file);
    }

}
