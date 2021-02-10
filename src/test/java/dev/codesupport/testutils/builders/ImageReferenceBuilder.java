package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.ImageReferenceEntity;

public class ImageReferenceBuilder {

    private Long id;
    private Long articleId;
    private String imageName;

    private ImageReferenceBuilder() {

    }

    public static ImageReferenceBuilder builder() {
        return new ImageReferenceBuilder();
    }

    public ImageReferenceEntity buildEntity() {
        ImageReferenceEntity entity = new ImageReferenceEntity();
        entity.setId(id);
        entity.setRevisionId(articleId);
        entity.setImageName(imageName);
        return entity;
    }

    public ImageReferenceBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ImageReferenceBuilder articleId(Long articleId) {
        this.articleId = articleId;
        return this;
    }

    public ImageReferenceBuilder imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }
}
