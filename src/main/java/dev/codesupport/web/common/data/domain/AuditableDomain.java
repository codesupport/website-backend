package dev.codesupport.web.common.data.domain;

/**
 * Used to extend domain classes so id & audit specific logic can be generically handled across domain classes.
 *
 * @param <I> The type associated to the ID property of the domain class
 * @param <T> The type associated to the UpdatedOn property of the domain class
 */
public interface AuditableDomain<I, T> extends IdentifiableDomain<I> {

    T getUpdatedOn();

    void setUpdatedOn(T updatedOn);

}
