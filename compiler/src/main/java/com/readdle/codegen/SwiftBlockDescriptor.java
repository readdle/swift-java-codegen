package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftBlock;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.StandardLocation;

import static com.readdle.codegen.JavaSwiftProcessor.FOLDER;

class SwiftBlockDescriptor {

    private static final String SUFFIX = ".swift";
    private String swiftFilePath;

    private TypeElement annotatedClassElement;
    private String javaFullName;
    String simpleTypeName;
    private String swiftType;
    private String[] importPackages;

    private String blockSignature;

    private String funcName;
    private boolean isThrown;
    private SwiftEnvironment.Type returnSwiftType;
    private boolean isReturnTypeOptional;
    private String description;
    private String sig;
    private List<SwiftParamDescriptor> params = new LinkedList<>();

    SwiftBlockDescriptor(TypeElement classElement, Filer filer, String[] importPackages) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;
        this.importPackages = importPackages;

        // Get the full QualifiedTypeName
        try {
            SwiftBlock annotation = classElement.getAnnotation(SwiftBlock.class);
            blockSignature = annotation.value();
            simpleTypeName = classElement.getSimpleName().toString();
            javaFullName = classElement.getQualifiedName().toString().replace(".", "/");

        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            simpleTypeName = classTypeElement.getSimpleName().toString();
            javaFullName = classElement.getQualifiedName().toString().replace(".", "/");
        }

        swiftType = "SwiftBlock" + simpleTypeName;

        try {
            swiftFilePath = filer.createResource(StandardLocation.SOURCE_OUTPUT, FOLDER, swiftType + SUFFIX, classElement).toUri().getPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't create swift file");
        }

        Element enclosingElement = classElement.getEnclosingElement();
        while (enclosingElement != null && enclosingElement.getKind() == ElementKind.CLASS) {
            javaFullName = JavaSwiftProcessor.replaceLast(javaFullName, '/', '$');
            enclosingElement = enclosingElement.getEnclosingElement();
        }

        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                // Except init. We generate it's manually
                this.funcName = executableElement.getSimpleName().toString();
                this.isThrown = executableElement.getThrownTypes() != null && executableElement.getThrownTypes().size() > 0;
                this.returnSwiftType = SwiftEnvironment.parseJavaType(executableElement.getReturnType().toString());
                this.isReturnTypeOptional = JavaSwiftProcessor.isNullable(executableElement);

                this.sig = "(";

                for (VariableElement variableElement : executableElement.getParameters()) {
                    params.add(new SwiftParamDescriptor(variableElement));
                    sig += Utils.javaClassToSig(variableElement.asType().toString());
                }

                sig += ")";

                if (returnSwiftType != null) {
                    sig += Utils.javaClassToSig(executableElement.getReturnType().toString());
                }
                else {
                    sig += "V";
                }
                break;
            }
        }
    }

    File generateCode() throws IOException {
        File swiftExtensionFile = new File(swiftFilePath);
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);

        // Write imports
        swiftWriter.emitImports(importPackages);
        swiftWriter.emitEmptyLine();

        swiftWriter.emitStatement(String.format("fileprivate let javaClass = JNI.GlobalFindClass(\"%s\")!", javaFullName));

        swiftWriter.emitStatement(String.format("public typealias %s = %s", simpleTypeName, blockSignature));

        swiftWriter.emitEmptyLine();
        swiftWriter.emit(String.format("public class %s", swiftType));
        swiftWriter.emit(" {\n");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("let jniObject: jobject");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("public init(jniObject: jobject) {");
        // TODO: throw exception
        swiftWriter.emitStatement("self.jniObject = JNI.api.NewGlobalRef(JNI.env, jniObject)!");
        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("deinit {");
        swiftWriter.emitStatement("JNI.api.DeleteGlobalRef(JNI.env, jniObject)");
        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create swift object");
        swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
        swiftWriter.emitStatement(String.format("return %s(jniObject: javaObject).block", swiftType));
        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create java object with native pointer");
        swiftWriter.emitStatement("public func javaObject() throws -> jobject {");
        swiftWriter.emitStatement("return jniObject");
        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("static let javaMethod%1$s = try! JNI.getJavaMethod(forClass:\"%2$s\", method: \"%1$s\", sig: \"%3$s\")",
                funcName,
                javaFullName,
                sig));

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("public lazy var block: %s = {", simpleTypeName));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format("let java_%s: JNIArgumentProtocol", param.name));
        }

        if (params.size() > 0) {
            swiftWriter.emitStatement("do {");
            for (int i = 0; i < params.size(); i++) {
                SwiftParamDescriptor param = params.get(i);
                if (param.isOptional) {
                    swiftWriter.emitStatement(String.format("if let %s = $%s {", param.name, i + ""));
                    swiftWriter.emitStatement(String.format("java_%s = try %s.javaObject()", param.name, param.name));
                    swiftWriter.emitStatement("} else {");
                    swiftWriter.emitStatement(String.format("java_%s = jnull()", param.name));
                    swiftWriter.emitEndOfBlock();
                } else {
                    swiftWriter.emitStatement(String.format("java_%s = try $%s.javaObject()", param.name, i + ""));
                }
            }

            swiftWriter.emitEndOfBlock();
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            if (returnSwiftType == null) {
                swiftWriter.emitStatement("assert(false, errorString)");
                swiftWriter.emitStatement("return");
            } else {
                swiftWriter.emitStatement("fatalError(errorString)");
            }
            swiftWriter.emitEndOfBlock();
        }

        String jniMethodTemplate;
        if (returnSwiftType != null) {
            jniMethodTemplate = "guard let result = JNI.CallObjectMethod(self.jniObject, %s.javaMethod%s";
        }
        else {
            jniMethodTemplate = "JNI.CallVoidMethod(self.jniObject, %s.javaMethod%s";
        }

        swiftWriter.emit(String.format(jniMethodTemplate, swiftType, funcName));

        for (SwiftParamDescriptor param : params) {
            swiftWriter.emitStatement(String.format(", java_%s", param.name));
        }

        if (returnSwiftType != null) {
            swiftWriter.emit(String.format(") else { %s }\n", isReturnTypeOptional ? "return nil" : "fatalError(\"Don't support nil here!\")"));
        }
        else {
            swiftWriter.emit(")\n");
        }

        swiftWriter.emitStatement("if let throwable = JNI.ExceptionCheck() {");
        if (isThrown) {
            swiftWriter.emitStatement("if let error = try? NSError.from(javaObject: throwable) {");
            swiftWriter.emitStatement("throw error");
            swiftWriter.emitStatement("} else {");
            swiftWriter.emitStatement("fatalError(\"JavaException\")");
            swiftWriter.emitEndOfBlock();
        }
        else {
            swiftWriter.emitStatement("if let error = try? NSError.from(javaObject: throwable) {");
            swiftWriter.emitStatement("fatalError(\"JavaException: \\(error) \")");
            swiftWriter.emitStatement("} else {");
            swiftWriter.emitStatement("fatalError(\"JavaException\")");
            swiftWriter.emitEndOfBlock();
        }
        swiftWriter.emitEndOfBlock();

        if (returnSwiftType != null) {
            swiftWriter.emitStatement("do {");
            swiftWriter.emitStatement(String.format("return try %s.from(javaObject: result)", returnSwiftType.swiftConstructorType));
            swiftWriter.emitEndOfBlock();
            swiftWriter.emitStatement("catch {");
            swiftWriter.emitStatement("let errorString = String(reflecting: type(of: error)) + String(describing: error)");
            if (returnSwiftType == null) {
                swiftWriter.emitStatement("assert(false, errorString)");
                swiftWriter.emitStatement("return");
            }
            else {
                swiftWriter.emitStatement("fatalError(errorString)");
            }
            swiftWriter.emitEndOfBlock();
        }

        swiftWriter.emitEndOfBlock();

        swiftWriter.emitEmptyLine();

        swiftWriter.endExtension();

        swiftWriter.close();
        return swiftExtensionFile;
    }
}
