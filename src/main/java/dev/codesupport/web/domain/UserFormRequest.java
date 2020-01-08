package dev.codesupport.web.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserFormRequest {

    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String clearPassword;
    @NotEmpty
    private String email;
    private String avatarLink;

}
