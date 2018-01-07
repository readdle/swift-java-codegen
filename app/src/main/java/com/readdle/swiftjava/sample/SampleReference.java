package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftParamName;
import com.readdle.codegen.anotation.SwiftReference;

import android.support.annotation.NonNull;
import android.util.Log;

@SwiftReference(importPackages = {"SampleAppCore"})
public class SampleReference {

    private long nativePointer = 0L;

    // Swift JNI private constructor
    private SampleReference(long nativePointer) {
        this.nativePointer = nativePointer;
    }

    public SampleReference() {
        this.nativePointer = SampleReference.init().nativePointer;
    }

    public native void retain();

    public native void release();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (nativePointer != 0) {
            Log.w("Leak", "Looks like your forgot to release " + SampleReference.class.getSimpleName());
        }
    }

    @SwiftFunc
    private static native SampleReference init();

    @SwiftFunc
    public native SampleValue getRandomValue();

    @SwiftFunc
    public native void saveValue(SampleValue value);

    @SwiftFunc
    public native void funcThrows() throws SwiftError;
}
