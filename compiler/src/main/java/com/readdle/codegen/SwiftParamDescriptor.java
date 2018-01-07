package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftParamName;

import android.support.annotation.NonNull;

import javax.lang.model.element.VariableElement;

public class SwiftParamDescriptor {

    final String name;
    final String paramName;
    final SwiftEnvironment.Type swiftType;
    final boolean isOptional;
    final String description;

    SwiftParamDescriptor(VariableElement variableElement) {
        this.name = variableElement.getSimpleName().toString();
        this.swiftType = SwiftEnvironment.parseJavaType(variableElement.asType().toString());
        this.isOptional = variableElement.getAnnotation(NonNull.class) == null;
        this.description = null;

        SwiftParamName swiftParamName = variableElement.getAnnotation(SwiftParamName.class);
        if (swiftParamName != null) {
            if (swiftParamName.name().isEmpty()) {
                paramName = name;
            }
            else {
                paramName = swiftParamName.name();
            }
        }
        else {
            paramName = null;
        }
    }

    @Override
    public String toString() {
        return "SwiftParamDescriptor{" +
                "name='" + name + '\'' +
                ", swiftType='" + swiftType + '\'' +
                ", isOptional=" + isOptional +
                ", description='" + description + '\'' +
                '}';
    }
}
