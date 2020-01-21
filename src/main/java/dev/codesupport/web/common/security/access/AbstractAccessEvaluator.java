package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import lombok.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Parent evaluator from which all children are created.
 *
 * @param <T> The class type associated with the evaluator
 */
public abstract class AbstractAccessEvaluator<T> {

    private final Class<T> classType;

    public AbstractAccessEvaluator() {
        // Finds the type of the class parameter and stores it for later mappings.
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            Type parameterizedType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            if (parameterizedType instanceof Class) {
                classType = (Class) parameterizedType;
            } else {
                // This is really not possible.
                throw new IllegalArgumentException("Internal error: Parameter was not a class.");
            }
        } else {
            // Must not have a class parameter, throw an exception.
            throw new IllegalArgumentException("Internal error: Cannot instantiate AbstractThrowableParser without exception type.");
        }
    }

    protected abstract boolean hasPermissionCheck(Authentication auth, T targetDomainObject, String permission);

    /**
     * Gets the canonical class name of the class type
     *
     * @return String value of the class in canonical format
     */
    String getEvaluatorClassType() {
        return classType.getCanonicalName();
    }

    /**
     * Used for evaluating permissions when there is no associated object or class type for the type of access
     *
     * @return The accessor type associated with the evaluation
     */
    protected Accessor getAccessor() {
        return Accessor.NONE;
    }

    /**
     * Calls the evaluator
     * <p>Checks if given object is not null and is the same type as expected by the evaluator prior to
     * invoking the evaluator's hasPermission() method</p>
     *
     * @param auth The Authentication associated with the access evaluation
     * @param targetDomainObject The object associated with the access evaluation
     * @param permission The permission associated with the access evaluation
     * @return True if the user has access for the givne permission on the given object, False otherwise
     */
    public boolean hasPermission(Authentication auth, @NonNull Object targetDomainObject, String permission){
        boolean hasPermission = false;
        if(classType.isInstance(targetDomainObject)) {
            hasPermission = hasPermissionCheck(auth, classType.cast(targetDomainObject), permission);
        } else {
            throw new InvalidArgumentException("Given target object type '" + targetDomainObject.getClass().getSimpleName() + "' must be '" + classType.getSimpleName() + "'");
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
