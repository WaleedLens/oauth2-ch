package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;

public class JsonHandler {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(JsonHandler.class);

    public static Object toObject(String json, Class<?> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Converting JSON to object of type: " + clazz.getName() + "   JSON: " + json);

            return objectMapper.readValue(json, clazz);


        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to object", e);
            throw new RuntimeException(e);
        }

    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            logger.info("Converting object to JSON: " + object.getClass().getName());
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error converting object to JSON", e);
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(Reader json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            logger.error("Error reading JSON tree", e);
            throw new RuntimeException(e);
        }
    }
}
