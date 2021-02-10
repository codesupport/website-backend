package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
@Data
public class ArticleRevisionEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long articleId;
    @Column(nullable = false)
    private String description;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private Long tagSetId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "createdBy", updatable = false)
    private UserEntity createdBy;
    @Column(updatable = false, nullable = false)
    private Long createdOn;

    @PrePersist
    public void onCreate() {
        setCreatedOn(System.currentTimeMillis());
    }

}
