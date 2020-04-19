package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class FloatTestStruct(var zero: Float = 0f,
                           var infinity: Float = Float.POSITIVE_INFINITY,
                           var negativeInfinity: Float = Float.NEGATIVE_INFINITY,
                           var optional: Float? = 0f,
                           var optionalNil: Float? = null)

@SwiftReference
class FloatTest private constructor() {

    @SwiftDelegate(protocols = ["FloatTestParamProtocol"])
    interface FloatParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Float): Boolean
    }

    @SwiftDelegate(protocols = ["FloatTestReturnTypeProtocol"])
    interface FloatReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Float
    }

    @SwiftDelegate(protocols = ["FloatTestOptionalParamProtocol"])
    interface FloatOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Float?): Boolean
    }

    @SwiftDelegate(protocols = ["FloatTestOptionalReturnTypeProtocol"])
    interface FloatOptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Float?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Float

        @JvmStatic
        external fun testInfinite(): Float

        @JvmStatic
        external fun testNan(): Float

        @JvmStatic
        external fun testParam(param: Float): Boolean

        @JvmStatic
        external fun testReturnType(): Float

        @JvmStatic
        external fun testOptionalParam(param: Float?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Float?

        @JvmStatic
        external fun testProtocolParam(callback: FloatParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: FloatReturnTypeProtocol): Float

        @JvmStatic
        external fun testProtocolOptionalParam(callback: FloatOptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: FloatOptionalReturnTypeProtocol): Float?

        @JvmStatic
        external fun testEncode():  FloatTestStruct

        @JvmStatic
        external fun testDecode(value: FloatTestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}