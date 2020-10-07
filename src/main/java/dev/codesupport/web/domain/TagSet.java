package dev.codesupport.web.domain;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Data
@FieldNameConstants
public class TagSet {

    private Long id;
    private List<Tag> tags;

}
