package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftReference;

@SwiftReference
public class SwiftEnvironment {

    // Swift SwiftEnvironment private native pointer
    private long nativePointer = 0L;

    // Swift SwiftEnvironment private constructor
    // Should be private. Don't call this constructor from Java!
    private SwiftEnvironment() {}

    // Swift SwiftEnvironment release method
    private native void release();

    public static native void initEnvironment();

}
