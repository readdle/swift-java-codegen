package com.readdle.codegen;

import com.google.gson.Gson;
import com.readdle.codegen.anotation.SwiftBlock;
import com.readdle.codegen.anotation.SwiftDelegate;
import com.readdle.codegen.anotation.SwiftReference;
import com.readdle.codegen.anotation.SwiftValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

public class JavaSwiftProcessor extends AbstractProcessor {

    interface WritableElement {
        void generateCode(SwiftWriter swiftWriter, String javaFullName, String swiftType) throws IOException;
        String toString(String javaClassname);
    }

    public static final String FOLDER = "SwiftGenerated";
    public static final String PACKAGE_OPTION = "com.readdle.codegen.package";

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    SwiftModuleDescriptor moduleDescriptor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        note("JavaSwiftProcessor init started");

        String packageJson = processingEnv.getOptions().get(PACKAGE_OPTION);

        moduleDescriptor = new Gson().fromJson(packageJson, SwiftModuleDescriptor.class);
        if (moduleDescriptor == null) {
            error(null, "No package description with option: com.readdle.codegen.package");
            return;
        }

        note("Package moduleName: " + moduleDescriptor.moduleName);

        if (moduleDescriptor.importPackages != null) {
            for (String anImport : moduleDescriptor.importPackages) {
                note("Package import: " + anImport);
            }
        }

        if (moduleDescriptor.customTypeMappings != null) {
            for (String key : moduleDescriptor.customTypeMappings.keySet()) {
                note("Package custom mapping: " + key + " -> " + moduleDescriptor.customTypeMappings.get(key));
            }
        }

        note("JavaSwiftProcessor init finished successfully");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(com.readdle.codegen.anotation.SwiftValue.class.getCanonicalName());
        annotations.add(com.readdle.codegen.anotation.SwiftReference.class.getCanonicalName());
        annotations.add(com.readdle.codegen.anotation.SwiftDelegate.class.getCanonicalName());
        annotations.add(com.readdle.codegen.anotation.SwiftBlock.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>(super.getSupportedOptions());
        options.add(PACKAGE_OPTION);
        return options;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            return processImpl(annotations, roundEnv);
        }
        catch(SwiftMappingException exc) {
            exc.printStackTrace();
            error(exc.getElement(), exc.getMessage());
            return true;
        }
    }

    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Filer filer = processingEnv.getFiler();
        note("JavaSwiftProcessor start code generation");

        try {
            generateJavaSwift(filer);
        } catch (IOException e) {
            e.printStackTrace();
            error(null, "Can't write to file: " + e.getMessage());
        }

        note("SwiftValue to process: "
                + roundEnv.getElementsAnnotatedWith(SwiftValue.class).size());
        note("SwiftReference to process: "
                + roundEnv.getElementsAnnotatedWith(SwiftReference.class).size());
        note("SwiftDelegate to process: "
                + roundEnv.getElementsAnnotatedWith(SwiftDelegate.class).size());
        note("SwiftBlock to process: "
                + roundEnv.getElementsAnnotatedWith(SwiftBlock.class).size());

        Map<String, SwiftValueDescriptor> swiftValues = new HashMap<>();
        Map<String, SwiftReferenceDescriptor> swiftReferences = new HashMap<>();
        Map<String, SwiftDelegateDescriptor> swiftDelegates = new HashMap<>();
        Map<String, SwiftBlockDescriptor> swiftBlocks = new HashMap<>();

        if (moduleDescriptor == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "No package description with SwiftModule.class", null);
            return true; // Exit processing
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SwiftValue.class)) {
            // Check if a class has been annotated with @SwiftValue
            if (annotatedElement.getKind() != ElementKind.CLASS && annotatedElement.getKind() != ElementKind.ENUM) {
                error(annotatedElement, "Only classes or enums can be annotated with @%s", SwiftValue.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                SwiftValueDescriptor swiftValueDescriptor = new SwiftValueDescriptor(typeElement, filer, this);
                swiftValues.put(swiftValueDescriptor.getSwiftType(), swiftValueDescriptor);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
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
                SwiftReferenceDescriptor swiftReferenceDescriptor = new SwiftReferenceDescriptor(typeElement, filer, this);
                swiftReferences.put(swiftReferenceDescriptor.getSwiftType(), swiftReferenceDescriptor);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
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
                SwiftDelegateDescriptor delegateDescriptor = new SwiftDelegateDescriptor(typeElement, filer, messager, this);
                swiftDelegates.put(delegateDescriptor.simpleTypeName, delegateDescriptor);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                error(annotatedElement, e.getMessage());
                return true; // Exit processing
            }
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SwiftBlock.class)) {
            // Skip PARAMS
            if (annotatedElement.getKind() == ElementKind.PARAMETER) {
                continue;
            }

            // Check if a class has been annotated with @SwiftValue
            if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                error(annotatedElement, "Only interface can be annotated with @%s", SwiftBlock.class.getSimpleName());
                return true; // Exit processing
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            try {
                SwiftBlockDescriptor blockDescriptor = new SwiftBlockDescriptor(typeElement, filer, this);
                swiftBlocks.put(blockDescriptor.simpleTypeName, blockDescriptor);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                error(annotatedElement, e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftValueDescriptor valueDescriptor: swiftValues.values()) {
            try {
                File file = valueDescriptor.generateCode();
                /* Logging */
                note("Generate SwiftValue " + file.getName() + ":");
                for (WritableElement function : valueDescriptor.getFunctions()) {
                    note(function.toString(valueDescriptor.getJavaFullName()));
                }
                /* Logging */
            } catch (IOException e) {
                e.printStackTrace();
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftReferenceDescriptor referenceDescriptor: swiftReferences.values()) {
            try {
                File file = referenceDescriptor.generateCode();
                /* Logging */
                note("Generate SwiftReference " + file.getName() + ":");
                for (WritableElement function : referenceDescriptor.functions) {
                    note(function.toString(referenceDescriptor.getJavaFullName()));
                }
                /* Logging */
            } catch (IOException e) {
                e.printStackTrace();
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftDelegateDescriptor delegateDescriptor: swiftDelegates.values()) {
            try {
                File file = delegateDescriptor.generateCode();
                /* Logging */
                note("Generate SwiftDelegate" + file.getName() + ":");
                for (WritableElement function : delegateDescriptor.functions) {
                    note(function.toString(delegateDescriptor.getJavaFullName()));
                }
                /* Logging */
            } catch (IOException e) {
                e.printStackTrace();
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        for (SwiftBlockDescriptor blockDescriptor: swiftBlocks.values()) {
            try {
                File file = blockDescriptor.generateCode();
                /* Logging */
                note("Generate SwiftBlock" + file.getName());
                /* Logging */
            } catch (IOException e) {
                e.printStackTrace();
                error(null, "Can't write to file: " + e.getMessage());
                return true; // Exit processing
            }
        }

        note("JavaSwiftProcessor finished successfully!");

        return false;
    }

    private void generateJavaSwift(Filer filer) throws IOException {
        String swiftFilePath = filer.getResource(StandardLocation.SOURCE_OUTPUT, FOLDER, "SwiftJava.swift").toUri().getPath();
        File swiftExtensionFile = new File(swiftFilePath);
        swiftExtensionFile.getParentFile().mkdir();
        note("JavaSwiftProcessor will generate sources at: " + swiftExtensionFile.getParent());
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

        // TODO: move to sample project
        swiftWriter.emitStatement("@_silgen_name(\"Java_com_readdle_codegen_anotation_JavaSwift_dumpReferenceTables\")");
        swiftWriter.emitStatement("public func Java_com_readdle_codegen_anotation_JavaBridgeable_dumpReferenceTables(env: UnsafeMutablePointer<JNIEnv?>, clazz: jclass) {");
        swiftWriter.emitStatement("JNI.dumpReferenceTables()");
        swiftWriter.emitStatement("}");

        swiftWriter.close();
    }

    void note(String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg);
    }

    void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    boolean isNullable(Element element) {
        note("Check nullability " + element.asType().toString());
        if (element.asType().getKind().isPrimitive()) {
            return false;
        }
        if (element.getKind() == ElementKind.METHOD) {
            ExecutableElement executableElement = (ExecutableElement) element;
            if (executableElement.getReturnType().getKind().isPrimitive()) {
                return false;
            }
        }
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

    boolean isUnsigned(Element element) {
        List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        for (AnnotationMirror mirror : mirrors) {
            Name simpleName = mirror.getAnnotationType().asElement().getSimpleName();
            if (simpleName.contentEquals("Unsigned")) {
                return true;
            }
        }
        return false;
    }

    static String replaceLast(String text, char replace, char replacement) {
        int index = text.lastIndexOf(replace);
        if (index >= 0) {
            return text.substring(0, index) + replacement + text.substring(index + 1);
        }
        return text;
    }

    public SwiftEnvironment.Type parseJavaType(String javaType) {
        if (moduleDescriptor.customTypeMappings != null && moduleDescriptor.customTypeMappings.containsKey(javaType)) {
            return new SwiftEnvironment.Type(moduleDescriptor.customTypeMappings.get(javaType), javaType);
        }
        switch (javaType) {
            case "void":
                return null;
            case "byte":
                return new SwiftEnvironment.Type("Int8", javaType);
            case "short":
                return new SwiftEnvironment.Type("Int16", javaType);
            case "int":
                return new SwiftEnvironment.Type("Int", javaType);
            case "long":
                return new SwiftEnvironment.Type("Int64", javaType);
            case "float":
                return new SwiftEnvironment.Type("Float", javaType);
            case "double":
                return new SwiftEnvironment.Type("Double", javaType);
            case "boolean":
                return new SwiftEnvironment.Type("Bool", javaType);
            case "java.lang.Integer":
                return new SwiftEnvironment.Type("Int", javaType);
            case "java.lang.Byte":
                return new SwiftEnvironment.Type("Int8", javaType);
            case "java.lang.Short":
                return new SwiftEnvironment.Type("Int16", javaType);
            case "java.lang.Long":
                return new SwiftEnvironment.Type("Int64", javaType);
            case "java.math.BigInteger":
                return new SwiftEnvironment.Type("UInt64", javaType);
            case "java.lang.Boolean":
                return new SwiftEnvironment.Type("Bool", javaType);
            case "java.lang.String":
                return new SwiftEnvironment.Type("String", javaType);
            case "android.net.Uri":
                return new SwiftEnvironment.Type("URL", javaType);
            case "java.util.Date":
                return new SwiftEnvironment.Type("Date", javaType);
            case "java.nio.ByteBuffer":
                return new SwiftEnvironment.Type("Data", javaType);
            case "java.lang.Exception":
                return new SwiftEnvironment.Type("Error", javaType, "NSError");
            default:
                try {
                    if (javaType.startsWith("java.util.ArrayList<")) {
                        SwiftEnvironment.Type subType = parseJavaType(javaType.substring("java.util.ArrayList<".length(), javaType.length() - 1));
                        return new SwiftEnvironment.Type("[" + subType.swiftType + "]", javaType);
                    }
                    else if (javaType.startsWith("java.util.HashSet<")) {
                        SwiftEnvironment.Type subType = parseJavaType(javaType.substring("java.util.HashSet<".length(), javaType.length() - 1));
                        return new SwiftEnvironment.Type("Set<" + subType.swiftType + ">", javaType);
                    }
                    else if (javaType.startsWith("java.util.HashMap<")) {
                        String substring = javaType.substring("java.util.HashMap<".length(), javaType.length() - 1);
                        int commaIndex = substring.indexOf(",");
                        SwiftEnvironment.Type keyType = parseJavaType(substring.substring(0, commaIndex));
                        SwiftEnvironment.Type valueType = parseJavaType(substring.substring(commaIndex + 1));
                        return new SwiftEnvironment.Type("[" + keyType.swiftType + ":" + valueType.swiftType + "]", javaType);
                    }
                    else {
                        // Try found enclosing typename
                        String[] parts = javaType.split(Pattern.quote("$"));
                        if (parts.length == 1) {
                            // If not found enclosing, find typename
                            parts = javaType.split(Pattern.quote("."));
                        }
                        String swiftType = parts[parts.length - 1];
                        return new SwiftEnvironment.Type(swiftType, javaType);
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(javaType);
                }
        }
    }
}
