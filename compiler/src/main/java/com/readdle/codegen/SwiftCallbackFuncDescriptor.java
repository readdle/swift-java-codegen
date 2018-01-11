package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftFunc;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

public class SwiftCallbackFuncDescriptor {

    private String name;

    private boolean isStatic;
    private boolean isThrown;

    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;

    private String description;

    private String sig;

    private List<SwiftParamDescriptor> params = new LinkedList<>();
    private List<String> paramNames = new LinkedList<>();

    SwiftCallbackFuncDescriptor(ExecutableElement executableElement) {
        this.name = executableElement.getSimpleName().toString();
        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);
        this.isThrown = executableElement.getThrownTypes() != null && executableElement.getThrownTypes().size() > 0;
        this.returnSwiftType = SwiftEnvironment.parseJavaType(executableElement.getReturnType().toString());
        this.isReturnTypeOptional = JavaSwiftProcessor.isNullable(executableElement);

        this.sig = "(";

        for (VariableElement variableElement : executableElement.getParameters()) {
            params.add(new SwiftParamDescriptor(variableElement));
            sig += javaClassToSig(variableElement.asType().toString());
        }

        sig += ")";

        if (returnSwiftType != null) {
            sig += javaClassToSig(executableElement.getReturnType().toString());
        }
        else {
            sig += "V";
        }

        SwiftCallbackFunc swiftFunc = executableElement.getAnnotation(SwiftCallbackFunc.class);

        if (swiftFunc != null && !swiftFunc.value().isEmpty()) {
            String funcFullName = swiftFunc.value();
            int paramStart = funcFullName.indexOf("(");
            int paramEnd = funcFullName.indexOf(")");
            if (paramStart > 0 && paramEnd > 0 && paramEnd > paramStart) {
                this.name = funcFullName.substring(0, paramStart);
                String arguments = funcFullName.substring(paramStart + 1, paramEnd);
                String[] paramNames = arguments.split(":");
                if (paramNames.length == params.size()) {
                    this.paramNames = Arrays.asList(paramNames);
                }
                else {
                    throw new IllegalArgumentException("Wrong count of arguments in func name");
                }
            }
            else {
                throw new IllegalArgumentException("Wrong func name");
            }
        }
        else {
            for (int i = 0; i < params.size(); i++) {
                paramNames.add("_");
            }
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
            String paramType = param.swiftType.swiftType + (param.isOptional ? "?" : "");
            if (i == 0) {
                swiftWriter.emit(paramNames.get(i) + " " + param.name + ": " + paramType);
            }
            else {
                swiftWriter.emit(", " + paramNames.get(i) + " " + param.name + ": " + paramType);
            }
        }

        if (returnSwiftType == null) {
            swiftWriter.emit(") {\n");
        }
        else {
            String returnParamType = isReturnTypeOptional ? returnSwiftType.swiftType + "?" : returnSwiftType.swiftType;
            swiftWriter.emit(String.format(") -> %s {\n", returnParamType));
        }

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("let java%s: JNIArgumentProtocol", param.name));
        }

        swiftWriter.emitStatement("do {");
        for (SwiftParamDescriptor param : params) {
            if (param.isOptional) {
                swiftWriter.emitStatement(String.format("if let %1$s = %1$s {", param.name));
                swiftWriter.emitStatement(String.format("java%1$s = try %1$s.javaObject()", param.name));
                swiftWriter.emitStatement("} else {");
                swiftWriter.emitStatement(String.format("java%s = jnull()", param.name));
                swiftWriter.emitStatement("}");
            }
            else {
                swiftWriter.emitStatement(String.format("java%s = try %s.javaObject()", param.name, param.name));
            }
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
            swiftWriter.emitStatement(String.format(", java%s", param.name));
        }

        if (returnSwiftType != null) {
            swiftWriter.emit(String.format(") else { %s }\n", isReturnTypeOptional ? "return nil" : "fatalError(\"Don't support nil here!\")"));
        }
        else {
            swiftWriter.emit(")\n");
        }

        swiftWriter.emitStatement("if let throwable = JNI.ExceptionCheck() {");
        if (isThrown) {
            swiftWriter.emitStatement("throw NSError(domain: \"JavaException\", code: 1)");
        }
        else {
            swiftWriter.emitStatement("fatalError(\"JavaException\")");
        }
        swiftWriter.emitStatement("}");

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

    private String javaClassToSig(String javaClass) {
        // First, remove all templates
        int templateStart = javaClass.indexOf("<");
        if (templateStart > 0) {
            javaClass = javaClass.substring(0, templateStart);
        }
        // Replace all dots with / in package name
        return "L" + javaClass.replace(".", "/") + ";";
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
