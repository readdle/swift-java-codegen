package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
data class Int16TestStruct(var zero: Short = 0,
                          var max: Short = Short.MAX_VALUE,
                          var min: Short = Short.MIN_VALUE,
                          var optional: Short? = 0,
                          var optionalNil: Short? = null)

@SwiftReference
class Int16Test private constructor() {

    @SwiftDelegate(protocols = ["Int16TestParamProtocol"])
    interface Int16ParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Short): Boolean
    }

    @SwiftDelegate(protocols = ["Int16TestReturnTypeProtocol"])
    interface Int16ReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Short
    }

    @SwiftDelegate(protocols = ["Int16TestOptionalParamProtocol"])
    interface Int16OptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Short?): Boolean
    }

    @SwiftDelegate(protocols = ["Int16TestOptionalReturnTypeProtocol"])
    interface Int16OptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Short?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Short

        @JvmStatic
        external fun testMin(): Short

        @JvmStatic
        external fun testMax(): Short

        @JvmStatic
        external fun testParam(param: Short): Boolean

        @JvmStatic
        external fun testReturnType(): Short

        @JvmStatic
        external fun testOptionalParam(param: Short?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Short?

        @JvmStatic
        external fun testProtocolParam(callback: Int16ParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: Int16ReturnTypeProtocol): Short

        @JvmStatic
        external fun testProtocolOptionalParam(callback: Int16OptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: Int16OptionalReturnTypeProtocol): Short?

        @JvmStatic
        external fun testEncode():  Int16TestStruct

        @JvmStatic
        external fun testDecode(value: Int16TestStruct): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}