package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Contributor extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
    private UserStripped user;
    private ContributorList contributorList;

}
