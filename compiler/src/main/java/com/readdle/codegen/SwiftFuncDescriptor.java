package com.readdle.codegen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

class SwiftFuncDescriptor {

    @NonNull
    String name;

    boolean isStatic;
    boolean isThrown;

    @Nullable
    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;

    @Nullable
    private String description;

    private List<SwiftParamDescriptor> params = new LinkedList<>();

    SwiftFuncDescriptor(ExecutableElement executableElement) {
        this.name = executableElement.getSimpleName().toString();
        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);
        this.isThrown = executableElement.getThrownTypes() != null && executableElement.getThrownTypes().size() > 0;
        this.returnSwiftType = SwiftEnvironment.parseJavaType(executableElement.getReturnType().toString());
        this.isReturnTypeOptional = executableElement.getAnnotation(NonNull.class) == null;

        for (VariableElement variableElement : executableElement.getParameters()) {
            params.add(new SwiftParamDescriptor(variableElement));
        }
    }

    void generateCode(SwiftWriter swiftWriter, String javaPackage, String swiftType) throws IOException {
        String swiftFuncName = "Java_" + javaPackage.replace(".", "_") + "_" + swiftType + "_" + name;

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("@_silgen_name(\"%s\")", swiftFuncName));
        swiftWriter.emit(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, %s", swiftFuncName, isStatic ? "clazz: jclass" : "this: jobject"));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emit(", j" + param.name + ": jobject");
        }

        swiftWriter.emit(String.format(")%s {\n", returnSwiftType != null ? " -> jobject?" : ""));
        swiftWriter.emitEmptyLine();

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("let swiftSelf: %s", swiftType));
        }

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("let %s: %s", param.name, param.swiftType.swiftType));
        }

        swiftWriter.emitStatement("do {");

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("swiftSelf = try %s.from(javaObject: this)", swiftType));
        }

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("%1$s = try %2$s.from(javaObject: j%1$s)", param.name, param.swiftType.swiftType));
        }
        swiftWriter.emitStatement("}");
        swiftWriter.emitStatement("catch {");
        swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
        swiftWriter.emitStatement("JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
        swiftWriter.emitStatement(String.format("return%s", returnSwiftType != null ? " nil" : ""));
        swiftWriter.emitStatement("}");

        if (isThrown) {
            swiftWriter.emitStatement("do {");
        }

        swiftWriter.emit(String.format("%s%s%s.%s(",
                returnSwiftType != null ? "let result = " : "",
                isThrown ? "try " : "",
                isStatic ? swiftType : "swiftSelf",
                name));

        for (int i = 0; i < params.size(); i++) {
            SwiftParamDescriptor param = params.get(i);
            String paramName = param.paramName != null ? param.paramName + ": " : "";
            if (i == 0) {
                swiftWriter.emit(paramName + param.name);
            }
            else {
                swiftWriter.emit(", " + paramName + param.name);
            }
        }
        swiftWriter.emit(")\n");

        if (isThrown) {
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            swiftWriter.emitStatement("JNI.api.ThrowNew(JNI.env, SwiftErrorClass, errorString)");
            swiftWriter.emitStatement(String.format("return%s", returnSwiftType != null ? " nil" : ""));
            swiftWriter.emitStatement("}");
        }

        if (returnSwiftType != null) {
            swiftWriter.emitStatement("do {");
            swiftWriter.emitStatement("return try result.javaObject()");
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            swiftWriter.emitStatement("JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
            swiftWriter.emitStatement("return nil");
            swiftWriter.emitStatement("}");
        }

        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
    }

    @Override
    public String toString() {
        return "SwiftFuncDescriptor{" +
                "name='" + name + '\'' +
                ", isStatic=" + isStatic +
                ", isThrown=" + isThrown +
                ", returnSwiftType='" + returnSwiftType + '\'' +
                ", isReturnTypeOptional=" + isReturnTypeOptional +
                ", description='" + description + '\'' +
                ", params=" + params +
                '}';
    }
}
