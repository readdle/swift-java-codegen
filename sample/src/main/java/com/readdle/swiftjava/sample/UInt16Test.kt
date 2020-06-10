package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

private const val MIN_VALUE: Short = 0
private const val MAX_VALUE: Short = -1

@SwiftValue
enum class UInt16Enum(@Unsigned val rawValue: Short) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Short, UInt16Enum>()

        @JvmStatic
        fun valueOf(rawValue: Short): UInt16Enum {
            return values[rawValue]!!
        }

        init {
            enumValues<UInt16Enum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class UInt16OptionsSet(@Unsigned val rawValue: Short = 0) {
    companion object {
        @JvmStatic
        val  one = UInt16OptionsSet(1)
        @JvmStatic
        val  two = UInt16OptionsSet(2)
        @JvmStatic
        val  three = UInt16OptionsSet(4)
    }
}

@SwiftValue
data class UInt16TestStruct(@Unsigned var zero: Short = 0,
                            @Unsigned var max: Short = MAX_VALUE,
                            @Unsigned var min: Short = MIN_VALUE,
                            @Unsigned var optional: Short? = 0,
                            @Unsigned var optionalNil: Short? = null)

@SwiftReference
class UInt16Test private constructor() {

    @SwiftDelegate(protocols = ["UInt16TestParamProtocol"])
    interface UInt16ParamProtocol {
        @SwiftCallbackFunc
        fun testParam(@Unsigned param: Short): Boolean
    }

    @SwiftDelegate(protocols = ["UInt16TestReturnTypeProtocol"])
    interface UInt16ReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testReturnType(): Short
    }

    @SwiftDelegate(protocols = ["UInt16TestOptionalParamProtocol"])
    interface UInt16OptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(@Unsigned param: Short?): Boolean
    }

    @SwiftDelegate(protocols = ["UInt16TestOptionalReturnTypeProtocol"])
    interface UInt16OptionalReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testOptionalReturnType(): Short?
    }

    @SwiftBlock("(UInt16) -> UInt16")
    interface UInt16Block {
        @Unsigned
        fun call(@Unsigned value: Short): Short
    }

    @SwiftBlock("(UInt16?) -> UInt16?")
    interface OptionalUInt16Block {
        @Unsigned
        fun call(@Unsigned value: Short?): Short?
    }

    companion object {
        @JvmStatic @Unsigned
        external fun testZero(): Short

        @JvmStatic @Unsigned
        external fun testMin(): Short

        @JvmStatic @Unsigned
        external fun testMax(): Short

        @JvmStatic
        external fun testParam(@Unsigned param: Short): Boolean

        @JvmStatic @Unsigned
        external fun testReturnType(): Short

        @JvmStatic
        external fun testOptionalParam(@Unsigned param: Short?): Boolean

        @JvmStatic @Unsigned
        external fun testOptionalReturnType(): Short?

        @JvmStatic
        external fun testProtocolParam(callback: UInt16ParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: UInt16ReturnTypeProtocol): Short

        @JvmStatic
        external fun testProtocolOptionalParam(callback: UInt16OptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: UInt16OptionalReturnTypeProtocol): Short?

        @JvmStatic
        external fun testEncode():  UInt16TestStruct

        @JvmStatic
        external fun testDecode(value: UInt16TestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(@Unsigned rawValue: Short) : UInt16Enum

        @JvmStatic @Unsigned
        external fun testEnumDecode(enum: UInt16Enum) : Short

        @JvmStatic
        external fun testOptionSetEncode(@Unsigned rawValue: Short) : UInt16OptionsSet

        @JvmStatic @Unsigned
        external fun testOptionSetDecode(enum: UInt16OptionsSet) : Short
        
        @JvmStatic
        external fun testBlock(@SwiftBlock block: UInt16Block): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalUInt16Block): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}