package com.readdle.codegen;

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

        boolean isPrimitiveType() {
            return swiftType.equals("Bool") ||
                    swiftType.equals("Int") ||
                    swiftType.equals("Int8") ||
                    swiftType.equals("Int16") ||
                    swiftType.equals("Int32") ||
                    swiftType.equals("Int64") ||
                    swiftType.equals("UInt") ||
                    swiftType.equals("UInt8") ||
                    swiftType.equals("UInt16") ||
                    swiftType.equals("UInt32") ||
                    swiftType.equals("Float") ||
                    swiftType.equals("Double");
        }

        String returnTypeFunc(boolean isOptional) {
            if (isOptional) {
                return "CallObjectMethod";
            }
            else {
                switch (swiftType) {
                    case "Bool":
                        return "CallBooleanMethod";
                    case "Int":
                    case "Int32":
                    case "UInt16":
                        return "CallIntMethod";
                    case "Int8":
                        return "CallByteMethod";
                    case "Int16":
                    case "UInt8":
                        return "CallShortMethod";
                    case "Int64":
                    case "UInt":
                    case "UInt32":
                        return "CallLongMethod";
                    case "Float":
                        return "CallFloatMethod";
                    case "Double":
                        return "CallDoubleMethod";
                    default:
                        return "CallObjectMethod";
                }
            }
        }

        String staticReturnTypeFunc(boolean isOptional) {
            if (isOptional) {
                return "CallStaticObjectMethod";
            }
            else {
                switch (swiftType) {
                    case "Bool":
                        return "CallStaticBooleanMethod";
                    case "Int":
                    case "Int32":
                    case "UInt16":
                        return "CallStaticIntMethod";
                    case "Int8":
                        return "CallStaticByteMethod";
                    case "Int16":
                    case "UInt8":
                        return "CallStaticShortMethod";
                    case "Int64":
                    case "UInt":
                    case "UInt32":
                        return "CallStaticLongMethod";
                    case "Float":
                        return "CallStaticFloatMethod";
                    case "Double":
                        return "CallStaticDoubleMethod";
                    default:
                        return "CallStaticObjectMethod";
                }
            }
        }
    }

}
