package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@FieldNameConstants
public class ArticleRevision implements IdentifiableDomain<Long> {

    private Long id;
    private Long articleId;
    private String description;
    private String content;
    private Set<String> tags = new HashSet<>();
    private User createdBy;
    private Long createdOn;

    public void setTagSet(TagSet tagSet) {
        if (tagSet != null) {
            tags = tagSet.getTags().stream().map(Tag::getLabel).collect(Collectors.toSet());
        }
    }

    public TagSet getTagSet() {
        TagSet tagSet = new TagSet();
        Set<Tag> uniqueTags = ObjectUtils.defaultIfNull(tags, Collections.<String>emptySet())
                .stream().map(label -> {
                    Tag tag = new Tag();
                    tag.setLabel(label);
                    return tag;
                })
                .collect(Collectors.toSet());

        tagSet.setTags(uniqueTags);
        return tagSet;
    }

}
