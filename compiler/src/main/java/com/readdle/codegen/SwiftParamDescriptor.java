package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftBlock;
import javax.lang.model.element.VariableElement;

import static java.util.Objects.requireNonNull;

public class SwiftParamDescriptor {

    final String name;
    final SwiftEnvironment.Type swiftType;
    final boolean isOptional;

    SwiftParamDescriptor(VariableElement variableElement, JavaSwiftProcessor processor) {
        this.name = variableElement.getSimpleName().toString();
        boolean isUnsigned = processor.isUnsigned(variableElement);
        if (isUnsigned) {
            this.swiftType = requireNonNull(processor.parseJavaType(Utils.typeToString(variableElement.asType()))).makeUnsigned();
        }
        else {
            this.swiftType = requireNonNull(processor.parseJavaType(Utils.typeToString(variableElement.asType())));
        }

        SwiftBlock swiftParam = variableElement.getAnnotation(SwiftBlock.class);
        if (swiftParam != null) {
            this.swiftType.swiftConstructorType = "SwiftBlock" + this.swiftType.swiftConstructorType;
        }
        this.isOptional = processor.isNullable(variableElement);
    }

    @Override
    public String toString() {
        return "SwiftParamDescriptor{" +
                "name='" + name + '\'' +
                ", swiftType='" + swiftType + '\'' +
                ", isOptional=" + isOptional +
                '}';
    }

    public boolean isPrimitive() {
        if (isOptional) {
            return false;
        }
        else {
            return swiftType.isPrimitiveType();
        }
    }
}
