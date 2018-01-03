package com.readdle.codegen;

import com.readdle.codegen.anotation.SwiftValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
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
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Map<String, SwiftValueDescriptor> swiftValues = new HashMap<>();

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

        for (SwiftValueDescriptor valueDescriptor: swiftValues.values()) {
            try {
                File file = valueDescriptor.generateCode(sourcePath);
                messager.printMessage(Diagnostic.Kind.NOTE, file.getName() + " generated");
            } catch (IOException e) {
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        messager.printMessage(Diagnostic.Kind.NOTE, "SwiftJava finished successfully!");

	    return false;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
