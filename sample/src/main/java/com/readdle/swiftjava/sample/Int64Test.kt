package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class Int64TestStruct(var zero: Long = 0,
                          var max: Long = Long.MAX_VALUE,
                          var min: Long = Long.MIN_VALUE,
                          var optional: Long? = 0,
                          var optionalNil: Long? = null)

@SwiftReference
class Int64Test private constructor() {

    @SwiftDelegate(protocols = ["Int64TestParamProtocol"])
    interface Int64ParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Long): Boolean
    }

    @SwiftDelegate(protocols = ["Int64TestReturnTypeProtocol"])
    interface Int64ReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Long
    }

    @SwiftDelegate(protocols = ["Int64TestOptionalParamProtocol"])
    interface Int64OptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Long?): Boolean
    }

    @SwiftDelegate(protocols = ["Int64TestOptionalReturnTypeProtocol"])
    interface Int64OptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Long?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Long

        @JvmStatic
        external fun testMin(): Long

        @JvmStatic
        external fun testMax(): Long

        @JvmStatic
        external fun testParam(param: Long): Boolean

        @JvmStatic
        external fun testReturnType(): Long

        @JvmStatic
        external fun testOptionalParam(param: Long?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Long?

        @JvmStatic
        external fun testProtocolParam(callback: Int64ParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: Int64ReturnTypeProtocol): Long

        @JvmStatic
        external fun testProtocolOptionalParam(callback: Int64OptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: Int64OptionalReturnTypeProtocol): Long?

        @JvmStatic
        external fun testEncode():  Int64TestStruct

        @JvmStatic
        external fun testDecode(value: Int64TestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}