package dev.codesupport.web.common.service.http.client;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.InternalServiceException;
import dev.codesupport.web.common.util.MappingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts an object into a URL encoded string.
 * <p>This is pulled in by Spring's RestTemplate and added to it's list of message converters which it
 * goes through each time looking for what it needs by using the canRead() and canWrite() methods</p>
 */
@Slf4j
public class ObjectToUrlEncodedConverter implements HttpMessageConverter<Object> {

    /**
     * This is stubbed to always be false, as it's not currently a needed feature.
     * <p>Determines if the particular media type can be read and transformed into the given class type</p>
     *
     * @param clazz     The class type we wish to transform to.
     * @param mediaType The media type of the message body we wish to transform.
     * @return True if the transformation is possible, False otherwise.
     */
    @Override
    public boolean canRead(Class clazz, MediaType mediaType) {
        return false;
    }

    /**
     * Only supports one media type for this class, to keep it simple.
     * <p>Determines if the particular class can be transformed into the given media type.</p>
     *
     * @param clazz     The class type to be written from
     * @param mediaType The mediaType to be written to
     * @return True if the class type can be written to the media type, False otherwise
     */
    @Override
    public boolean canWrite(Class clazz, MediaType mediaType) {
        return getSupportedMediaTypes().contains(mediaType);
    }

    /**
     * The list of supported media types, only supports x-www-form-urlencoded.
     *
     * @return The list of supported media types.
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * Not implemented, not needed, required to define.
     * <p>Reads the input message and transforms it into an object of the given class type.</p>
     *
     * @param clazz        The class type to transform into.
     * @param inputMessage The input message to transform.
     * @return An object of the given class type, populated with data from the input message.
     * @throws HttpMessageNotReadableException If the message is otherwise unreadable.
     * @throws NotImplementedException         This method is not implemented, as it is not needed.
     */
    @Override
    public Object read(Class clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new NotImplementedException("Read not implemented");
    }

    /**
     * Always assumes x-www-form-urlencoded, as it's the only thing canWrite() returns true for.
     * <p>Writes the given object to the desired content type and stores it in the HttpOutputMessage</p>
     *
     * @param o             The object to transform.
     * @param contentType   The media type to transform the object to.
     * @param outputMessage The output message to write the transformed object to.
     * @throws HttpMessageNotWritableException If the message can not be written to.
     * @throws InternalServiceException        If their was a failure writing to the message body.
     */
    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {
        String body = MappingUtils
                .convertToType(o, UrlEncodedWriter.class)
                .toString();

        try {
            outputMessage.getBody().write(body.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Failure writing to request body.", e);
            throw new InternalServiceException(InternalServiceException.Reason.INTERNAL, e);
        }
    }

    /**
     * A private class used for the object to media type transformations.
     * <p>Makes use of the @JsonAnySetter annotation to map properties with ObjectMapper</p>
     */
    @VisibleForTesting
    static class UrlEncodedWriter {
        private final Map<String, Object> properties = new HashMap<>();

        /**
         * The @JsonAnySetter causes every property transformation to use this setter.
         * <p>Sets the parameter and it's value, to be used in generating the urlencoded string.</p>
         *
         * @param name  The name of the parameter
         * @param value The value of the parameter
         */
        @JsonAnySetter
        public void set(String name, Object value) {
            properties.put(name, value);
        }

        /**
         * Builds the urlencoded string from the list of previously saved parameters.
         *
         * @return A string representing the urlencoded parameters.
         */
        @Override
        public String toString() {
            // Encoding to use.
            String encoding = StandardCharsets.UTF_8.toString();
            List<String> urlProperties = new ArrayList<>();

            // Try block to catch unsupported encoding issues (should never happen)
            try {
                // Url encodes every property / parameter.
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    String name = URLEncoder.encode(entry.getKey(), encoding);
                    String value;
                    if (entry.getValue() != null) {
                        value = URLEncoder.encode(entry.getValue().toString(), encoding);
                    } else {
                        value = "";
                    }

                    urlProperties.add(name + "=" + value);
                }
            } catch (UnsupportedEncodingException e) {
                throw new InternalServiceException(InternalServiceException.Reason.INTERNAL, e);
            }
            // Join the list of encoded parameters with & (like url parameters)
            return String.join("&", urlProperties);
        }
    }
}