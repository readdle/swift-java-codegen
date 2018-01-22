package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftParam;

import javax.lang.model.element.VariableElement;

public class SwiftParamDescriptor {

    final String name;
    SwiftEnvironment.Type swiftType;
    final boolean isOptional;
    final String description;

    SwiftParamDescriptor(VariableElement variableElement) {
        this.name = variableElement.getSimpleName().toString();
        this.swiftType = SwiftEnvironment.parseJavaType(variableElement.asType().toString());
        SwiftParam swiftParam = variableElement.getAnnotation(SwiftParam.class);
        if (swiftParam != null) {
            this.swiftType.swiftType = swiftParam.value();
        }
        this.isOptional = JavaSwiftProcessor.isNullable(variableElement);
        this.description = null;
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
