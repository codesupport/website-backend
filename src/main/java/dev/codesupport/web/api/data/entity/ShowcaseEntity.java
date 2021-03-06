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

@Data
@Entity
public class ShowcaseEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private UserEntity user;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private boolean approved;
    @Column(nullable = false)
    private String link;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private ContributorListEntity contributorList;

}
