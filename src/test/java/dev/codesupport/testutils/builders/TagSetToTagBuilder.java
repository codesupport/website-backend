package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.TagSetToTagEntity;

/**
 * This is a test utility used to create objects for unit tests
 */
public class TagSetToTagBuilder {

    private Long id;
    private TagBuilder tag;
    private TagSetBuilder tagSet;

    private TagSetToTagBuilder() {

    }

    public static TagSetToTagBuilder builder() {
        return new TagSetToTagBuilder();
    }

    public TagSetToTagEntity buildEntity() {
        TagSetToTagEntity entity = new TagSetToTagEntity();
        entity.setId(id);
        if (tag != null) {
            entity.setTag(tag.buildEntity());
        }
        if (tagSet != null) {
            entity.setTagSet(tagSet.buildEntity());
        }
        return entity;
    }

    public TagSetToTagBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TagSetToTagBuilder tag(TagBuilder tag) {
        this.tag = tag;
        return this;
    }

    public TagSetToTagBuilder tagSet(TagSetBuilder tagSet) {
        this.tagSet = tagSet;
        return this;
    }

}
