package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.FileReference;
import dev.codesupport.web.domain.FileResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileResource getArticleImage(@PathVariable String fileName);

    FileResource getArticleCoverImage(@PathVariable String fileName);

    @PreAuthorize("hasPermission('Image', 'create')")
    FileReference storeArticleImage(MultipartFile file);

    @PreAuthorize("hasPermission('Image', 'create')")
    FileReference storeArticleCoverImage(MultipartFile file);

}
