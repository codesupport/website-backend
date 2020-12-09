package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Showcase implements IdentifiableDomain<Long> {

    private Long id;
    private User user;
    private String title;
    private String description;
    private boolean approved;
    private String link;
    private ContributorList contributorList;

}
