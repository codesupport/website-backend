package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.AuditableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Article implements AuditableDomain<Long, Long> {

    private Long id;
    private String title;
    private ArticleRevision revision;
    private User createdBy;
    private Long createdOn;
    private User updatedBy;
    private Long updatedOn;

}
