package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftReference
import java.lang.annotation.Native

@SwiftReference
class SwiftEnvironment private constructor() {

    @Native
    private val nativePointer = 0L // Swift SwiftEnvironment private native pointer

    private external fun release() // Swift SwiftEnvironment release method

    companion object {

        @JvmStatic
        external fun initEnvironment()
    }

}
