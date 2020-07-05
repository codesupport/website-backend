package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ContributorEntity;
import dev.codesupport.web.domain.Contributor;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class ContributorBuilder {

    private Long id;
    private String alias;
    private UserBuilder user;
    private ContributorListBuilder contributorList;

    private ContributorBuilder() {

    }

    public static ContributorBuilder builder() {
        return new ContributorBuilder();
    }

    public Contributor buildDomain() {
        Contributor domain = new Contributor();
        domain.setId(id);
        domain.setAlias(alias);
        if (user != null)
        domain.setUser(user.buildUserStrippedDomain());
        if (contributorList != null) {
            domain.setContributorList(contributorList.buildDomain());
        }
        return domain;
    }

    public ContributorEntity buildEntity() {
        ContributorEntity entity = new ContributorEntity();
        entity.setId(id);
        entity.setAlias(alias);
        if (user != null)
            entity.setUser(user.buildEntity());
        if (contributorList != null) {
            entity.setContributorList(contributorList.buildEntity());
        }
        return entity;
    }

    public ContributorBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ContributorBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    public ContributorBuilder user(UserBuilder user) {
        this.user = user;
        return this;
    }

    public ContributorBuilder contributorList(ContributorListBuilder contributorList) {
        this.contributorList = contributorList;
        return this;
    }
}
