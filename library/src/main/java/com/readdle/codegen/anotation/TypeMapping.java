package com.readdle.codegen.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated Using SwiftModule for package makes incremental annotation processing unavailable.
 * Please use annotationProcessorOptions "com.readdle.codegen.package" instead
 */
@Deprecated
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PACKAGE)
public @interface TypeMapping {

    String swiftType();

    Class javaClass();

}
