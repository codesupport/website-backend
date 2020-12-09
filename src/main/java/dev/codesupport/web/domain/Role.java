package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

/**
 * Used to store Role resource data
 */
@Data
public class Role implements IdentifiableDomain<Long> {

    private Long id;
    private String code;
    private String label;

}
