package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftParamName;
import com.readdle.codegen.anotation.SwiftValue;

import android.support.annotation.NonNull;

@SwiftValue(importPackages = {"SampleAppCore"})
public class SampleValue {

    @NonNull
    public String str1;
    @NonNull
    public String str2;
    @NonNull
    public String str3;

    // Swift JNI private constructor
    public SampleValue() {

    }

    @SwiftFunc
    public static native void initCoder();

    @SwiftFunc
    public static native SampleValue getRandomValue();

    @SwiftFunc
    public native void saveValue();

    @SwiftFunc
    public native Boolean isSame(@NonNull @SwiftParamName SampleValue other);

    @SwiftFunc
    public static native void funcThrows() throws SwiftError;
}
