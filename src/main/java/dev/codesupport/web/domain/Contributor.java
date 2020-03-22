package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Contributor implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private UserStripped user;
    private ContributorList contributorList;

}
