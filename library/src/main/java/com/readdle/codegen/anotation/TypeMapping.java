package com.readdle.codegen.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PACKAGE)
public @interface TypeMapping {

    String swiftType();

    Class javaClass();

}
