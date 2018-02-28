package com.prperiscal.spring.data.compose;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pablo Rey Periscal
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataComposeResource {

    /**
     * <p>The value indicates the basic name or the full path to the resource for loading, which should conform the json
     * structure to load data into database.
     *
     * @return the basic name or full path.
     */
    String value() default "";

}
