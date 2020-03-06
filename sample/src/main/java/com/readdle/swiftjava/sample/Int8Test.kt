package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

// TODO: fix it
//    @SwiftDelegate(protocols = ["Int8TestParamProtocol"])
//    interface ParamProtocol {
//        @SwiftCallbackFunc
//        fun testParam(param: Byte): Boolean
//    }

@SwiftDelegate(protocols = ["Int8TestReturnTypeProtocol"])
interface ReturnTypeProtocol {
    @SwiftCallbackFunc
    fun testReturnType(): Byte
}
// TODO: fix it
//    @SwiftDelegate(protocols = ["Int8TestOptionalParamProtocol"])
//    interface OptionalParamProtocol {
//        @SwiftCallbackFunc
//        fun testOptionalParam(param: Byte?): Boolean
//    }

@SwiftDelegate(protocols = ["Int8TestOptionalReturnTypeProtocol"])
interface OptionalReturnTypeProtocol {
    @SwiftCallbackFunc
    fun testOptionalReturnType(): Byte?
}

@SwiftValue
data class Int8TestStruct(var zero: Byte = 0,
                          var max: Byte = Byte.MAX_VALUE,
                          var min: Byte = Byte.MIN_VALUE,
                          var optional: Byte? = 0,
                          var optionalNil: Byte? = null)

@SwiftReference
class Int8Test private constructor() {

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

        // TODO: fix it
//        @JvmStatic
//        external fun testOptionalReturnType(): Byte?

        @JvmStatic
        external fun testProtocolReturnType(callback: ReturnTypeProtocol): Byte

        // TODO: fix it
//        @JvmStatic
//        external fun testProtocolOptionalReturnType(callback: OptionalReturnTypeProtocol): Byte?

        @JvmStatic
        external fun testEncode():  Int8TestStruct

        @JvmStatic
        external fun testDecode(value: Int8TestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}