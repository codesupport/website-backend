package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.TagSetEntity;

/**
 * This is a test utility used to create objects for unit tests
 */
public class TagSetBuilder {

    private Long id;

    private TagSetBuilder() {

    }

    public static TagSetBuilder builder() {
        return new TagSetBuilder();
    }

    public TagSetEntity buildEntity() {
        TagSetEntity entity = new TagSetEntity();
        entity.setId(id);
        return entity;
    }

    public TagSetBuilder id(Long id) {
        this.id = id;
        return this;
    }

}
