package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ContributorListEntity;
import dev.codesupport.web.domain.ContributorList;

import java.util.List;
import java.util.stream.Collectors;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class ContributorListBuilder {

    private Long id;
    private List<ContributorBuilder> contributors;

    private ContributorListBuilder() {

    }

    public static ContributorListBuilder builder() {
        return new ContributorListBuilder();
    }

    public ContributorList buildDomain() {
        ContributorList domain = new ContributorList();
        domain.setId(id);
        if (contributors != null) {
            domain.setContributors(contributors.stream().map(ContributorBuilder::buildDomain).collect(Collectors.toList()));
        }
        return domain;
    }

    public ContributorListEntity buildEntity() {
        ContributorListEntity entity = new ContributorListEntity();
        entity.setId(id);
        return entity;
    }

    public ContributorListBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ContributorListBuilder contributors(List<ContributorBuilder> contributors) {
        this.contributors = contributors;
        return this;
    }
}
