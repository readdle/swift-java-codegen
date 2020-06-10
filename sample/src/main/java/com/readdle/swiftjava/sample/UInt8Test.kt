package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

/*
 *  Byte representation in Int8 and UInt8
 *  Int8:    | 0 | 0 | 0 | 0 | 0 | 0 | 0 | Sign bit: 0 | = 0
 *  UInt8:   | 0 | 0 | 0 | 0 | 0 | 0 | 0 |           0 | = 0
 *  Int8:    | 1 | 1 | 1 | 1 | 1 | 1 | 1 | Sign bit: 0 | = 127
 *  UInt8:   | 1 | 1 | 1 | 1 | 1 | 1 | 1 |           0 | = 127
 *  Int8:    | 1 | 1 | 1 | 1 | 1 | 1 | 1 | Sign bit: 1 | = -1
 *  UInt8:   | 1 | 1 | 1 | 1 | 1 | 1 | 1 |           1 | = 255
 */
private const val MIN_VALUE: Byte = 0   // | 0 | 0 | 0 | 0 | 0 | 0 | 0 | Sign bit: 0 |
private const val MAX_VALUE: Byte = -1  // | 1 | 1 | 1 | 1 | 1 | 1 | 1 | Sign bit: 1 |

@SwiftValue
enum class UInt8Enum(@Unsigned val rawValue: Byte) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Byte, UInt8Enum>()

        @JvmStatic
        fun valueOf(rawValue: Byte): UInt8Enum {
            return values[rawValue]!!
        }

        init {
            enumValues<UInt8Enum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class UInt8OptionsSet(@Unsigned val rawValue: Byte = 0) {
    companion object {
        @JvmStatic
        val  one = UInt8OptionsSet(1)
        @JvmStatic
        val  two = UInt8OptionsSet(2)
        @JvmStatic
        val  three = UInt8OptionsSet(4)
    }
}

@SwiftValue
data class UInt8TestStruct(@Unsigned var zero: Byte = 0,
                           @Unsigned var max: Byte = MAX_VALUE,
                           @Unsigned var min: Byte = MIN_VALUE,
                           @Unsigned var optional: Byte? = 0,
                           @Unsigned var optionalNil: Byte? = null)

@SwiftReference
class UInt8Test private constructor() {

    @SwiftDelegate(protocols = ["UInt8TestParamProtocol"])
    interface UInt8ParamProtocol {
        @SwiftCallbackFunc
        fun testParam(@Unsigned param: Byte): Boolean
    }

    @SwiftDelegate(protocols = ["UInt8TestReturnTypeProtocol"])
    interface UInt8ReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testReturnType(): Byte
    }

    @SwiftDelegate(protocols = ["UInt8TestOptionalParamProtocol"])
    interface UInt8OptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(@Unsigned param: Byte?): Boolean
    }

    @SwiftDelegate(protocols = ["UInt8TestOptionalReturnTypeProtocol"])
    interface UInt8OptionalReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testOptionalReturnType(): Byte?
    }

    @SwiftBlock("(UInt8) -> UInt8")
    interface UInt8Block {
        @Unsigned
        fun call(@Unsigned value: Byte): Byte
    }

    @SwiftBlock("(UInt8?) -> UInt8?")
    interface OptionalUInt8Block {
        @Unsigned
        fun call(@Unsigned value: Byte?): Byte?
    }

    companion object {
        @JvmStatic @Unsigned
        external fun testZero(): Byte

        @JvmStatic @Unsigned
        external fun testMin(): Byte

        @JvmStatic @Unsigned
        external fun testMax(): Byte

        @JvmStatic
        external fun testParam(@Unsigned param: Byte): Boolean

        @JvmStatic @Unsigned
        external fun testReturnType(): Byte

        @JvmStatic
        external fun testOptionalParam(@Unsigned param: Byte?): Boolean

        @JvmStatic @Unsigned
        external fun testOptionalReturnType(): Byte?

        @JvmStatic
        external fun testProtocolParam(callback: UInt8ParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: UInt8ReturnTypeProtocol): Byte

        @JvmStatic
        external fun testProtocolOptionalParam(callback: UInt8OptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: UInt8OptionalReturnTypeProtocol): Byte?

        @JvmStatic
        external fun testEncode():  UInt8TestStruct

        @JvmStatic
        external fun testDecode(value: UInt8TestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(@Unsigned rawValue: Byte) : UInt8Enum

        @JvmStatic @Unsigned
        external fun testEnumDecode(enum: UInt8Enum) : Byte

        @JvmStatic
        external fun testOptionSetEncode(@Unsigned rawValue: Byte) : UInt8OptionsSet

        @JvmStatic @Unsigned
        external fun testOptionSetDecode(enum: UInt8OptionsSet) : Byte
        
        @JvmStatic
        external fun testBlock(@SwiftBlock block: UInt8Block): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalUInt8Block): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}