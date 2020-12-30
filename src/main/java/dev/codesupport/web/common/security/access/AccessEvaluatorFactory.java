package dev.codesupport.web.common.security.access;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Gets the appropriate evaluator for a given access evaluation
 */
@Component
public class AccessEvaluatorFactory {

    private Map<String, AbstractAccessEvaluator<?>> evaluatorMap;
    private ApplicationContext context;

    /**
     * Builds evaluator maps for all found AccessEvaluator components
     *
     * @param context The spring application context
     */
    @Autowired
    public AccessEvaluatorFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Lazy loads the evaluatorMap
     * <p>Upon first request for an evaluator, loads the evaluators from the applciation context.
     * This is done to combat a bean load order issue that surfaced.</p>
     *
     * @return The map of evaluators
     */
    @VisibleForTesting
    Map<String, AbstractAccessEvaluator<?>> getEvaluatorMap() {
        if (evaluatorMap == null) {
            evaluatorMap = new HashMap<>();

            //rawtypes - No choice here.
            //noinspection rawtypes
            Map<String, AbstractAccessEvaluator> beanMap = context.getBeansOfType(AbstractAccessEvaluator.class);

            for (AbstractAccessEvaluator<?> evaluator : beanMap.values()) {
                evaluatorMap.put(evaluator.getEvaluatorName(), evaluator);
            }
        }
        return evaluatorMap;
    }

    /**
     * Get evaluator that matches type of targetDomainObject
     *
     * @param targetDomainObject Object related to access evaluation
     * @return Evaluator that evaluates the type of the given targetDomainObject
     */
    //S1452 - No way to know what type of evaluator it will be.
    @SuppressWarnings("squid:S1452")
    public AbstractAccessEvaluator<?> getEvaluator(Object targetDomainObject, String permission) {
        AbstractAccessEvaluator<?> evaluator;

        if (targetDomainObject instanceof String) {
            evaluator = getEvaluatorByName(targetDomainObject.toString().toLowerCase(), permission);
        } else {
            String domainObjectClassName;

            // If list, we use first element to find class type, else we use the base object
            if (targetDomainObject == null) {
                domainObjectClassName = null;
            } else if (targetDomainObject instanceof Collection) {
                domainObjectClassName = ((Collection<?>) targetDomainObject).iterator().next().getClass().getCanonicalName();
            } else {
                domainObjectClassName = targetDomainObject.getClass().getCanonicalName();
            }

            evaluator = getEvaluatorByName(domainObjectClassName, permission);
        }

        return evaluator;
    }

    /**
     * Get evaluator that matches the canonicalClassName string
     *
     * @param canonicalClassName Canonical class name of class associated with access evaluation
     * @return Evaluator that evaluates the given canonical class name
     */
    //S1452 - No way to know what type of evaluator it will be.
    @SuppressWarnings("squid:S1452")
    @VisibleForTesting
    AbstractAccessEvaluator<?> getEvaluatorByName(String canonicalClassName, String permission) {
        AbstractAccessEvaluator<?> evaluator;

        String evaluatorName = permission + " " + canonicalClassName;

        if (getEvaluatorMap().containsKey(evaluatorName)) {
            evaluator = getEvaluatorMap().get(evaluatorName);
        } else {
            throw new InvalidArgumentException("No '" + permission + "' access evaluator for class type '" + canonicalClassName + "'");
        }

        return evaluator;
    }

}
