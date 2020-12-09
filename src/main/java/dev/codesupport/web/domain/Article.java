package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@FieldNameConstants
public class Article implements IdentifiableDomain<Long> {

    private Long id;
    private String articleCode;
    private String title;
    private String description;
    private String content;
    private List<String> tags;
    private User createdBy;
    private Long createdOn;
    private User updatedBy;
    private Long updatedOn;

    public void setTagSet(TagSet tagSet) {
        if (tagSet != null) {
            tags = tagSet.getTags().stream().map(Tag::getLabel).collect(Collectors.toList());
        }
    }

    public TagSet getTagSet() {
        TagSet tagSet = new TagSet();
        List<Tag> tagList = new ArrayList<>();

        for (String label : tags) {
            Tag tag = new Tag();
            tag.setLabel(label);
            tagList.add(tag);
        }

        tagSet.setTags(tagList);
        return tagSet;
    }

}
