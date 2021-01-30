package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

@SwiftValue
enum class StringEnum(val rawValue: String) {

    ONE("ONE"), TWO("TWO"), THREE("THREE");

}

@SwiftValue
data class StringTestStruct(var zero: String = "",
                            var optional: String? = "42",
                            var optionalNil: String? = null)

@SwiftReference
class StringTest private constructor() {

    @SwiftDelegate(protocols = ["StringTestParamProtocol"])
    interface StringParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: String): Boolean
    }

    @SwiftDelegate(protocols = ["StringTestReturnTypeProtocol"])
    interface StringReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): String
    }

    @SwiftDelegate(protocols = ["StringTestOptionalParamProtocol"])
    interface StringOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: String?): Boolean
    }

    @SwiftDelegate(protocols = ["StringTestOptionalReturnTypeProtocol"])
    interface StringOptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): String?
    }

    @SwiftBlock("(String) -> String")
    interface StringBlock {
        fun call(value: String): String
    }

    @SwiftBlock("(String?) -> String?")
    interface OptionalStringBlock {
        fun call(value: String?): String?
    }

    companion object {
        @JvmStatic
        external fun testZero(): String

        @JvmStatic
        external fun testParam(param: String): Boolean

        @JvmStatic
        external fun testReturnType(): String

        @JvmStatic
        external fun testOptionalParam(param: String?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): String?

        @JvmStatic
        external fun testProtocolParam(callback: StringParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: StringReturnTypeProtocol): String

        @JvmStatic
        external fun testProtocolOptionalParam(callback: StringOptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: StringOptionalReturnTypeProtocol): String?

        @JvmStatic
        external fun testEncode(): StringTestStruct

        @JvmStatic
        external fun testDecode(value: StringTestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(rawValue: String) : StringEnum

        @JvmStatic
        external fun testEnumDecode(enum: StringEnum) : String

        @JvmStatic
        external fun testBlock(@SwiftBlock block: StringBlock): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalStringBlock): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}