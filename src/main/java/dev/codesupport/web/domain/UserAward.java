package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

/**
 * Used for gathering User Award resource data
 */
@Data
public class UserAward implements IdentifiableDomain<Long> {

    private Long id;
    private String code;
    private String label;
    private String description;

}
