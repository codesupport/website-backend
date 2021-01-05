package dev.codesupport.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlParameter {

    private String name;
    private String type;
    private boolean optional;
    
}
