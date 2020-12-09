package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * Parent evaluator from which all children are created.
 *
 * @param <T> The class type associated with the evaluator
 */
@EqualsAndHashCode
public abstract class AbstractAccessEvaluator<T> {

    private final Class<T> classType;
    private final Permission permission;

    public AbstractAccessEvaluator(Permission permission) {
        this.permission = permission;

        // Finds the type of the class parameter and stores it for later mappings.
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            Type parameterizedType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            if (parameterizedType instanceof Class) {
                //unchecked - We should be good here, the type is derived from the instance itself.
                //noinspection unchecked
                classType = (Class<T>) parameterizedType;
            } else {
                // Not allowing nested paramaterized types
                throw new IllegalArgumentException("Internal error: Invalid parameter type.");
            }
        } else {
            // Must not have a class parameter, throw an exception.
            throw new IllegalArgumentException("Internal error: Cannot instantiate AbstractAccessEvaluator without a type.");
        }
    }

    /**
     * Specific sub class implementation for permission checks
     *
     * @param auth               The Authentication associated with the access evaluation
     * @param targetDomainObject The object associated with the access evaluation
     * @return True if the user has access for the given permission on the given object, False otherwise
     */
    protected abstract boolean hasPermissionCheck(Authentication auth, T targetDomainObject);

    /**
     * Returns the derived evaluator name based on permission and associated class type
     *
     * @return Derived evaluator name
     */
    public String getEvaluatorName() {
        return permission.toString().toLowerCase() + " " +
                (
                        getAccessor() == Accessor.NONE ?
                                classType.getCanonicalName() :
                                getAccessor().toString().toLowerCase()
                );
    }

    /**
     * Used for evaluating permissions when there is no associated object or class type for permission evaluation
     *
     * @return The Accessor associated with this permission evaluation.
     */
    public Accessor getAccessor() {
        return Accessor.NONE;
    }

    /**
     * Calls the evaluator
     * <p>Checks if given object is not null and is the same type as expected by the evaluator prior to
     * invoking the evaluator's hasPermission() method</p>
     *
     * @param auth               The Authentication associated with the access evaluation
     * @param targetDomainObject The object associated with the access evaluation
     * @return True if the user has access for the givne permission on the given object, False otherwise
     */
    public boolean hasPermission(Authentication auth, @NonNull Object targetDomainObject) {
        boolean hasPermission;
        if (classType.isInstance(targetDomainObject)) {
            hasPermission = hasPermissionCheck(auth, classType.cast(targetDomainObject));
            // Check if targetDomainObject was a list of class objects.
        } else if (targetDomainObject instanceof List) {
            hasPermission = true;
            //unchecked - This should be fine, type was checked in evaluator factory
            //noinspection unchecked
            Iterator<Object> iterator = ((List<Object>) targetDomainObject).iterator();
            // Cycle through until one fails, or they all pass.
            while (hasPermission && iterator.hasNext()) {
                Object domainObject = iterator.next();
                if (classType.isInstance(domainObject)) {
                    hasPermission = hasPermissionCheck(auth, classType.cast(domainObject));
                } else {
                    throw new InvalidArgumentException("List has invalid element class types, Expecting '" +
                            classType.getCanonicalName() + "', found '" +
                            domainObject.getClass().getSimpleName() + "'");
                }
            }
        } else {
            throw new InvalidArgumentException("Expecting '" + classType.getCanonicalName() + "', found '" + targetDomainObject.getClass().getSimpleName() + "'");
        }
        return hasPermission;
    }

    /**
     * Checks of the given Authentication is valid
     *
     * @param authentication The Authentication to validate
     * @return True if the Authentication is not null & not Anonymous, False otherwise
     */
    protected boolean isValidAuth(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

}
