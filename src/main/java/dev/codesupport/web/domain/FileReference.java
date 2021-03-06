package dev.codesupport.web.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileReference implements Serializable {

    private String id;
    private String name;
    private ContentType contentType;
    private Long fileSizeB;

}
