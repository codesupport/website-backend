package dev.codesupport.web.common.service.service;

public interface CrudLogic<E, D> {

    void preGetLogic(E e, D d);

    void preSaveLogic(E e, D d);

}
