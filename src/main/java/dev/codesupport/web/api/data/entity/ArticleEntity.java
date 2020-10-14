package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Data
@Entity
public class ArticleEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String articleCode;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean finalized;
    @Transient
    private TagSetEntity tagSet;
    @Column(nullable = false)
    private Long tagSetId;
    @ManyToOne(optional = false)
    private UserEntity createdBy;
    @Column(updatable = false, nullable = false)
    private Long createdOn;
    @ManyToOne(optional = false)
    private UserEntity updatedBy;
    @Column(nullable = false)
    private Long updatedOn;

}