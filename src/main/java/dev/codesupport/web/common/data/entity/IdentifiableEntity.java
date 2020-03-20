package dev.codesupport.web.common.data.entity;

import java.io.Serializable;

/**
 * Used to extend entity classes so id specific logic can be generically handled across entities.
 *
 * @param <I> The type associated to the ID property of the entity
 */
public interface IdentifiableEntity<I> extends Serializable {

    I getId();

}
