package com.readdle.swiftjava.sample;

import android.support.annotation.NonNull;

import com.readdle.codegen.anotation.SwiftReference;

import java.lang.annotation.Native;

// This is protocol on swift side
@SwiftReference
public class Storage {
    @Native
    private long nativePointer = 0L;

    private Storage() {
    }

    public native void release();

    @NonNull
    public static native Storage init();

    native void add(@NonNull String str);
}

