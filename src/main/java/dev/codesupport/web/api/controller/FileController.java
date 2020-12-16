package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.FileReference;
import dev.codesupport.web.domain.validation.annotation.ImageUploadConstraint;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file/v1")
@Api(value = "File", description = "REST API for Files", tags = {"File"})
@Validated
public interface FileController {

    @GetMapping("/images/{fileId}")
    void getImage(@PathVariable String fileId);

    @PostMapping("/images")
    FileReference storeImage(@RequestParam("file") @ImageUploadConstraint MultipartFile file);

}
