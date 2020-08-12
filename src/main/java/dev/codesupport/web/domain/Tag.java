package dev.codesupport.web.domain;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Tag {

    private Long id;
    private String label;

}
