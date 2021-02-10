package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.TagEntity;

/**
 * This is a test utility used to create objects for unit tests
 */
public class TagBuilder {

    private Long id;
    private String label;

    private TagBuilder() {

    }

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public TagEntity buildEntity() {
        TagEntity entity = new TagEntity();
        entity.setId(id);
        entity.setLabel(label);
        return entity;
    }

    public TagBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder label(String label) {
        this.label = label;
        return this;
    }

}
