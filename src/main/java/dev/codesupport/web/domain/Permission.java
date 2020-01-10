package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.IdentifiableDomain;
import lombok.Data;

/**
 * Used to store Permission resource data
 */
@Data
public class Permission implements IdentifiableDomain<Long> {

    private Long id;
    private String code;
    private String label;

}
