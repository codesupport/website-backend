package dev.codesupport.web.common.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.codesupport.web.api.data.entity.UserEntity;

/**
 * Used to extend entity classes so id specific logic can be generically handled across entities.
 *
 * @param <I> The type associated to the ID property of the entity
 */
public interface AuditableEntity<I, T> extends IdentifiableEntity<I> {

    @JsonIgnore
    AuditEntity<T> getAuditEntity();

    void setAuditEntity(AuditEntity<T> auditEntity);

    default UserEntity getCreatedBy() {
        return getAuditEntity().getCreatedBy();
    }

}
