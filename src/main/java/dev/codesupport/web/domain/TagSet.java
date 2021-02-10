package dev.codesupport.web.domain;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Data
@FieldNameConstants
public class TagSet {

    private Long id;
    private Set<Tag> tags;

}
