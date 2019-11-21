package dev.codesupport.web.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MappingUtils {

    private static ObjectMapper objectMapper;

    private MappingUtils() {

    }

    static ObjectMapper mapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        return objectMapper
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static <D, S> D convertToType(S object, Class<D> clazz) {
        return mapper()
                .convertValue(object, clazz);
    }

    public static <D, S> List<D> convertToType(List<S> objectList, Class<D> clazz) {
        return mapper()
                .convertValue(objectList, mapper().getTypeFactory().constructCollectionLikeType(List.class, clazz));
    }

    //WeakerAccess - just not being used, but might be
    @SuppressWarnings("WeakerAccess")
    public static <D extends Serializable> D convertFromJson(String object, Class<D> clazz) throws IOException {
        return mapper()
                .readValue(object, clazz);
    }

    //WeakerAccess - just not being used, but might be
    @SuppressWarnings("WeakerAccess")
    public static <D extends Serializable> List<D> convertFromJsonList(String object, Class<D> clazz) throws IOException {
        return mapper()
                .readValue(object, mapper().getTypeFactory().constructCollectionLikeType(List.class, clazz));
    }
}
