package dev.codesupport.web.domain;

import lombok.Data;
import dev.codesupport.web.api.data.entity.UserEntity_;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.domain.Validatable;
import dev.codesupport.web.common.util.ValidationUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class User implements Validatable<Long> {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String avatarLink;
    private Long addedBy;
    private Long joinDate;

    @Override
    public List<ValidationIssue> validate() {
        List<ValidationIssue> validationIssues = new ArrayList<>();

        String idString = (id == null ? null : Long.toString(id));

        if (StringUtils.isEmpty(username)) {
            validationIssues.add(createMissingParameter(idString, UserEntity_.USERNAME));
        } else if (!ValidationUtils.isValidUsername(username)) {
            validationIssues.add(createInvalidParameter(idString, UserEntity_.USERNAME, "Must be 4-15 Alphanumeric figures"));
        }
        if (StringUtils.isEmpty(password)) {
            validationIssues.add(createMissingParameter(idString, UserEntity_.PASSWORD));
        } else if (!ValidationUtils.isValidPassword(password)) {
            validationIssues.add(createInvalidParameter(idString, UserEntity_.PASSWORD, "Must be >10 Alphanumeric figures"));
        }
        if (StringUtils.isEmpty(email)) {
            validationIssues.add(createMissingParameter(idString, UserEntity_.EMAIL));
        } else if (!ValidationUtils.isValidEmail(email)) {
            validationIssues.add(createInvalidParameter(idString, UserEntity_.EMAIL, "Must be valid email"));
        }
        if (ObjectUtils.defaultIfNull(addedBy, 0L) <= 0) {
            validationIssues.add(createMissingParameter(idString, UserEntity_.ADDED_BY));
        }

        return validationIssues;
    }
}
