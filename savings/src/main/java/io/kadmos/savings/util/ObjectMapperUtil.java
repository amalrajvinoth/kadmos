package io.kadmos.savings.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

import io.kadmos.savings.exception.ObjectMapperException;
import lombok.extern.slf4j.Slf4j;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Slf4j
public final class ObjectMapperUtil {

    private static ObjectMapper objectMapper;

    private ObjectMapperUtil() {
    }

    public static String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("toJson: Unable to convert to JSON: " + e.getMessage(), e);
            throw new ObjectMapperException(e.getMessage());
        }
    }

    public static <T> T readValue(String json, Class<T> tClass) {
        try {
            return getObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            log.error("readValue: Error converting Json String to object. Json: {}, toClass: {}", json,
                    tClass.getName());
            log.error("Json conversion error", e);
            throw new ObjectMapperException("Invalid Json string.");
        }
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }
}
