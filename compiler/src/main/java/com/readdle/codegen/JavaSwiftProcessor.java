package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftDelegate;
import com.readdle.codegen.anotation.SwiftReference;
import com.readdle.codegen.anotation.SwiftValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public class JavaSwiftProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    private File sourcePath;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        // TODO: try to find better way to get GeneratedSources path
        try {
            Filer filer = processingEnv.getFiler();
            FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "tmp", (Element[]) null);
            String projectPath = resource.toUri().getPath();
            projectPath = projectPath.substring(0, projectPath.lastIndexOf("/build/"));
            resource.delete();
            sourcePath = new File(projectPath, "/src/main/swift/.build/generated");

            if (sourcePath.mkdirs()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "GeneratedSources was created");
            }
        }
        catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Can't get source dir: " + e.getMessage());
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(com.readdle.codegen.anotation.SwiftValue.class.getCanonicalName());
        annotations.add(com.readdle.codegen.anotation.SwiftReference.class.getCanonicalName());
        annotations.add(com.readdle.codegen.anotation.SwiftDelegate.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        messager.printMessage(Diagnostic.Kind.NOTE, "Start SwiftJava code generation:");

        Map<String, SwiftValueDescriptor> swiftValues = new HashMap<>();
        Map<String, SwiftReferenceDescriptor> swiftReferences = new HashMap<>();
        Map<String, SwiftDelegateDescriptor> swiftDelegates = new HashMap<>();

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SwiftValue.class)) {
            // Check if a class has been annotated with @SwiftValue
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s", SwiftValue.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                SwiftValueDescriptor swiftValueDescriptor = new SwiftValueDescriptor(typeElement);
                swiftValues.put(swiftValueDescriptor.getSwiftType(), swiftValueDescriptor);
            }
            catch (IllegalArgumentException e) {
                error(annotatedElement, e.getMessage());
                return true; // Exit processing
            }
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SwiftReference.class)) {
            // Check if a class has been annotated with @SwiftValue
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s", SwiftReference.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                SwiftReferenceDescriptor swiftReferenceDescriptor = new SwiftReferenceDescriptor(typeElement);
                swiftReferences.put(swiftReferenceDescriptor.getSwiftType(), swiftReferenceDescriptor);
            }
            catch (IllegalArgumentException e) {
                error(annotatedElement, e.getMessage());
                return true; // Exit processing
            }
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SwiftDelegate.class)) {
            // Check if a class has been annotated with @SwiftValue
            if (annotatedElement.getKind() != ElementKind.CLASS && annotatedElement.getKind() != ElementKind.INTERFACE) {
                error(annotatedElement, "Only class or interface can be annotated with @%s", SwiftDelegate.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                SwiftDelegateDescriptor delegateDescriptor = new SwiftDelegateDescriptor(typeElement);
                swiftDelegates.put(delegateDescriptor.simpleTypeName, delegateDescriptor);
            }
            catch (IllegalArgumentException e) {
                error(annotatedElement, e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftValueDescriptor valueDescriptor: swiftValues.values()) {

            for (SwiftFuncDescriptor function : valueDescriptor.functions) {
                messager.printMessage(Diagnostic.Kind.NOTE, function.toString());
            }

            try {
                File file = valueDescriptor.generateCode(sourcePath);
                messager.printMessage(Diagnostic.Kind.NOTE, file.getName() + " generated");
            } catch (IOException e) {
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftReferenceDescriptor referenceDescriptor: swiftReferences.values()) {

            for (SwiftFuncDescriptor function : referenceDescriptor.functions) {
                messager.printMessage(Diagnostic.Kind.NOTE, function.toString());
            }

            try {
                File file = referenceDescriptor.generateCode(sourcePath);
                messager.printMessage(Diagnostic.Kind.NOTE, file.getName() + " generated");
            } catch (IOException e) {
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftDelegateDescriptor delegateDescriptor: swiftDelegates.values()) {

            for (SwiftFuncDescriptor function : delegateDescriptor.functions) {
                messager.printMessage(Diagnostic.Kind.NOTE, function.toString());
            }

            try {
                File file = delegateDescriptor.generateCode(sourcePath);
                messager.printMessage(Diagnostic.Kind.NOTE, file.getName() + " generated");
            } catch (IOException e) {
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        try {
            generateJavaSwift(sourcePath);
        } catch (IOException e) {
            error(null, "Can't write to file: " + e.getMessage());
            return true; // Exit processing
        }

        messager.printMessage(Diagnostic.Kind.NOTE, "SwiftJava finished successfully!");

	    return false;
    }

    void generateJavaSwift(File dirPath) throws IOException {
        // TODO: check if already generated
        File swiftExtensionFile = new File(dirPath, "JavaSwift.swift");
        SwiftWriter swiftWriter = new SwiftWriter(swiftExtensionFile);
        swiftWriter.emitImports(new String[0]);
        swiftWriter.emitEmptyLine();
        swiftWriter.emitStatement("public let SwiftErrorClass = JNI.GlobalFindClass(\"com/readdle/codegen/anotation/SwiftError\")");
        swiftWriter.emitStatement("public let SwiftRuntimeErrorClass = JNI.GlobalFindClass(\"com/readdle/codegen/anotation/SwiftRuntimeError\")");
        swiftWriter.emitEmptyLine();
        // TODO: remove when JavaCoder become deprecated
        swiftWriter.emitStatement("@_silgen_name(\"Java_com_readdle_codegen_anotation_JavaSwift_init\")");
        swiftWriter.emitStatement("public func Java_com_readdle_codegen_anotation_JavaBridgeable_init(env: UnsafeMutablePointer<JNIEnv?>, clazz: jclass) {");
        swiftWriter.emitStatement("JavaCoderConfig.RegisterBasicJavaTypes()");
        swiftWriter.emitStatement("}");
        swiftWriter.close();
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    static boolean isNullable(Element element) {
        List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        for (AnnotationMirror mirror : mirrors) {
            Name simpleName = mirror.getAnnotationType().asElement().getSimpleName();
            if (simpleName.contentEquals("NonNull")) {
                return false;
            }
            if (simpleName.contentEquals("NotNull")) {
                return false;
            }
        }
        return true;
    }

    static String replaceLast(String text, char replace, char replacement) {
        int index = text.lastIndexOf(replace);
        if (index >= 0) {
            return text.substring(0, index) + replacement + text.substring(index + 1, text.length());
        }
        return text;
    }
}
