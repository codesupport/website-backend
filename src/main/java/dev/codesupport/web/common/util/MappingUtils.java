package dev.codesupport.web.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Utilities for object transformations and JSON serialization/deserialization
 */
public class MappingUtils {

    /**
     * Single instance of transformation class for efficiency
     */
    private static ObjectMapper objectMapper;

    private MappingUtils() {

    }

    /**
     * @return Single instance of transformation class for efficiency
     */
    static ObjectMapper mapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        return objectMapper
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Converts a single object to a specified type.
     * <p></p>
     *
     * @param object The object to convert
     * @param clazz The destination type to convert to
     * @param <D> The destination type
     * @param <S> The source object type
     * @return a new instance of the object, converted to the specified type
     */
    public static <D, S> D convertToType(S object, Class<D> clazz) {
        return mapper()
                .convertValue(object, clazz);
    }

    /**
     * Converts a list of objects to a specified type.
     * <p></p>
     *
     * @param objectList The list of objects to convert
     * @param clazz The destination type to convert to
     * @param <D> The destination type
     * @param <S> The source object type
     * @return a new instance of the object list, with every element converted to the specified type
     */
    public static <D, S> List<D> convertToType(List<S> objectList, Class<D> clazz) {
        return mapper()
                .convertValue(objectList, mapper().getTypeFactory().constructCollectionLikeType(List.class, clazz));
    }

    /**
     * Converts a given json string to a provided type
     * <p></p>
     *
     * @param jsonString The json string to convert
     * @param clazz The destination type to convert to
     * @param <D> The destination type
     * @return a new instance of the deserialized object as the provided type
     * @throws IOException when the json object can not be deserialized
     */
    //WeakerAccess - just not being used, but might be
    @SuppressWarnings("WeakerAccess")
    public static <D extends Serializable> D convertFromJson(String jsonString, Class<D> clazz) throws IOException {
        return mapper()
                .readValue(jsonString, clazz);
    }

    /**
     * Converts a given json string list to a provided type
     * <p></p>
     *
     * @param jsonString The json string to convert
     * @param clazz The destination type to convert to
     * @param <D> The destination type
     * @return a new instance of the deserialized object list as the provided type
     * @throws IOException when the json object can not be deserialized
     */
    //WeakerAccess - just not being used, but might be
    @SuppressWarnings("WeakerAccess")
    public static <D extends Serializable> List<D> convertFromJsonList(String jsonString, Class<D> clazz) throws IOException {
        return mapper()
                .readValue(jsonString, mapper().getTypeFactory().constructCollectionLikeType(List.class, clazz));
    }
}
