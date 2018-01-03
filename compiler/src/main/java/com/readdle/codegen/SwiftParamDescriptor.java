package com.readdle.codegen;


public class SwiftParamDescriptor {

    public final String name;
    public final String swiftType;
    public final boolean isOptional;
    public final String description;

    public SwiftParamDescriptor(String name, String swiftType) {
        this(name, swiftType, true, null);
    }

    public SwiftParamDescriptor(String name, String swiftType, boolean isOptional) {
        this(name, swiftType, isOptional, null);
    }

    public SwiftParamDescriptor(String name, String swiftType, boolean isOptional, String description) {
        this.name = name;
        this.swiftType = swiftType;
        this.isOptional = isOptional;
        this.description = description;
    }
}
