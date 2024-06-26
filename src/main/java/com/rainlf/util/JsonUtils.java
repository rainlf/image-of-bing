package com.rainlf.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author : rain
 * @date : 2020/6/9 16:14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String parseObject(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Parse object error: " + e.getMessage(), e);
        }
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Parse json error: " + e.getMessage(), e);
        }
    }

    public static <T> List<T> parseJsonList(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (IOException e) {
            throw new IllegalArgumentException("Parse json error: " + e.getMessage(), e);
        }
    }
}
