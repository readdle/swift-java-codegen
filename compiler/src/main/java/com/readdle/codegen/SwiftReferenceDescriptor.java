package com.readdle.codegen;


import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftReference;

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

public class SwiftReferenceDescriptor {

    static final String SUFFIX = "Android.swift";

    private TypeElement annotatedClassElement;
    String javaPackage;
    String simpleTypeName;
    String[] importPackages;
    String pointerBasicTypeSig;

    List<SwiftFuncDescriptor> functions = new LinkedList<>();

    SwiftReferenceDescriptor(TypeElement classElement) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;

        // Get the full QualifiedTypeName
        try {
            SwiftReference annotation = classElement.getAnnotation(SwiftReference.class);
            importPackages = annotation.importPackages();
            simpleTypeName = classElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            simpleTypeName = classTypeElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
        }

        // Check if it's an abstract class
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new IllegalArgumentException(String.format("The class %s is abstract. You can't annotate abstract classes with @%s",
                    classElement.getQualifiedName().toString(), SwiftReference.class.getSimpleName()));
        }

        ExecutableElement retainExecutableElement = null;
        ExecutableElement releaseExecutableElement = null;
        boolean hasNativePointer = false;
        boolean hasEmptyConstructor = false;

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
            }

            if (element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) element;
                if (variableElement.getSimpleName().toString().equals("nativePointer")
                        && variableElement.asType().toString().equals("long")) {
                    hasNativePointer = true;
                }
            }

            if (element.getKind() == ElementKind.CONSTRUCTOR && !hasEmptyConstructor) {
                ExecutableElement constructorElement = (ExecutableElement) element;
                if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers().contains(Modifier.PRIVATE)) {
                    hasEmptyConstructor = true;
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

        if (!hasEmptyConstructor) {
            throw new IllegalArgumentException(String.format("%s doesn't contain private empty constructor", simpleTypeName));
        }

        if (releaseExecutableElement == null) {
            throw new IllegalArgumentException(String.format("%s doesn't contain release method", simpleTypeName));
        }

        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
                    functions.add(new SwiftFuncDescriptor(executableElement));
                }
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
        }
        else {
            swiftWriter.emitStatement("fileprivate let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaClass, \"nativePointer\", \"J\")");
        }

        swiftWriter.emitStatement(String.format("fileprivate let javaConstructor = try! JNI.getJavaEmptyConstructor(forClass: \"%s/%s\")", javaPackage.replace(".", "/"), simpleTypeName));

        swiftWriter.emitEmptyLine();
        swiftWriter.beginExtension(simpleTypeName);

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Get swift object from pointer");
        swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
        swiftWriter.emitStatement("guard let pointer = UnsafeRawPointer(bitPattern: Int(JNI.api.GetLongField(JNI.env, javaObject, javaSwiftPointerFiled))) else {\nthrow NSError(domain: \"NullPointerException\", code: 1)\n}");
        swiftWriter.emitStatement(String.format("return Unmanaged<%s>.fromOpaque(pointer).takeUnretainedValue()", simpleTypeName));
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create java object with native pointer");
        swiftWriter.emitStatement("public func javaObject() throws -> jobject {");
        swiftWriter.emitStatement("let nativePointer = jlong(Int(bitPattern: Unmanaged.passRetained(self).toOpaque()))");
        swiftWriter.emitStatement("guard let result = JNI.NewObject(javaClass, methodID: javaConstructor) else {\nthrow NSError(domain: \"CantCreateObject\", code: 1)\n}");
        swiftWriter.emitStatement("JNI.api.SetLongField(JNI.env, result, javaSwiftPointerFiled, nativePointer)");
        swiftWriter.emitStatement("return result");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Unbalance release");
        swiftWriter.emitStatement("public func release() {");
        swiftWriter.emitStatement("Unmanaged.passUnretained(self).release()");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Unbalanced retain");
        swiftWriter.emitStatement("public func retain() {");
        swiftWriter.emitStatement("Unmanaged.passUnretained(self).retain()");
        swiftWriter.emitStatement("}");

        swiftWriter.endExtension();

        for (SwiftFuncDescriptor function : functions) {
            function.generateCode(swiftWriter, javaPackage, simpleTypeName);
        }

        swiftWriter.close();
        return swiftExtensionFile;
    }

    /**
     *
     * @return qualified name
     */
    public String getSwiftType() {
        return simpleTypeName;
    }
}
