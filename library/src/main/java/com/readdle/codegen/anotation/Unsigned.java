package com.readdle.codegen.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD}) @Retention(RetentionPolicy.CLASS)
public @interface Unsigned {

    String value() default "";
}
