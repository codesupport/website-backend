package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
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
public class ContributorEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String alias;
    @ManyToOne
    private UserEntity user;
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    private ContributorListEntity contributorList;

}
