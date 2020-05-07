package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Article implements IdentifiableDomain<Long> {

    private Long id;
    private String title;
    private String description;
    private boolean visible;
    private UserStripped createdBy;
    private UserStripped updatedBy;
    private String content;
    private TagSet tagSet;
    private Long createDate;
    private Long updateDate;

}
