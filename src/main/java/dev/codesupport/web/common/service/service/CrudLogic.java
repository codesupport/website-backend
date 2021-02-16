package dev.codesupport.web.common.service.service;

//S1610 - Default implementations are an abstract class concept
@SuppressWarnings("java:S1610")
public abstract class CrudLogic<E, D> {

    public void preCreateLogic(D d) {
        // Only used if overridden
    }

    public void preUpdateLogic(D d) {
        // Only used if overridden
    }

    public void preGetLogic(E e, D d) {
        // Only used if overridden
    }

    public void preSaveLogic(E e, D d) {
        // Only used if overridden
    }

}
