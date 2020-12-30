package dev.codesupport.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.codesupport.web.common.exception.InvalidArgumentException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContentType {

    JPG("image/jpeg", "jpg"),
    JPEG("image/jpeg", "jpeg"),
    GIF("image/gif", "gif"),
    PNG("image/png", "png"),
    SVG("image/svg+xml", "svg");
    
    private final String type;
    private final String extension;

    public static ContentType valueFrom(String value) {
        Optional<ContentType> optional = Stream.of(values())
                .filter(t -> t.type.equalsIgnoreCase(value) || t.extension.equalsIgnoreCase(value))
                .findFirst();

        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new InvalidArgumentException("Unsupported ContentType");
        }
    }

}
