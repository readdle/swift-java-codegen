package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftBlock;

import javax.lang.model.element.VariableElement;

import static java.util.Objects.requireNonNull;

public class SwiftParamDescriptor {

    final String name;
    final SwiftEnvironment.Type swiftType;
    final boolean isOptional;

    SwiftParamDescriptor(VariableElement variableElement) {
        this.name = variableElement.getSimpleName().toString();
        this.swiftType = requireNonNull(SwiftEnvironment.parseJavaType(variableElement.asType().toString()));

        SwiftBlock swiftParam = variableElement.getAnnotation(SwiftBlock.class);
        if (swiftParam != null) {
            this.swiftType.swiftConstructorType = "SwiftBlock" + this.swiftType.swiftConstructorType;
        }
        this.isOptional = JavaSwiftProcessor.isNullable(variableElement);
    }

    @Override
    public String toString() {
        return "SwiftParamDescriptor{" +
                "name='" + name + '\'' +
                ", swiftType='" + swiftType + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }
}
