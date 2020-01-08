package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.IdentifiableDomain;
import lombok.Data;

@Data
public class UserAward implements IdentifiableDomain<Long> {

    private Long id;
    private String code;
    private String label;
    private String description;

}
