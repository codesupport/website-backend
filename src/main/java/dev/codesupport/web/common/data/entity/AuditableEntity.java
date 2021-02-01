package dev.codesupport.web.common.data.entity;

/**
 * Used to extend entity classes so id specific logic can be generically handled across entities.
 *
 * @param <I> The type associated to the ID property of the entity
 */
public interface AuditableEntity<I, T> extends IdentifiableEntity<I> {

    T getUpdatedOn();

    void setUpdatedOn(T updatedOn);

}
