package dev.codesupport.web.common.domain;

import java.io.Serializable;

public interface IdentifiableDomain<I> extends Serializable {

    I getId();

}
