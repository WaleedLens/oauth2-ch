package org.example.utils;

import org.apache.logging.log4j.Logger;
import org.example.annotations.TableEntity;
import org.reflections.Reflections;

import java.util.Set;

public class ClassFinder {
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(ClassFinder.class);

    public static String findClassName(String simpleClassName) {
        // Scan the entire classpath (you might want to restrict this)
        Reflections reflections = new Reflections("");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(TableEntity.class); // or use getAllTypes() to fetch all classes

        for (Class<?> clazz : classes) {
            if (clazz.getSimpleName().equals(simpleClassName)) {
                logger.info("Found class: " + clazz.getName());
                return clazz.getName();
            }
        }
        return null;
    }
}

