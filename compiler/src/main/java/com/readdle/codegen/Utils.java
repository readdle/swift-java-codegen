package com.readdle.codegen;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic;

public class Utils {

    private Utils() {
    }

    /**
     * Mangled function name according to https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/design.html
     *
     * @param javaFullName - fully-qualified class name
     * @param methodName   - method name
     * @param params       - list of arguments classes (primitives or arrays not supported here)
     * @return mangled function name
     * @see "Resolving Native Method Names"
     */
    public static String mangleFunctionName(String javaFullName, String methodName, List<SwiftParamDescriptor> params) {
        StringBuilder mangledFunctionName = new StringBuilder("Java_"); /* the prefix Java_ */
        mangledFunctionName.append(mangleName(javaFullName)); /* a mangled fully-qualified class name */
        mangledFunctionName.append("_"); /* an underscore (“_”) separator */
        mangledFunctionName.append(mangleName(methodName)); /*  a mangled method name */
        mangledFunctionName.append("__"); /* for overloaded native methods, two underscores (“__”) followed by the mangled argument signature */

        for (SwiftParamDescriptor paramDescriptor : params) {
            mangledFunctionName.append(mangleName(javaClassToSig(paramDescriptor.swiftType.javaType)));
        }

        return mangledFunctionName.toString();
    }

    public static String mangleName(String javaFullName) {
        // Unicode Character Translation
        String mangledNameString = javaFullName.replace("_", "_1"); // the character “_”
        mangledNameString = mangledNameString.replace("$", "_00024"); // a Unicode character XXXX: Inner class separator $
        mangledNameString = mangledNameString.replace(";", "_2"); // the character “;” in signatures
        mangledNameString = mangledNameString.replace("[", "_3"); // the character “[“ in signatures
        return mangledNameString.replace("/", "_");
    }

    public static String javaClassToSig(String javaClass) {
        // First, remove all templates
        int templateStart = javaClass.indexOf("<");
        if (templateStart > 0) {
            javaClass = javaClass.substring(0, templateStart);
        }
        // Replace all dots with / in package name
        return "L" + javaClass.replace(".", "/") + ";";
    }

    public static String typeToString(final TypeMirror type) {
        StringBuilder stringBuilder = new StringBuilder();
        typeToString(type, stringBuilder, '$');
        return stringBuilder.toString();
    }

    public static void typeToString(final TypeMirror type, final StringBuilder result, final char innerClassSeparator) {
        type.accept(new SimpleTypeVisitor6<Void, Void>() {

            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v) {
                TypeElement typeElement = (TypeElement) declaredType.asElement();
                rawTypeToString(result, typeElement, innerClassSeparator);
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    result.append("<");
                    for (int i = 0; i < typeArguments.size(); i++) {
                        if (i != 0) {
                            result.append(", ");
                        }
                        typeToString(typeArguments.get(i), result, innerClassSeparator);
                    }
                    result.append(">");
                }
                return null;
            }

            @Override
            public Void visitPrimitive(PrimitiveType primitiveType, Void v) {
                throw new UnsupportedOperationException("Primitives not supported");
            }

            @Override
            public Void visitArray(ArrayType arrayType, Void v) {
                throw new UnsupportedOperationException("Array not supported");
            }

            @Override
            public Void visitTypeVariable(TypeVariable typeVariable, Void v) {
                result.append(typeVariable.asElement().getSimpleName());
                return null;
            }

            @Override
            public Void visitError(ErrorType errorType, Void v) {
                // Error type found, a type may not yet have been generated, but we need the type
                // so we can generate the correct code in anticipation of the type being available
                // to the compiler.

                // Paramterized types which don't exist are returned as an error type whose name is "<any>"
                if ("<any>".equals(errorType.toString())) {
                    throw new UnsupportedOperationException("Type reported as <any> is likely a not-yet generated parameterized type.");
                }
                // TODO(cgruber): Figure out a strategy for non-FQCN cases.
                result.append(errorType.toString());
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror typeMirror, Void v) {
                throw new UnsupportedOperationException("Unexpected TypeKind " + typeMirror.getKind() + " for " + typeMirror);
            }

        }, null);
    }

    private static void rawTypeToString(StringBuilder result, TypeElement type, char innerClassSeparator) {
        String packageName = getPackage(type).getQualifiedName().toString();
        String qualifiedName = type.getQualifiedName().toString();
        if (packageName.isEmpty()) {
            result.append(qualifiedName.replace('.', innerClassSeparator));
        } else {
            result.append(packageName);
            result.append('.');
            result.append(
                    qualifiedName.substring(packageName.length() + 1).replace('.', innerClassSeparator));
        }
    }

    public static PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }
}
