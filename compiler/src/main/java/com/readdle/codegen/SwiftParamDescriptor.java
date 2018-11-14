package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftBlock;
import com.readdle.codegen.anotation.SwiftModule;

import java.util.HashMap;

import javax.lang.model.element.VariableElement;

import static java.util.Objects.requireNonNull;

public class SwiftParamDescriptor {

    final String name;
    final SwiftEnvironment.Type swiftType;
    final boolean isOptional;

    SwiftParamDescriptor(VariableElement variableElement, JavaSwiftProcessor processor) {
        this.name = variableElement.getSimpleName().toString();
        this.swiftType = requireNonNull(processor.parseJavaType(Utils.typeToString(variableElement.asType())));

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
