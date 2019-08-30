package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftFunc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

class SwiftFuncDescriptor implements JavaSwiftProcessor.WritableElement {

    private String javaMethodName;
    private String swiftMethodName;

    private boolean isStatic;
    private boolean isThrown;

    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;

    private List<SwiftParamDescriptor> params;
    private List<String> paramNames;

    SwiftFuncDescriptor(ExecutableElement executableElement, JavaSwiftProcessor processor) {
        String elementName = executableElement.getSimpleName().toString();
        this.javaMethodName = elementName;
        this.swiftMethodName = elementName;

        this.isStatic = executableElement.getModifiers().contains(Modifier.STATIC);
        this.isThrown = executableElement.getThrownTypes() != null && executableElement.getThrownTypes().size() > 0;
        this.returnSwiftType = processor.parseJavaType(executableElement.getReturnType().toString());
        this.isReturnTypeOptional = JavaSwiftProcessor.isNullable(executableElement);

        int paramsSize = executableElement.getParameters().size();
        this.params = new ArrayList<>(paramsSize);
        this.paramNames = new ArrayList<>(paramsSize);

        for (VariableElement variableElement : executableElement.getParameters()) {
            params.add(new SwiftParamDescriptor(variableElement, processor));
        }

        SwiftFunc swiftFunc = executableElement.getAnnotation(SwiftFunc.class);

        if (swiftFunc != null && !swiftFunc.value().isEmpty()) {
            String funcFullName = swiftFunc.value();
            int paramStart = funcFullName.indexOf("(");
            int paramEnd = funcFullName.indexOf(")");

            if (paramStart <= 0 || paramEnd <= 0 || paramEnd <= paramStart) {
                throw new SwiftMappingException("Wrong func name", executableElement);
            }

            this.swiftMethodName = funcFullName.substring(0, paramStart);

            String arguments = funcFullName.substring(paramStart + 1, paramEnd);

            String[] paramNames;
            if (!arguments.isEmpty()) {
                paramNames = arguments.split(":");
            }
            else {
                paramNames = new String[0];
            }

            if (paramNames.length != params.size()) {
                throw new SwiftMappingException("Wrong count of arguments in func name " + paramNames.length + " !=  "  + params.size(), executableElement);
            }

            for (String paramName : paramNames) {
                this.paramNames.add(paramName + ": ");
            }
        }
        else {
            paramNames = Collections.nCopies(params.size(), "");
        }
    }

    @Override
    public void generateCode(SwiftWriter swiftWriter, String javaFullName, String swiftType) throws IOException {
        String swiftFuncName = Utils.mangleFunctionName(javaFullName, javaMethodName, params);

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("@_silgen_name(\"%s\")", swiftFuncName));
        swiftWriter.emit(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, %s", swiftFuncName, isStatic ? "clazz: jclass" : "this: jobject"));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emit(String.format(", j%s: jobject%s", param.name, param.isOptional ? "?" : ""));
        }

        swiftWriter.emit(String.format(")%s {\n", returnSwiftType != null ? " -> jobject?" : ""));
        swiftWriter.emitEmptyLine();

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("let swiftSelf: %s", swiftType));
        }

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("let %s: %s%s", param.name, param.swiftType.swiftType, param.isOptional ? "?" : ""));
        }

        boolean shouldCatchPreamble = params.size() > 0 || !isStatic;
        if (shouldCatchPreamble) {
            swiftWriter.emitStatement("do {");
        }

        if (!isStatic) {
            swiftWriter.emitStatement(String.format("swiftSelf = try %s.from(javaObject: this)", swiftType));
        }

        for (SwiftParamDescriptor param : params) {
            if (param.isOptional) {
                swiftWriter.emitStatement(String.format("if let j%1$s = j%1$s {", param.name));
                swiftWriter.emitStatement(String.format("%1$s = try %2$s.from(javaObject: j%1$s)", param.name, param.swiftType.swiftConstructorType));
                swiftWriter.emitStatement("}");
                swiftWriter.emitStatement("else {");
                swiftWriter.emitStatement(String.format("%s = nil", param.name));
                swiftWriter.emitStatement("}");
            }
            else {
                swiftWriter.emitStatement(String.format("%1$s = try %2$s.from(javaObject: j%1$s)", param.name, param.swiftType.swiftConstructorType));
            }
        }

        if (shouldCatchPreamble) {
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let nsError = error as NSError");
            swiftWriter.emitStatement("let errorString = \"\\(nsError.domain): \\(nsError.code)\"");
            swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
            swiftWriter.emitStatement(String.format("return%s", returnSwiftType != null ? " nil" : ""));
            swiftWriter.emitStatement("}");
        }

        if (isThrown) {
            swiftWriter.emitStatement("do {");
        }

        String swiftMethodName = this.swiftMethodName;
        if (isStatic && swiftMethodName.equals("init")) {
            swiftMethodName = "";
        }

        swiftWriter.emit(String.format("%s%s%s%s(",
                returnSwiftType != null ? "let result = " : "",
                isThrown ? "try " : "",
                isStatic ? swiftType : "swiftSelf",
                swiftMethodName.length() > 0 ? "." + swiftMethodName : ""));

        for (int i = 0; i < params.size(); i++) {
            SwiftParamDescriptor param = params.get(i);
            if (i == 0) {
                swiftWriter.emit(paramNames.get(i) + param.name);
            }
            else {
                swiftWriter.emit(", " + paramNames.get(i) + param.name);
            }
        }
        swiftWriter.emit(")\n");

        if (swiftMethodName.equals("release")) {
            swiftWriter.emitStatement("JNI.api.SetLongField(JNI.env, this, javaSwiftPointerFiled, 0)");
        }

        if (returnSwiftType != null) {
            swiftWriter.emitStatement("do {");
            if (isReturnTypeOptional) {
                swiftWriter.emitStatement("return try result?.javaObject()");
            }
            else {
                swiftWriter.emitStatement("return try result.javaObject()");
            }
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let nsError = error as NSError");
            swiftWriter.emitStatement("let errorString = \"\\(nsError.domain): \\(nsError.code)\"");
            swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftRuntimeErrorClass, errorString)");
            swiftWriter.emitStatement("return nil");
            swiftWriter.emitStatement("}");
        }

        if (isThrown) {
            swiftWriter.emitStatement("}");
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let nsError = error as NSError");
            swiftWriter.emitStatement("let errorString = \"\\(nsError.domain): \\(nsError.code)\"");
            swiftWriter.emitStatement("_ = JNI.api.ThrowNew(JNI.env, SwiftErrorClass, errorString)");
            swiftWriter.emitStatement(String.format("return%s", returnSwiftType != null ? " nil" : ""));
            swiftWriter.emitStatement("}");
        }

        swiftWriter.emitStatement("}");
    }

    @Override
    public String toString(String javaClassname) {
        return Utils.mangleFunctionName(javaClassname, javaMethodName, params);
    }

    @Override
    public String toString() {
        return "SwiftFuncDescriptor{" +
                "javaMethodName='" + javaMethodName + '\'' +
                ", swiftMethodName='" + swiftMethodName + '\'' +
                ", isStatic=" + isStatic +
                ", isThrown=" + isThrown +
                ", returnSwiftType='" + returnSwiftType + '\'' +
                ", isReturnTypeOptional=" + isReturnTypeOptional +
                ", params=" + params +
                '}';
    }
}
