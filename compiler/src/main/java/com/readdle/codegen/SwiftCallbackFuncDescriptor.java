package com.readdle.codegen;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

public class SwiftCallbackFuncDescriptor {

    @NonNull
    private String name;

    private boolean isStatic;
    private boolean isThrown;

    @Nullable
    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;

    @Nullable
    private String description;

    private String sig;

    private List<SwiftParamDescriptor> params = new LinkedList<>();

    SwiftCallbackFuncDescriptor(ExecutableElement executableElement) {
        this.name = executableElement.getSimpleName().toString();
        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);
        this.isThrown = executableElement.getThrownTypes() != null && executableElement.getThrownTypes().size() > 0;
        this.returnSwiftType = SwiftEnvironment.parseJavaType(executableElement.getReturnType().toString());
        this.isReturnTypeOptional = executableElement.getAnnotation(NonNull.class) == null;

        this.sig = "(";

        for (VariableElement variableElement : executableElement.getParameters()) {
            params.add(new SwiftParamDescriptor(variableElement));
            sig += "L" + variableElement.asType().toString().replace(".", "/") + ";";
        }

        sig += ")";

        if (returnSwiftType != null) {
            sig += "L" + executableElement.getReturnType().toString().replace(".", "/") + ";";
        }
        else {
            sig += "V";
        }
    }

    void generateCode(SwiftWriter swiftWriter, String javaPackage, String swiftType) throws IOException {

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("static let javaMethod%1$s = try! JNI.%5$s(forClass:\"%2$s/%3$s\", method: \"%1$s\", sig: \"%4$s\")",
                name,
                javaPackage.replace(".", "/"),
                swiftType,
                sig,
                isStatic ? "getStaticJavaMethod" : "getJavaMethod"));

        swiftWriter.emitEmptyLine();
        swiftWriter.emit(String.format("public %s func %s(", isStatic ? "static" : "", name));
        for (int i = 0; i < params.size(); i++) {
            SwiftParamDescriptor param = params.get(i);
            String paramName = param.paramName != null ? param.paramName : "_";
            if (i == 0) {
                swiftWriter.emit(paramName + " " + param.name + ": " + param.swiftType.swiftType);
            }
            else {
                swiftWriter.emit(", " + paramName + " " + param.name + ": " + param.swiftType.swiftType);
            }
        }
        swiftWriter.emit(String.format(")%s {\n", returnSwiftType != null ? " -> " + returnSwiftType.swiftType : ""));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("let j%s: jobject", param.name));
        }

        swiftWriter.emitStatement("do {");
        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("j%s = try %s.javaObject()", param.name, param.name));
        }

        swiftWriter.emitStatement("}");
        swiftWriter.emitStatement("catch {");
        swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
        if (returnSwiftType == null) {
            swiftWriter.emitStatement("assert(false, errorString)");
            swiftWriter.emitStatement("return");
        }
        else {
            swiftWriter.emitStatement("fatalError(errorString)");
        }
        swiftWriter.emitStatement("}");

        String jniMethodTemplate;
        if (returnSwiftType != null) {
            if (!isStatic) {
                jniMethodTemplate = "guard let result = JNI.CallObjectMethod(jniObject, methodID: %s.javaMethod%s";
            }
            else {
                jniMethodTemplate = "guard let result = JNI.CallStaticObjectMethod(javaClass, methodID: %s.javaMethod%s";
            }
        }
        else {
            if (!isStatic) {
                jniMethodTemplate = "JNI.CallVoidMethod(jniObject, %s.javaMethod%s";
            }
            else {
                jniMethodTemplate = "JNI.CallStaticVoidMethod(javaClass, %s.javaMethod%s";
            }
        }

        swiftWriter.emit(String.format(jniMethodTemplate, swiftType, name));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format(", j%s", param.name));
        }

        if (returnSwiftType != null) {
            swiftWriter.emit(String.format(") else { %s }\n", isReturnTypeOptional ? "return nil" : "fatalError(\"Don't support nil here!\")"));
        }
        else {
            swiftWriter.emit(")\n");
        }

        if (returnSwiftType != null) {
            swiftWriter.emitStatement("do {");
            swiftWriter.emitStatement(String.format("return try %s.from(javaObject: result)", returnSwiftType.swiftType));
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            if (returnSwiftType == null) {
                swiftWriter.emitStatement("assert(false, errorString)");
                swiftWriter.emitStatement("return");
            }
            else {
                swiftWriter.emitStatement("fatalError(errorString)");
            }
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
