package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CountryEntity implements IdentifiableEntity<Long> {

    @Id
    private Long id;
    @Column(updatable = false, length = 2, nullable = false)
    private String code;
    @Column(length = 50, nullable = false)
    private String label;

}
