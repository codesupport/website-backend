package dev.codesupport.web.common.service.data.entity;

import java.io.Serializable;

public interface IdentifiableEntity<T> extends Serializable {

    T getId();

}
