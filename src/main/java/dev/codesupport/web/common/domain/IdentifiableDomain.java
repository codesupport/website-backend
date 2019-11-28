package dev.codesupport.web.common.domain;

import java.io.Serializable;

/**
 * Used to extend domain classes so id specific logic can be generically handled across domain classes.
 *
 * @param <I> The type associated to the ID property of the domain class
 */
public interface IdentifiableDomain<I> extends Serializable {

    I getId();

}
