package org.example.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Make this annotation available at runtime
@Target(ElementType.TYPE) // This annotation is only applicable to classes (types)
public @interface TableEntity {
    String tableName() default ""; // You can add elements to annotations like this
}
