package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftReference;

@SwiftReference(importPackages = {"SampleAppCore"})
public class SampleReference {

    // Swift JNI private native pointer
    private long nativePointer = 0L;

    // Swift JNI private constructor
    // Should be private. Don't call this constructor from Java!
    private SampleReference() {
    }

    // Swift JNI release method
    public native void release();

    @SwiftFunc
    public native SampleValue getRandomValue();

    @SwiftFunc
    public native void saveValue(SampleValue value);

    @SwiftFunc
    public native void funcThrows() throws SwiftError;

    @SwiftFunc
    public static native SampleReference init();

    @SwiftFunc
    public native void setDelegate(SampleDelegateAndroid delegate);

    @SwiftFunc
    public native Long tick();
}
