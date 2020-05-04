package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

@SwiftValue
data class BoolTestStruct(var yes: Boolean = true,
                          var no: Boolean = false,
                          var optional: Boolean? = true,
                          var optionalNil: Boolean? = null)

@SwiftReference
class BoolTest private constructor() {

    @SwiftDelegate(protocols = ["BoolTestParamProtocol"])
    interface BoolParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Boolean): Boolean
    }

    @SwiftDelegate(protocols = ["BoolTestReturnTypeProtocol"])
    interface BoolReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Boolean
    }

    @SwiftDelegate(protocols = ["BoolTestOptionalParamProtocol"])
    interface BoolOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Boolean?): Boolean
    }

    @SwiftDelegate(protocols = ["BoolTestOptionalReturnTypeProtocol"])
    interface BoolOptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Boolean?
    }

    @SwiftBlock("(Bool) -> Bool")
    interface BoolBlock {
        fun call(value: Boolean): Boolean
    }

    @SwiftBlock("(Bool?) -> Bool?")
    interface OptionalBoolBlock {
        fun call(value: Boolean?): Boolean?
    }

    companion object {
        @JvmStatic
        external fun testYes(): Boolean

        @JvmStatic
        external fun testNo(): Boolean

        @JvmStatic
        external fun testParam(param: Boolean): Boolean

        @JvmStatic
        external fun testReturnType(): Boolean

        @JvmStatic
        external fun testOptionalParam(param: Boolean?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Boolean?

        @JvmStatic
        external fun testProtocolParam(callback: BoolParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: BoolReturnTypeProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalParam(callback: BoolOptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: BoolOptionalReturnTypeProtocol): Boolean?

        @JvmStatic
        external fun testEncode(): BoolTestStruct

        @JvmStatic
        external fun testDecode(value: BoolTestStruct): Boolean

        @JvmStatic
        external fun testBlock(@SwiftBlock block: BoolBlock): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalBoolBlock): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}