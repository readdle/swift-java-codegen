package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftDelegate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

import static com.readdle.codegen.JavaSwiftProcessor.FOLDER;

class SwiftDelegateDescriptor {

    private static final String SUFFIX = "Android.swift";
    private String swiftFilePath;

    private TypeElement annotatedClassElement;
    private String javaFullName;
    String simpleTypeName;
    private String[] importPackages;
    private String pointerBasicTypeSig;

    List<SwiftFuncDescriptor> functions = new LinkedList<>();
    private List<SwiftCallbackFuncDescriptor> callbackFunctions = new LinkedList<>();
    private String[] protocols;

    private boolean isInterface;

    SwiftDelegateDescriptor(TypeElement classElement, Filer filer, Messager messager, JavaSwiftProcessor processor) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;
        this.isInterface = classElement.getKind() == ElementKind.INTERFACE;
        this.importPackages = processor.moduleDescriptor.importPackages;

        // Get the full QualifiedTypeName
        try {
            SwiftDelegate annotation = classElement.getAnnotation(SwiftDelegate.class);
            protocols = annotation.protocols();
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

        if (!isInterface) {

            ExecutableElement initExecutableElement = null;
            ExecutableElement releaseExecutableElement = null;
            boolean hasNativePointer = false;

            for (Element element : classElement.getEnclosedElements()) {
                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement executableElement = (ExecutableElement) element;
                    if (executableElement.getSimpleName().toString().equals("release")) {
                        if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                            throw new SwiftMappingException(String.format("%s is not native method",
                                    executableElement.getSimpleName()), executableElement);
                        }
                        releaseExecutableElement = executableElement;
                    }
                    if (executableElement.getSimpleName().toString().equals("init")) {
                        if (!executableElement.getModifiers().contains(Modifier.NATIVE)) {
                            throw new SwiftMappingException(String.format("%s is not native method",
                                    executableElement.getSimpleName()), executableElement);
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
                    typeElement = (TypeElement) ((DeclaredType) typeElement.getSuperclass()).asElement();
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
        }
        try {

            for (Element element : classElement.getEnclosedElements()) {
                messager.printMessage(Diagnostic.Kind.WARNING, "Element: " + element);

                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement executableElement = (ExecutableElement) element;
                    // Except init. We generate it's manually
                    if (executableElement.getModifiers().contains(Modifier.NATIVE) && !executableElement.getSimpleName().contentEquals("init")) {
                        functions.add(new SwiftFuncDescriptor(executableElement, processor));
                    }
                }

                if (element.getAnnotation(SwiftCallbackFunc.class) != null) {
                    String message = String.format("%s SwiftCallbackFunc",
                            element.getSimpleName());
                    messager.printMessage(Diagnostic.Kind.WARNING, message);
                }

                if (element.getKind() == ElementKind.METHOD && element.getAnnotation(SwiftCallbackFunc.class) != null) {
                    ExecutableElement executableElement = (ExecutableElement) element;
                    if (executableElement.getModifiers().contains(Modifier.NATIVE)) {
                        String message = String.format("%s is native method. Only java methods can be annotated with @%s",
                                executableElement.getSimpleName(), SwiftCallbackFunc.class.getSimpleName());
                        throw new SwiftMappingException(message, executableElement);
                    }

                    callbackFunctions.add(new SwiftCallbackFuncDescriptor(executableElement, processor));
                }
            }

        }
        catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.WARNING, "Error: " + e.toString());
        }
    }

    File generateCode() throws IOException {
        File swiftExtensionFile = new File(swiftFilePath);
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);

        // Write imports
        swiftWriter.emitImports(importPackages);
        swiftWriter.emitEmptyLine();

        swiftWriter.emitStatement(String.format("private let javaClass = JNI.GlobalFindClass(\"%s\")!", javaFullName));

        if (!isInterface) {
            if (pointerBasicTypeSig != null) {
                swiftWriter.emitStatement(String.format("private let javaPointerClass = JNI.GlobalFindClass(\"%s\")!", pointerBasicTypeSig));
                swiftWriter.emitStatement("private let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaPointerClass, \"nativePointer\", \"J\")");
            } else {
                swiftWriter.emitStatement("private let javaSwiftPointerFiled = JNI.api.GetFieldID(JNI.env, javaClass, \"nativePointer\", \"J\")");
            }
        }

        swiftWriter.emitEmptyLine();
        swiftWriter.emit(String.format("public class %s", simpleTypeName));
        for (int i = 0; i < protocols.length; i++) {
            if (i == 0) {
                swiftWriter.emit(": NSObject, " + protocols[0]);
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

        if (isInterface) {
            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement("// Create swift object");
            swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
            swiftWriter.emitStatement(String.format("return %s(jniObject: javaObject)", simpleTypeName));
            swiftWriter.emitStatement("}");
        }
        else {
            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement("// Get swift object from pointer");
            swiftWriter.emitStatement(String.format("public static func from(javaObject: jobject) throws -> %s {", simpleTypeName));
            swiftWriter.emitStatement("let longPointer = JNI.api.GetLongField(JNI.env, javaObject, javaSwiftPointerFiled)");
            swiftWriter.emitStatement("guard longPointer != 0, let pointer = UnsafeRawPointer(bitPattern: Int(longPointer)) else {\nthrow NSError(domain: \"java.lang.NullPointerException\", code: 1)\n}");
            swiftWriter.emitStatement(String.format("return Unmanaged<%s>.fromOpaque(pointer).takeUnretainedValue()", simpleTypeName));
            swiftWriter.emitStatement("}");

            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement("// Unbalance release");
            swiftWriter.emitStatement("public func release() {");
            swiftWriter.emitStatement("Unmanaged.passUnretained(self).release()");
            swiftWriter.emitStatement("}");
        }

        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("// Create java object with native pointer");
        swiftWriter.emitStatement("public func javaObject() throws -> jobject {");
        swiftWriter.emitStatement("return jniObject");
        swiftWriter.emitStatement("}");

        for (SwiftCallbackFuncDescriptor function : callbackFunctions) {
            function.generateCode(swiftWriter, javaFullName, simpleTypeName);
        }

        swiftWriter.endExtension();

        if (!isInterface) {
            String swiftFuncName = Utils.mangleFunctionName(javaFullName, "init", new ArrayList<>());
            swiftWriter.emitEmptyLine();
            swiftWriter.emitStatement(String.format("@_cdecl(\"%s\")", swiftFuncName));
            swiftWriter.emitStatement(String.format("public func %s(env: UnsafeMutablePointer<JNIEnv?>, this: jobject) {", swiftFuncName));
            swiftWriter.emitStatement(String.format("let swiftSelf = %s(jniObject: this)", simpleTypeName));
            swiftWriter.emitStatement("let nativePointer = jlong(Int(bitPattern: Unmanaged.passRetained(swiftSelf).toOpaque()))");
            swiftWriter.emitStatement("JNI.api.SetLongField(JNI.env, this, javaSwiftPointerFiled, nativePointer)");
            swiftWriter.emitStatement("}");
        }

        for (SwiftFuncDescriptor function : functions) {
            function.generateCode(swiftWriter, javaFullName, simpleTypeName);
        }

        swiftWriter.close();
        return swiftExtensionFile;
    }

    public String getJavaFullName() {
        return javaFullName;
    }
}
