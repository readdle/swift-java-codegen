package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftGetter
import com.readdle.codegen.anotation.SwiftReference
import java.lang.annotation.Native

@SwiftReference
class JNICore private constructor() {

    // Swift JNICore private native pointer
    @Native
    private val nativePointer = 0L

    // Swift JNICore release method
    private external fun release()

    external fun dumpReferenceTables()

    companion object {

        @JvmStatic
        val JNI: JNICore?
            @SwiftGetter("instance")
            external get
    }

}
