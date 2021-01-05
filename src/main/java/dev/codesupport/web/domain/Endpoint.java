package dev.codesupport.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {

    private String name;
    private int version;
    private String method;
    private String uri;
    private String returns;
    private boolean expectsBody;
    private Set<UrlParameter> urlParameters;

}
