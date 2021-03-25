package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftReference

// Swift JNI private constructor
// Should be private. Don't call this constructor from Java!
@SwiftReference
class SwiftEnvironment private constructor() {

    // Swift JNI private native pointer
    private val nativePointer = 0L

    // Swift JNI release method
    external fun release()

    companion object {

        @JvmStatic
        external fun initEnvironment()

        @JvmStatic
        external fun is64BitArch(): Boolean
    }

}
