package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

@Data
public class Role implements IdentifiableDomain<Long> {

    private Long id;
    private String code;
    private String label;
    private Set<Permission> permission;

}
