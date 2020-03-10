package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class IntTestStruct(var zero: Int = 0,
                         var max: Int = Int.MAX_VALUE,
                         var min: Int = Int.MIN_VALUE,
                         var optional: Int? = 0,
                         var optionalNil: Int? = null)

@SwiftReference
class IntTest private constructor() {

    @SwiftDelegate(protocols = ["IntTestParamProtocol"])
    interface IntParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Int): Boolean
    }

    @SwiftDelegate(protocols = ["IntTestReturnTypeProtocol"])
    interface IntReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Int
    }

    @SwiftDelegate(protocols = ["IntTestOptionalParamProtocol"])
    interface IntOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Int?): Boolean
    }

    @SwiftDelegate(protocols = ["IntTestOptionalReturnTypeProtocol"])
    interface IntOptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Int?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Int

        @JvmStatic
        external fun testMin(): Int

        @JvmStatic
        external fun testMax(): Int

        @JvmStatic
        external fun testParam(param: Int): Boolean

        @JvmStatic
        external fun testReturnType(): Int

        @JvmStatic
        external fun testOptionalParam(param: Int?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Int?

        @JvmStatic
        external fun testProtocolParam(callback: IntParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: IntReturnTypeProtocol): Int

        @JvmStatic
        external fun testProtocolOptionalParam(callback: IntOptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: IntOptionalReturnTypeProtocol): Int?

        @JvmStatic
        external fun testEncode(): IntTestStruct

        @JvmStatic
        external fun testDecode(value: IntTestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}