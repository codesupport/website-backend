package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContributorList extends AbstractValidatable<Long> {

    private Long id;
    private List<Contributor> contributors;

}
