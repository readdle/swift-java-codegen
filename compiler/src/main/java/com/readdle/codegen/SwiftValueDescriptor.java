package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftValue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

class SwiftValueDescriptor {

    private static final String SUFFIX = "Android.swift";

    private TypeElement annotatedClassElement;
    private String javaPackage;
    private String simpleTypeName;
    private String[] importPackages;

    List<SwiftFuncDescriptor> functions = new LinkedList<>();

    SwiftValueDescriptor(TypeElement classElement) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;

        // Get the full QualifiedTypeName
        try {
            SwiftValue annotation = classElement.getAnnotation(SwiftValue.class);
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
                    classElement.getQualifiedName().toString(), SwiftValue.class.getSimpleName()));
        }

        boolean hasEmptyConstructor = false;

        // Check if an empty constructor is given
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0) {
                    hasEmptyConstructor = true;
                    break;
                }
            }
        }

        if (!hasEmptyConstructor) {
            // No empty constructor found
            throw new IllegalArgumentException(String.format("The class %s must provide an public empty default constructor",
                    classElement.getQualifiedName().toString()));
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
        }

    }

    File generateCode(File dirPath) throws IOException {
        File swiftExtensionFile = new File(dirPath, simpleTypeName + SUFFIX);
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);

        // Write imports
        swiftWriter.emitImports(importPackages);

        swiftWriter.emitEmptyLine();
        swiftWriter.beginExtension(simpleTypeName);

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Decoding SwiftValue type with JavaCoder");
        swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws ->%s {", simpleTypeName));
        swiftWriter.emitStatement(String.format("return try JavaDecoder(forPackage: \"%s\").decode(%s.self, from: javaObject)", javaPackage, simpleTypeName));
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Encoding SwiftValue type with JavaCoder");
        swiftWriter.emitStatement("public func javaObject() throws -> jobject {");
        swiftWriter.emitStatement(String.format("return try JavaEncoder(forPackage: \"%s\").encode(self)", javaPackage));
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
