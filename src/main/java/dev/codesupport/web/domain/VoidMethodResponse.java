package dev.codesupport.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class VoidMethodResponse implements Serializable {

    private String action;
    private int affectedEntities;

}