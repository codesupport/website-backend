package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.List;

@Data
public class ContributorList implements IdentifiableDomain<Long> {

    private Long id;
    private List<Contributor> contributors;

}
