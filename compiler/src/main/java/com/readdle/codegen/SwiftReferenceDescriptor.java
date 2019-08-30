package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftGetter;
import com.readdle.codegen.anotation.SwiftReference;
import com.readdle.codegen.anotation.SwiftSetter;

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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.StandardLocation;

import static com.readdle.codegen.JavaSwiftProcessor.FOLDER;

class SwiftReferenceDescriptor {

    private static final String SUFFIX = "Android.swift";
    private String swiftFilePath;

    private String javaFullName;
    private String simpleTypeName;
    private String[] importPackages;

    List<JavaSwiftProcessor.WritableElement> functions = new LinkedList<>();

    SwiftReferenceDescriptor(TypeElement classElement, Filer filer, JavaSwiftProcessor processor) throws IllegalArgumentException {
        this.importPackages = processor.importPackages;

        // Get the full QualifiedTypeName
        try {
            simpleTypeName = classElement.getSimpleName().toString();
            javaFullName = classElement.getQualifiedName().toString().replace(".", "/");
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            simpleTypeName = classTypeElement.getSimpleName().toString();
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
            enclosingElement = enclosingElement.getEnclosingElement();
        }

        // Check if it's an abstract class
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new IllegalArgumentException(String.format("The class %s is abstract. You can't annotate abstract classes with @%s",
                    classElement.getQualifiedName().toString(), SwiftReference.class.getSimpleName()));
        }

        ExecutableElement releaseExecutableElement = null;
        boolean hasNativePointer = false;
        boolean hasEmptyConstructor = false;

        for (Element element : classElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getSimpleName().toString().equals("release")) {
                    if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                        String message = String.format("%s is not native method",
                                executableElement.getSimpleName());
                        throw new SwiftMappingException(message, executableElement);
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

        swiftWriter.emitStatement(String.format("private let javaClass = JNI.GlobalFindClass(\"%s\")!", javaFullName));
        swiftWriter.emitStatement("private let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaClass, \"nativePointer\", \"J\")");

        swiftWriter.emitStatement(String.format("private let javaConstructor = try! JNI.getJavaEmptyConstructor(forClass: \"%s\")", javaFullName));

        swiftWriter.emitEmptyLine();
        swiftWriter.beginExtension(simpleTypeName);

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Get swift object from pointer");
        swiftWriter.emitStatement(String.format("static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
        swiftWriter.emitStatement("let longPointer = JNI.api.GetLongField(JNI.env, javaObject, javaSwiftPointerFiled)");
        swiftWriter.emitStatement("guard longPointer != 0, let pointer = UnsafeRawPointer(bitPattern: Int(longPointer)) else {\nthrow NSError(domain: \"java.lang.NullPointerException\", code: 1)\n}");
        swiftWriter.emitStatement(String.format("return Unmanaged<%s>.fromOpaque(pointer).takeUnretainedValue()", simpleTypeName));
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create java object with native pointer");
        swiftWriter.emitStatement("func javaObject() throws -> jobject {");
        swiftWriter.emitStatement("let nativePointer = jlong(Int(bitPattern: Unmanaged.passRetained(self).toOpaque()))");
        swiftWriter.emitStatement("guard let result = JNI.NewObject(javaClass, methodID: javaConstructor) else {\nthrow NSError(domain: \"CantCreateObject\", code: 1)\n}");
        swiftWriter.emitStatement("JNI.api.SetLongField(JNI.env, result, javaSwiftPointerFiled, nativePointer)");
        swiftWriter.emitStatement("return result");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Unbalance release");
        swiftWriter.emitStatement("func release() {");
        swiftWriter.emitStatement("Unmanaged.passUnretained(self).release()");
        swiftWriter.emitStatement("}");

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Unbalanced retain");
        swiftWriter.emitStatement("func retain() {");
        swiftWriter.emitStatement("_ = Unmanaged.passUnretained(self).retain()");
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
}
