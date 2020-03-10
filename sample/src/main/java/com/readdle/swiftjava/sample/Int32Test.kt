package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class Int32TestStruct(var zero: Int = 0,
                           var max: Int = Int.MAX_VALUE,
                           var min: Int = Int.MIN_VALUE,
                           var optional: Int? = 0,
                           var optionalNil: Int? = null)

@SwiftReference
class Int32Test private constructor() {

    companion object {

        @JvmStatic
        external fun testEncode(): Int32TestStruct

        @JvmStatic
        external fun testDecode(value: Int32TestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}