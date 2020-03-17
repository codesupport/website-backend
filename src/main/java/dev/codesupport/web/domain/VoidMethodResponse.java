package dev.codesupport.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoidMethodResponse {

    private String action;
    private int affectedEntities;

}