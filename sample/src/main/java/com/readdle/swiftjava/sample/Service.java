package com.readdle.swiftjava.sample;

import android.support.annotation.NonNull;

import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftReference;

import java.lang.annotation.Native;

@SwiftReference
public class Service {
    @Native
    private long nativePointer = 0L;

    private Service() {
    }

    public native void release();

    @NonNull
    public static native Service init();

    @SwiftFunc("register(provider:)")
    native static void registerProvider(@NonNull Provider provider);
}
