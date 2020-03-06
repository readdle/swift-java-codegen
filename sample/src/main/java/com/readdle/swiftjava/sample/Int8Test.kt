package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class Int8TestStruct(var zero: Byte = 0,
                          var max: Byte = Byte.MAX_VALUE,
                          var min: Byte = Byte.MIN_VALUE,
                          var optional: Byte? = 0,
                          var optionalNil: Byte? = null)

@SwiftReference
class Int8Test private constructor() {

    @SwiftDelegate(protocols = ["Int8TestParamProtocol"])
    interface Int8ParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Byte): Boolean
    }

    @SwiftDelegate(protocols = ["Int8TestReturnTypeProtocol"])
    interface Int8ReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Byte
    }

    @SwiftDelegate(protocols = ["Int8TestOptionalParamProtocol"])
    interface Int8OptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Byte?): Boolean
    }

    @SwiftDelegate(protocols = ["Int8TestOptionalReturnTypeProtocol"])
    interface Int8OptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Byte?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Byte

        @JvmStatic
        external fun testMin(): Byte

        @JvmStatic
        external fun testMax(): Byte

        @JvmStatic
        external fun testParam(param: Byte): Boolean

        @JvmStatic
        external fun testReturnType(): Byte

        @JvmStatic
        external fun testOptionalParam(param: Byte?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Byte?

        @JvmStatic
        external fun testProtocolParam(callback: Int8ParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: Int8ReturnTypeProtocol): Byte

        @JvmStatic
        external fun testProtocolOptionalParam(callback: Int8OptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: Int8OptionalReturnTypeProtocol): Byte?

        @JvmStatic
        external fun testEncode():  Int8TestStruct

        @JvmStatic
        external fun testDecode(value: Int8TestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}