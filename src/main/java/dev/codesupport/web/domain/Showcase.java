package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Showcase extends AbstractValidatable<Long> {

    private Long id;
    private UserStripped user;
    private String title;
    private String description;
    private boolean approved;
    private String link;
    private ContributorList contributorList;

}
