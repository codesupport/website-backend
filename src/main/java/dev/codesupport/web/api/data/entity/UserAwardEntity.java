package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserAwardEntity implements IdentifiableEntity<Long> {

    @Id
    private Long id;
    private String code;
    private String label;
    private String description;

}
