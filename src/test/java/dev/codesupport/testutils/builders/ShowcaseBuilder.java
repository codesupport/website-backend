package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import dev.codesupport.web.domain.Showcase;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class ShowcaseBuilder {

    private Long id;
    private UserBuilder user;
    private String title;
    private String description;
    private boolean approved;
    private String link;
    private ContributorListBuilder contributorList;

    private ShowcaseBuilder() {

    }

    public static ShowcaseBuilder builder() {
        return new ShowcaseBuilder();
    }

    public Showcase buildDomain() {
        Showcase domain = new Showcase();
        domain.setId(id);
        if (user != null)
        domain.setUser(user.buildUserStrippedDomain());
        domain.setTitle(title);
        domain.setDescription(description);
        domain.setApproved(approved);
        domain.setLink(link);
        if (contributorList != null)
        domain.setContributorList(contributorList.buildDomain());
        return domain;
    }

    public ShowcaseEntity buildEntity() {
        ShowcaseEntity domain = new ShowcaseEntity();
        domain.setId(id);
        if (user != null)
            domain.setUser(user.buildEntity());
        domain.setTitle(title);
        domain.setDescription(description);
        domain.setApproved(approved);
        domain.setLink(link);
        if (contributorList != null)
            domain.setContributorList(contributorList.buildEntity());
        return domain;
    }

    public ShowcaseBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ShowcaseBuilder user(UserBuilder user) {
        this.user = user;
        return this;
    }

    public ShowcaseBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ShowcaseBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ShowcaseBuilder approved(boolean approved) {
        this.approved = approved;
        return this;
    }

    public ShowcaseBuilder link(String link) {
        this.link = link;
        return this;
    }

    public ShowcaseBuilder contributorList(ContributorListBuilder contributorList) {
        this.contributorList = contributorList;
        return this;
    }
}
