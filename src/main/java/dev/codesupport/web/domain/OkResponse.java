package dev.codesupport.web.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class OkResponse implements Serializable {

    //S1170 - Doesn't serialize correctly if made static.
    @SuppressWarnings({"squid:S1170"})
    private final String status = "ok";

}
