package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Data
@FieldNameConstants
public class ContributorList implements IdentifiableDomain<Long> {

    private Long id;
    private List<Contributor> contributors;

}
