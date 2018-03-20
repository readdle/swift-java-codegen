package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftSetter;

import java.io.IOException;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

class SwiftSetterDescriptor implements JavaSwiftProcessor.WritableElement {

    private String javaName;
    private String swiftName;

    private boolean isStatic;

    private SwiftParamDescriptor param;

    SwiftSetterDescriptor(ExecutableElement executableElement, SwiftSetter setterAnnotation) {
        this.javaName = executableElement.getSimpleName().toString();
        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);

        if (executableElement.getThrownTypes().size() != 0) {
            throw new IllegalArgumentException("Setter can't throw");
        }

        if (executableElement.getParameters().size() != 1) {
            throw new IllegalArgumentException("Setter should have exactly 1 parameter");
        }

        param = new SwiftParamDescriptor(executableElement.getParameters().get(0));

        if (setterAnnotation != null && !setterAnnotation.value().isEmpty()) {
            this.swiftName = setterAnnotation.value();
        }
        else {
            this.swiftName = javaName;
            if (swiftName.startsWith("set")) {
                swiftName = swiftName.substring(3);
            }
            char first = Character.toLowerCase(swiftName.charAt(0));
            swiftName = first + swiftName.substring(1);
        }
    }

    @Override
    public void generateCode(SwiftWriter swiftWriter, String javaFullName, String swiftType) throws IOException {
        String swiftFuncName = "Java_" + javaFullName.replace("/", "_").replace("$", "_00024") + "_" + javaName;

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("@_silgen_name(\"%s\")", swiftFuncName));
        swiftWriter.emit(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, %s", swiftFuncName, isStatic ? "clazz: jclass" : "this: jobject"));
        swiftWriter.emit(String.format(", j%s: jobject%s) {\n", param.name, param.isOptional ? "?" : ""));
        swiftWriter.emitEmptyLine();

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("let swiftSelf: %s", swiftType));
        }

        swiftWriter.emitStatement(String.format("let %s: %s%s", param.name, param.swiftType.swiftType, param.isOptional ? "?" : ""));

        swiftWriter.emitStatement("do {");

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("swiftSelf = try %s.from(javaObject: this)", swiftType));
        }

        if (param.isOptional) {
            swiftWriter.emitStatement(String.format("if let j%1$s = j%1$s {", param.name));
            swiftWriter.emitStatement(String.format("%1$s = try %2$s.from(javaObject: j%1$s)", param.name, param.swiftType.swiftConstructorType));
            swiftWriter.emitStatement("} else {");
            swiftWriter.emitStatement(String.format("%s = nil", param.name));
            swiftWriter.emitStatement("}");
        }
        else {
            swiftWriter.emitStatement(String.format("%1$s = try %2$s.from(javaObject: j%1$s)", param.name, param.swiftType.swiftConstructorType));
        }

        swiftWriter.emitStatement("}");
        swiftWriter.emitStatement("catch {");
        swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
        swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
        swiftWriter.emitStatement("return");
        swiftWriter.emitStatement("}");

        swiftWriter.emitStatement(String.format("%s.%s = %s", isStatic ? swiftType : "swiftSelf", swiftName, param.name));
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
    }

    @Override
    public String toString() {
        return "SwiftSetterDescriptor{" +
                "javaName='" + javaName + '\'' +
                ", swiftName='" + swiftName + '\'' +
                ", isStatic=" + isStatic +
                ", param=" + param +
                '}';
    }
}
