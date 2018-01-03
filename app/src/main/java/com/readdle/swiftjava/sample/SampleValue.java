package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftMethod;
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
    private SampleValue() {

    }

    @SwiftMethod
    public static native SampleValue getRandomValue();

    @SwiftMethod
    public native void saveValue();
}
