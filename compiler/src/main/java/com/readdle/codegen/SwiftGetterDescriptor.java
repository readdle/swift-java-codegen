package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftGetter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

class SwiftGetterDescriptor implements JavaSwiftProcessor.WritableElement {

    private String javaName;
    private String swiftName;

    private boolean isStatic;

    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;

    SwiftGetterDescriptor(ExecutableElement executableElement, SwiftGetter getterAnnotation) {
        this.javaName = executableElement.getSimpleName().toString();
        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);
        this.returnSwiftType = SwiftEnvironment.parseJavaType(executableElement.getReturnType().toString());
        this.isReturnTypeOptional = JavaSwiftProcessor.isNullable(executableElement);

        if (executableElement.getThrownTypes().size() != 0) {
            throw new SwiftMappingException("Getter can't throw", executableElement);
        }

        if (executableElement.getParameters().size() != 0) {
            throw new SwiftMappingException("Getter can't has parameters", executableElement);
        }

        if (getterAnnotation != null && !getterAnnotation.value().isEmpty()) {
            this.swiftName = getterAnnotation.value();
        }
        else {
            this.swiftName = javaName;
            if (swiftName.startsWith("get")) {
                swiftName = swiftName.substring(3);
            }
            char first = Character.toLowerCase(swiftName.charAt(0));
            swiftName = first + swiftName.substring(1);
        }
    }

    @Override
    public void generateCode(SwiftWriter swiftWriter, String javaFullName, String swiftType) throws IOException {
        String swiftFuncName = Utils.mangleFunctionName(javaFullName, javaName, new ArrayList<>());

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("@_silgen_name(\"%s\")", swiftFuncName));
        swiftWriter.emit(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, %s", swiftFuncName, isStatic ? "clazz: jclass" : "this: jobject"));
        swiftWriter.emit(String.format(")%s {\n", returnSwiftType != null ? " -> jobject?" : ""));
        swiftWriter.emitEmptyLine();

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("let swiftSelf: %s", swiftType));
            swiftWriter.emitStatement("do {");
            swiftWriter.emitStatement(String.format("swiftSelf = try %s.from(javaObject: this)", swiftType));
            swiftWriter.emitEndOfBlock();
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
            swiftWriter.emitStatement(String.format("return%s", returnSwiftType != null ? " nil" : ""));
            swiftWriter.emitEndOfBlock();
        }

        swiftWriter.emitStatement(String.format("let result = %s.%s", isStatic ? swiftType : "swiftSelf", swiftName));

        if (returnSwiftType != null) {
            swiftWriter.emitStatement("do {");
            if (isReturnTypeOptional) {
                swiftWriter.emitStatement("return try result?.javaObject()");
            }
            else {
                swiftWriter.emitStatement("return try result.javaObject()");
            }
            swiftWriter.emitEndOfBlock();
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
            swiftWriter.emitStatement("return nil");
            swiftWriter.emitEndOfBlock();
        }

        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();
    }

    @Override
    public String toString(String javaClassname) {
        return Utils.mangleFunctionName(javaClassname, javaName, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "SwiftGetterDescriptor{" +
                "javaName='" + javaName + '\'' +
                ", swiftName='" + swiftName + '\'' +
                ", isStatic=" + isStatic +
                ", returnSwiftType=" + returnSwiftType +
                ", isReturnTypeOptional=" + isReturnTypeOptional +
                '}';
    }
}
