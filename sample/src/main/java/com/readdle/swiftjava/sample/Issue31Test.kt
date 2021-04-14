package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftFunc
import com.readdle.codegen.anotation.SwiftGetter
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue

@SwiftValue
data class Issue31TestProgress(
        var elapsed: Int = 0,
        var total: Int = 1
) {
    @get:SwiftGetter
    val percentage: Double external get

    @SwiftFunc
    external fun calculatePercentage(): Double
}

@SwiftReference
class Issue31ReferenceTestProgress private constructor() {
    // Swift JNI private native pointer
    private val nativePointer = 0L

    // Swift JNI release method
    external fun release()

    @get:SwiftGetter
    val percentage: Double external get

    @SwiftFunc
    external fun calculatePercentage(): Double

    companion object {
        @JvmStatic
        @SwiftFunc("init(elapsed:total:)")
        external fun init(elapsed: Int, total: Int): Issue31ReferenceTestProgress
    }
}