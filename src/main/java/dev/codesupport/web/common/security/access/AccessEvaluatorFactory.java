package dev.codesupport.web.common.security.access;

import dev.codesupport.web.common.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets the appropriate evaluator for a given access evaluation
 */
@Component
public class AccessEvaluatorFactory {

    private Map<String, AbstractAccessEvaluator<?>> evaluatorMap;
    private Map<String, AbstractAccessEvaluator<?>> stringEvaluatorMap;

    /**
     * Builds evaluator maps for all found AccessEvaluator components
     *
     * @param context The spring application context
     */
    @Autowired
    public AccessEvaluatorFactory(ApplicationContext context) {
        evaluatorMap = new HashMap<>();
        stringEvaluatorMap = new HashMap<>();

        Map<String, AbstractAccessEvaluator> beanMap = context.getBeansOfType(AbstractAccessEvaluator.class);

        for (AbstractAccessEvaluator<?> evaluator : beanMap.values()) {
            if (evaluator.getEvaluatorClassType().equals(String.class.getCanonicalName())){
                stringEvaluatorMap.put(evaluator.getAccessor().toString().toLowerCase(), evaluator);
            } else {
                evaluatorMap.put(evaluator.getEvaluatorClassType(), evaluator);
            }
        }
    }

    /**
     * Get evaluator that matches type of targetDomainObject
     *
     * @param targetDomainObject Object related to access evaluation
     * @return Evaluator that evaluates the type of the given targetDomainObject
     */
    public AbstractAccessEvaluator<?> getEvaluator(Object targetDomainObject) {
        String evaluatorName = targetDomainObject.getClass().getCanonicalName();

        AbstractAccessEvaluator<?> evaluator;

        if (targetDomainObject instanceof String) {
            String accessorName = (String)targetDomainObject;
            if (stringEvaluatorMap.containsKey(accessorName)) {
                evaluator = stringEvaluatorMap.get(accessorName);
            } else {
                throw new InvalidArgumentException("No access evaluator for class type '" + accessorName + "'");
            }
        } else {
            evaluator = getEvaluatorByName(evaluatorName);
        }

        return evaluator;
    }

    /**
     * Get evaluator that matches the canonicalClassName string
     *
     * @param canonicalClassName Canonical class name of class associated with access evaluation
     * @return Evaluator that evaluates the given canonical class name
     */
    public AbstractAccessEvaluator<?> getEvaluatorByName(String canonicalClassName) {
        AbstractAccessEvaluator<?> evaluator;

        if (evaluatorMap.containsKey(canonicalClassName)){
            evaluator = evaluatorMap.get(canonicalClassName);
        } else {
            throw new InvalidArgumentException("No access evaluator for class type '" + canonicalClassName + "'");
        }

        return evaluator;
    }

}
