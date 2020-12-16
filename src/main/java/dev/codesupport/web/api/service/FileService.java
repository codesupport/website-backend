package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.FileReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void getImage(@PathVariable String fileId);

//    @PreAuthorize("hasPermission(#file, 'create')")
    FileReference storeImage(MultipartFile file);

}
