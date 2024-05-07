package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftGetter;
import com.readdle.codegen.anotation.SwiftSetter;
import com.readdle.codegen.anotation.SwiftValue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.StandardLocation;

import static com.readdle.codegen.JavaSwiftProcessor.FOLDER;

class SwiftValueDescriptor {

    private static final String SUFFIX = "Android.swift";
    private String swiftFilePath;

    private String javaPackage;
    private String javaFullName;
    private String simpleTypeName;
    private String[] importPackages;

    private boolean hasSubclasses = false;

    private List<JavaSwiftProcessor.WritableElement> functions = new LinkedList<>();

    SwiftValueDescriptor(TypeElement classElement, Filer filer, JavaSwiftProcessor processor) throws IllegalArgumentException {
        this.importPackages = processor.moduleDescriptor.importPackages;

        // Get the full QualifiedTypeName
        try {
            SwiftValue swiftValue = classElement.getAnnotation(SwiftValue.class);
            hasSubclasses = swiftValue.hasSubclasses();
            simpleTypeName = classElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
            javaFullName = classElement.getQualifiedName().toString().replace(".", "/");
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            simpleTypeName = classTypeElement.getSimpleName().toString();
            javaPackage = classElement.getQualifiedName().toString().replace("." + simpleTypeName, "");
            javaFullName = classElement.getQualifiedName().toString().replace(".", "/");
        }

        try {
            swiftFilePath = filer.createResource(StandardLocation.SOURCE_OUTPUT, FOLDER, simpleTypeName + SUFFIX, classElement).toUri().getPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't create swift file");
        }

        Element enclosingElement = classElement.getEnclosingElement();
        while (enclosingElement != null && enclosingElement.getKind() == ElementKind.CLASS) {
            javaFullName = JavaSwiftProcessor.replaceLast(javaFullName, '/', '$');
            javaPackage = javaFullName.substring(0, javaFullName.lastIndexOf("."));
            enclosingElement = enclosingElement.getEnclosingElement();
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

        if (!hasEmptyConstructor && classElement.getKind() != ElementKind.ENUM) {
            // No empty constructor found
            throw new IllegalArgumentException(String.format("The class %s must provide an public empty default constructor",
                    classElement.getQualifiedName().toString()));
        }

        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
                    SwiftGetter getterAnnotation = executableElement.getAnnotation(SwiftGetter.class);
                    SwiftSetter setterAnnotation = executableElement.getAnnotation(SwiftSetter.class);

                    if (getterAnnotation != null) {
                        functions.add(new SwiftGetterDescriptor(executableElement, getterAnnotation, processor));
                    }
                    else if (setterAnnotation != null) {
                        functions.add(new SwiftSetterDescriptor(executableElement, setterAnnotation, processor));
                    } else {
                        functions.add(new SwiftFuncDescriptor(executableElement, processor));
                    }
                }
            }
        }

    }

    File generateCode() throws IOException {
        File swiftExtensionFile = new File(swiftFilePath);
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);

        // Write imports
        swiftWriter.emitImports(importPackages);

        swiftWriter.emitEmptyLine();
        swiftWriter.beginExtension(simpleTypeName);

        if (hasSubclasses) {
            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement("// Decoding SwiftValue type with JavaCoder");
            swiftWriter.emitStatement(String.format("static func from<T: %s>(javaObject: jobject) throws -> T {", simpleTypeName));
            swiftWriter.emitStatement(String.format("let any = try JavaDecoder(forPackage: \"%s\", missingFieldsStrategy: .ignore).decode(AnyCodable.self, from: javaObject)", javaPackage.replace(".", "/")));
            swiftWriter.emitStatement("return any.value as! T");
            swiftWriter.emitStatement("}");
        }
        else {
            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement("// Decoding SwiftValue type with JavaCoder");
            swiftWriter.emitStatement(String.format("static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
            swiftWriter.emitStatement(String.format("return try JavaDecoder(forPackage: \"%s\", missingFieldsStrategy: .ignore).decode(%s.self, from: javaObject)", javaPackage.replace(".", "/"), simpleTypeName));
            swiftWriter.emitStatement("}");
        }

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Encoding SwiftValue type with JavaCoder");
        swiftWriter.emitStatement("func javaObject() throws -> jobject {");
        swiftWriter.emitStatement(String.format("return try JavaEncoder(forPackage: \"%s\", missingFieldsStrategy: .ignore).encode(self)", javaPackage.replace(".", "/")));
        swiftWriter.emitStatement("}");

        swiftWriter.endExtension();

        for (JavaSwiftProcessor.WritableElement function : functions) {
            function.generateCode(swiftWriter, javaFullName, simpleTypeName);
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

    public String getJavaFullName() {
        return javaFullName;
    }

    public List<JavaSwiftProcessor.WritableElement> getFunctions() {
        return functions;
    }
}
