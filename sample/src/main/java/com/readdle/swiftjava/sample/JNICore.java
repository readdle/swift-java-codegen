package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftGetter;
import com.readdle.codegen.anotation.SwiftReference;

@SwiftReference
public class JNICore {

    // Swift JNICore private native pointer
    private long nativePointer = 0L;

    // Swift JNICore private constructor
    // Should be private. Don't call this constructor from Java!
    private JNICore() {}

    // Swift JNICore release method
    private native void release();

    public native void dumpReferenceTables();

    @SwiftGetter("instance")
    public static native JNICore getJNI();

}
