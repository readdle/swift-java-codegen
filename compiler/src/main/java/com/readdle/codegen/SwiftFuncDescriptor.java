package com.readdle.codegen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SwiftFuncDescriptor {

    @NonNull
    public final String name;

    public final boolean isStatic;

    @Nullable
    private final String returnSwiftType;
    private final boolean isReturnTypeOptional;

    @Nullable
    private final String description;

    private List<SwiftParamDescriptor> params = new LinkedList<>();

    private SwiftFuncDescriptor(Builder builder) {
        name = builder.name;
        isStatic = builder.isStatic;
        returnSwiftType = builder.returnSwiftType;
        isReturnTypeOptional = builder.isReturnTypeOptional;
        description = builder.description;
        params = builder.params;
    }

    public static Builder newBuilder(String name) {
        return new Builder(name);
    }

    public static Builder newBuilder(SwiftFuncDescriptor copy) {
        Builder builder = new Builder(copy.name);
        builder.isStatic = copy.isStatic;
        builder.returnSwiftType = copy.returnSwiftType;
        builder.isReturnTypeOptional = copy.isReturnTypeOptional;
        builder.description = copy.description;
        builder.params = copy.params;
        return builder;
    }

    public static final class Builder {
        private String name;
        private boolean isStatic = false;
        private String returnSwiftType = null;
        private boolean isReturnTypeOptional = false;
        private String description = null;
        private List<SwiftParamDescriptor> params = new LinkedList<>();

        private Builder(String name) {
            this.name = name;
        }

        public Builder setIsStatic(boolean val) {
            isStatic = val;
            return this;
        }

        public Builder setReturnSwiftType(String val) {
            returnSwiftType = val;
            return this;
        }

        public Builder setIsReturnTypeOptional(boolean val) {
            isReturnTypeOptional = val;
            return this;
        }

        public Builder setDescription(String val) {
            description = val;
            return this;
        }

        public Builder addParams(SwiftParamDescriptor paramDescriptor) {
            params.add(paramDescriptor);
            return this;
        }

        public SwiftFuncDescriptor build() {
            return new SwiftFuncDescriptor(this);
        }
    }
}
