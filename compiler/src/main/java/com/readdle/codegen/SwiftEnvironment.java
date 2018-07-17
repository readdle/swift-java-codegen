package com.readdle.codegen;

import java.util.regex.Pattern;

class SwiftEnvironment {

    static class Type {
        String swiftType;
        String javaType;
        String swiftConstructorType;

        Type(String swiftType, String javaType) {
            this.swiftType = swiftType;
            this.javaType = javaType;
            this.swiftConstructorType = swiftType;
        }

        Type(String swiftType, String javaType, String swiftConstructorType) {
            this.swiftType = swiftType;
            this.javaType = javaType;
            this.swiftConstructorType = swiftConstructorType;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "swiftType='" + swiftType + '\'' +
                    ", javaType='" + javaType + '\'' +
                    '}';
        }
    }

    public static Type parseJavaType(String javaType) {
        switch (javaType) {
            case "void":
                return null;
            case "java.lang.Integer":
                return new Type("Int", javaType);
            case "java.lang.Byte":
                return new Type("Int8", javaType);
            case "java.lang.Short":
                return new Type("Int16", javaType);
            case "java.lang.Long":
                return new Type("Int64", javaType);
            case "java.math.BigInteger":
                return new Type("UInt64", javaType);
            case "java.lang.Boolean":
                return new Type("Bool", javaType);
            case "java.lang.String":
                return new Type("String", javaType);
            case "android.net.Uri":
                return new Type("URL", javaType);
            case "java.util.Date":
                return new Type("Date", javaType);
            case "java.nio.ByteBuffer":
                return new Type("Data", javaType);
            case "java.lang.Exception":
                return new Type("Error", javaType, "NSError");
            default:
                try {
                    if (javaType.startsWith("java.util.ArrayList<")) {
                        Type subType = parseJavaType(javaType.substring("java.util.ArrayList<".length(), javaType.length() - 1));
                        return new Type("[" + subType.swiftType + "]", javaType);
                    }
                    else if (javaType.startsWith("java.util.HashSet<")) {
                        Type subType = parseJavaType(javaType.substring("java.util.HashSet<".length(), javaType.length() - 1));
                        return new Type("Set<" + subType.swiftType + ">", javaType);
                    }
                    else if (javaType.startsWith("java.util.HashMap<")) {
                        String substring = javaType.substring("java.util.HashMap<".length(), javaType.length() - 1);
                        int commaIndex = substring.indexOf(",");
                        Type keyType = parseJavaType(substring.substring(0, commaIndex));
                        Type valueType = parseJavaType(substring.substring(commaIndex + 1, substring.length()));
                        return new Type("[" + keyType.swiftType + ":" + valueType.swiftType + "]", javaType);
                    }
                    else {
                        // Try found enclosing typename
                        String[] parts = javaType.split(Pattern.quote("$"));
                        if (parts.length == 1) {
                            // If not found enclosing, find typename
                            parts = javaType.split(Pattern.quote("."));
                        }
                        String swiftType = parts[parts.length - 1];
                        return new Type(swiftType, javaType);
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(javaType);
                }
        }
    }

}
