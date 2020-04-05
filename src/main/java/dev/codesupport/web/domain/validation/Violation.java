package dev.codesupport.web.domain.validation;

/**
 * Interface for reporting validation issues
 */
public interface Violation {

    /**
     * Determine if any violations were previously reported.
     *
     * @return True if no violations were reported, False otherwise.
     */
    boolean isValid();

    /**
     * Reports a property that is either null or otherwise empty
     *
     * @param propertyName The name of the property reported
     */
    void missing(String propertyName);

    /**
     * Reports a property that is invalid
     *
     * @param propertyName The name of the property reported
     */
    void invalid(String propertyName);

    /**
     * Reports a property that is invalid
     *
     * @param propertyName The name of the property reported
     * @param value        The invalid value that was assigned
     */
    void invalidValue(String propertyName, String value);

    /**
     * Reports a property that is null
     *
     * @param propertyName The name of the property reported
     */
    void nullValue(String propertyName);

    /**
     * Reports a property with a given message
     * <p>Flags the object as invalid when any violation is reported</p>
     *
     * @param propertyName The name of the property reported
     * @param message      The message describing the property's validation issue
     */
    void violation(String propertyName, String message);

}
