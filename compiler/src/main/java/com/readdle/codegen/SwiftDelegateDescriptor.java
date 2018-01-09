package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftDelegate;
import com.readdle.codegen.anotation.SwiftFunc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class SwiftDelegateDescriptor {

    static final String SUFFIX = "Android.swift";

    private TypeElement annotatedClassElement;
    private String javaPackage;
    String simpleTypeName;
    private String[] importPackages;
    String pointerBasicTypeSig;

    List<SwiftFuncDescriptor> functions = new LinkedList<>();
    List<SwiftCallbackFuncDescriptor> callbackFunctions = new LinkedList<>();
    String[] protocols;

    SwiftDelegateDescriptor(TypeElement classElement) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;

        // Get the full QualifiedTypeName
        try {
            SwiftDelegate annotation = classElement.getAnnotation(SwiftDelegate.class);
            importPackages = annotation.importPackages();
            protocols = annotation.protocols();
            simpleTypeName = classElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            simpleTypeName = classTypeElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
        }

        ExecutableElement initExecutableElement = null;
        ExecutableElement retainExecutableElement = null;
        ExecutableElement releaseExecutableElement = null;
        boolean hasNativePointer = false;


        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getSimpleName().toString().equals("retain")) {
                    if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                        throw new IllegalArgumentException(String.format("%s is not native method",
                                executableElement.getSimpleName()));
                    }
                    retainExecutableElement = executableElement;
                }
                if (executableElement.getSimpleName().toString().equals("release")) {
                    if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                        throw new IllegalArgumentException(String.format("%s is not native method",
                                executableElement.getSimpleName()));
                    }
                    releaseExecutableElement = executableElement;
                }
                if (executableElement.getSimpleName().toString().equals("init")) {
                    if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                        throw new IllegalArgumentException(String.format("%s is not native method",
                                executableElement.getSimpleName()));
                    }
                    initExecutableElement = executableElement;
                }
            }

            if (element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) element;
                if (variableElement.getSimpleName().toString().equals("nativePointer")
                        && variableElement.asType().toString().equals("long")) {
                    hasNativePointer = true;
                }
            }
        }

        if (!hasNativePointer) {
            TypeElement typeElement = classElement;
            while (typeElement.getSuperclass() != null && !hasNativePointer) {
                typeElement = (TypeElement) ((DeclaredType)typeElement.getSuperclass()).asElement();
                for (Element element : typeElement.getEnclosedElements()) {
                    if (element.getKind() == ElementKind.FIELD) {
                        VariableElement variableElement = (VariableElement) element;
                        if (variableElement.getSimpleName().toString().equals("nativePointer")
                                && variableElement.asType().toString().equals("long")) {
                            hasNativePointer = true;
                            pointerBasicTypeSig = classElement.getQualifiedName().toString().replace(".", "/");
                            break;
                        }
                    }
                }
            }
        }

        if (!hasNativePointer) {
            throw new IllegalArgumentException(String.format("%s doesn't contain nativePointer field", simpleTypeName));
        }

        if (initExecutableElement == null) {
            throw new IllegalArgumentException(String.format("%s doesn't contain init native method", simpleTypeName));
        }

        if (releaseExecutableElement == null) {
            throw new IllegalArgumentException(String.format("%s doesn't contain release native method", simpleTypeName));
        }
        else {
            functions.add(new SwiftFuncDescriptor(releaseExecutableElement));
        }

        if (retainExecutableElement != null) {
            functions.add(new SwiftFuncDescriptor(retainExecutableElement));
        }

        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD && element.getAnnotation(SwiftFunc.class) != null) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                    throw new IllegalArgumentException(String.format("%s is not native method. Only native methods can be annotated with @%s",
                            executableElement.getSimpleName(), SwiftFunc.class.getSimpleName()));
                }

                functions.add(new SwiftFuncDescriptor(executableElement));
            }

            if (element.getKind() == ElementKind.METHOD && element.getAnnotation(SwiftCallbackFunc.class) != null) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
                    throw new IllegalArgumentException(String.format("%s is native method. Only java methods can be annotated with @%s",
                            executableElement.getSimpleName(), SwiftCallbackFunc.class.getSimpleName()));
                }

                callbackFunctions.add(new SwiftCallbackFuncDescriptor(executableElement));
            }
        }
    }

    File generateCode(File dirPath) throws IOException {
        File swiftExtensionFile = new File(dirPath, simpleTypeName + SUFFIX);
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);

        // Write imports
        swiftWriter.emitImports(importPackages);
        swiftWriter.emitEmptyLine();

        swiftWriter.emitStatement(String.format("fileprivate let javaClass = JNI.GlobalFindClass(\"%s/%s\")!", javaPackage.replace(".", "/"), simpleTypeName));
        if (pointerBasicTypeSig != null) {
            swiftWriter.emitStatement(String.format("fileprivate let javaPointerClass = JNI.GlobalFindClass(\"%s\")!", pointerBasicTypeSig));
            swiftWriter.emitStatement("fileprivate let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaPointerClass, \"nativePointer\", \"J\")");
        } else {
            swiftWriter.emitStatement("fileprivate let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaClass, \"nativePointer\", \"J\")");
        }

        swiftWriter.emitStatement("fileprivate let javaConstructor = JNI.api.GetMethodID(JNI.env, javaClass, \"<init>\", \"(J)V\")!");

        swiftWriter.emitEmptyLine();
        swiftWriter.emit(String.format("public class %s", simpleTypeName));
        for (int i = 0; i < protocols.length; i++) {
            if (i == 0) {
                swiftWriter.emit(": " + protocols[0]);
            }
            else {
                swiftWriter.emit(", " + protocols[i]);
            }
        }
        swiftWriter.emit(" {\n");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("let jniObject: jobject");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("public init(jniObject: jobject) {");
        // TODO: throw exception
        swiftWriter.emitStatement("self.jniObject = JNI.api.NewGlobalRef(JNI.env, jniObject)!");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("deinit {");
        swiftWriter.emitStatement("JNI.api.DeleteGlobalRef(JNI.env, jniObject)");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Get swift object from pointer");
        swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
        swiftWriter.emitStatement("guard let pointer = UnsafeRawPointer(bitPattern: Int(JNI.api.GetLongField(JNI.env, javaObject, javaSwiftPointerFiled))) else {\nthrow NSError(domain: \"NullPointerException\", code: 1)\n}");
        swiftWriter.emitStatement(String.format("return Unmanaged<%s>.fromOpaque(pointer).takeUnretainedValue()", simpleTypeName));
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create java object with native pointer");
        swiftWriter.emitStatement("public func javaObject() throws -> jobject {");
        swiftWriter.emitStatement("return jniObject");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Unbalance release");
        swiftWriter.emitStatement("public func release() {");
        swiftWriter.emitStatement("Unmanaged.passUnretained(self).release()");
        swiftWriter.emitStatement("}");

        for (SwiftCallbackFuncDescriptor function : callbackFunctions) {
            function.generateCode(swiftWriter, javaPackage, simpleTypeName);
        }

        swiftWriter.endExtension();

        String swiftFuncName = "Java_" + javaPackage.replace(".", "_") + "_" + simpleTypeName + "_init";
        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement(String.format("@_silgen_name(\"%s\")", swiftFuncName));
        swiftWriter.emitStatement(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, this: jobject) {", swiftFuncName));
        swiftWriter.emitStatement(String.format("let swiftSelf = %s(jniObject: this)", simpleTypeName));
        swiftWriter.emitStatement("let nativePointer = jlong(Int(bitPattern: Unmanaged.passRetained(swiftSelf).toOpaque()))");
        swiftWriter.emitStatement("JNI.api.SetLongField(JNI.env, this, javaSwiftPointerFiled, nativePointer)");
        swiftWriter.emitStatement("}");

        for (SwiftFuncDescriptor function : functions) {
            function.generateCode(swiftWriter, javaPackage, simpleTypeName);
        }

        swiftWriter.close();
        return swiftExtensionFile;
    }
}
