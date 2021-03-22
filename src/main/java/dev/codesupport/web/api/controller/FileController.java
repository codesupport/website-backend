package dev.codesupport.web.api.controller;

import dev.codesupport.web.common.service.http.DontWrapResponse;
import dev.codesupport.web.domain.FileReference;
import dev.codesupport.web.domain.validation.annotation.ImageUploadConstraint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
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

    //S1452 - Spring requires returning byte, which can't be used as a parameterized type.
    @SuppressWarnings("java:S1452")
    @DontWrapResponse
    @ApiOperation("Get article image")
    @GetMapping("/images/articles/{fileName}")
    ResponseEntity<?> getArticleImage(@PathVariable String fileName);

    //S1452 - Spring requires returning byte, which can't be used as a parameterized type.
    @SuppressWarnings("java:S1452")
    @DontWrapResponse
    @ApiOperation("Get all cover image")
    @GetMapping("/images/articles/cover/{fileName}")
    ResponseEntity<?> getArticleCoverImage(@PathVariable String fileName);

    @ApiOperation("Store article image")
    @PostMapping("/images/articles")
    FileReference storeArticleImage(@RequestParam("file") @ImageUploadConstraint MultipartFile file);

    @ApiOperation("Store article cover image")
    @PostMapping("/images/articles/cover")
    FileReference storeArticleCoverImage(@RequestParam("file") @ImageUploadConstraint MultipartFile file);

}
