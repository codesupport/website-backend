package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Data
@Entity
public class ArticleEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @ManyToOne(optional = false)
    private UserEntity createdBy;
    @ManyToOne(optional = false)
    private UserEntity updatedBy;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean visible;
    @ManyToOne(cascade = CascadeType.ALL)
    private TagSetEntity tagSet;
    @Column(updatable = false, nullable = false)
    private Long createDate;
    @Column(nullable = false)
    private Long updateDate;

    @PrePersist
    public void onCreateArticle() {
        createDate = System.currentTimeMillis();
        onUpdateArticle();
    }

    @PreUpdate
    public void onUpdateArticle() {
        updateDate = System.currentTimeMillis();
    }

}
