package com.readdle.codegen;

import java.util.regex.Pattern;

class SwiftEnvironment {

    static class Type {
        String swiftType;
        String javaPackage;
        String swiftConstructorType;

        Type(String swiftType, String javaPackage) {
            this.swiftType = swiftType;
            this.javaPackage = javaPackage;
            this.swiftConstructorType = swiftType;
        }

        Type(String swiftType, String javaPackage, String swiftConstructorType) {
            this.swiftType = swiftType;
            this.javaPackage = javaPackage;
            this.swiftConstructorType = swiftConstructorType;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "swiftType='" + swiftType + '\'' +
                    ", javaPackage='" + javaPackage + '\'' +
                    '}';
        }
    }

    public static Type parseJavaType(String javaType) {
        switch (javaType) {
            case "void":
                return null;
            case "java.lang.Integer":
                return new Type("Int", null);
            case "java.lang.Byte":
                return new Type("Int8", null);
            case "java.lang.Short":
                return new Type("Int16", null);
            case "java.lang.Long":
                return new Type("Int64", null);
            case "java.math.BigInteger":
                return new Type("UInt64", null);
            case "java.lang.Boolean":
                return new Type("Bool", null);
            case "java.lang.String":
                return new Type("String", null);
            case "android.net.Uri":
                return new Type("URL", null);
            case "java.util.Date":
                return new Type("Date", null);
            case "java.nio.ByteBuffer":
                return new Type("Data", null);
            case "java.lang.Exception":
                return new Type("Error", null, "NSError");
            default:
                try {
                    if (javaType.startsWith("java.util.ArrayList<")) {
                        Type subType = parseJavaType(javaType.substring("java.util.ArrayList<".length(), javaType.length() - 1));
                        return new Type("[" + subType.swiftType + "]", null);
                    }
                    else if (javaType.startsWith("java.util.HashSet<")) {
                        Type subType = parseJavaType(javaType.substring("java.util.HashSet<".length(), javaType.length() - 1));
                        return new Type("Set<" + subType.swiftType + ">", null);
                    }
                    else if (javaType.startsWith("java.util.HashMap<")) {
                        String substring = javaType.substring("java.util.HashMap<".length(), javaType.length() - 1);
                        int commaIndex = substring.indexOf(",");
                        Type keyType = parseJavaType(substring.substring(0, commaIndex));
                        Type valueType = parseJavaType(substring.substring(commaIndex + 1, substring.length()));
                        return new Type("[" + keyType.swiftType + ":" + valueType.swiftType + "]", null);
                    }
                    else {
                        String[] parts = javaType.split(Pattern.quote("."));
                        String swiftType = parts[parts.length - 1];
                        String javaPackage = javaType.replace("." + swiftType, "");
                        return new Type(swiftType, javaPackage);
                    }
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(javaType);
                }
        }
    }

}
