package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.FileService;
import dev.codesupport.web.domain.FileReference;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void getImage(@PathVariable String fileId) {
//        return service.getImage(fileId);
    }

    @Override
    public FileReference storeImage(MultipartFile file) {
        return service.storeImage(file);
    }

}
