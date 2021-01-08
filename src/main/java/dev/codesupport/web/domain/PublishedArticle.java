package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class PublishedArticle implements IdentifiableDomain<Long> {

    private Long id;
    private Article article;
    private String articleCode;
    private boolean published;

}
