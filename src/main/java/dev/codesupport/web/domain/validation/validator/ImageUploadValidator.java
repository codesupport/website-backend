package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.web.domain.validation.annotation.ImageUploadConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Validation logic to be performed on properties annotated with {@link ImageUploadConstraint}
 */
public class ImageUploadValidator implements ConstraintValidator<ImageUploadConstraint, MultipartFile> {

    private Set<String> validTypes;

    @Override
    public void initialize(ImageUploadConstraint constraintAnnotation) {
        validTypes = Sets.newHashSet(constraintAnnotation.types());
    }

    /**
     * Validates the file type of the uploaded file.
     *
     * @param file The file that was uploaded
     * @param cxt  Reference to the validation context
     * @return True if the file is a valid type, False otherwise.
     */
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext cxt) {
        return validTypes.contains(file.getContentType());
    }

}
