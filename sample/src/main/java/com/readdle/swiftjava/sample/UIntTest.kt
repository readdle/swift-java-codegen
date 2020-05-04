package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

private const val MIN_VALUE: Int = 0
private const val MAX_VALUE: Int = -1

@SwiftValue
enum class UIntEnum(@Unsigned val rawValue: Int) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Int, UIntEnum>()

        @JvmStatic
        fun valueOf(rawValue: Int): UIntEnum {
            return values[rawValue]!!
        }

        init {
            enumValues<UIntEnum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class UIntOptionsSet(@Unsigned val rawValue: Int = 0) {
    companion object {
        @JvmStatic
        val  one = UIntOptionsSet(1)
        @JvmStatic
        val  two = UIntOptionsSet(2)
        @JvmStatic
        val  three = UIntOptionsSet(4)
    }
}


@SwiftValue
data class UIntTestStruct(@Unsigned var zero: Int = 0,
                          @Unsigned var max: Int = MAX_VALUE,
                          @Unsigned var min: Int = MIN_VALUE,
                          @Unsigned var optional: Int? = 0,
                          @Unsigned var optionalNil: Int? = null)

@SwiftReference
class UIntTest private constructor() {

    @SwiftDelegate(protocols = ["UIntTestParamProtocol"])
    interface UIntParamProtocol {
        @SwiftCallbackFunc
        fun testParam(@Unsigned param: Int): Boolean
    }

    @SwiftDelegate(protocols = ["UIntTestReturnTypeProtocol"])
    interface UIntReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testReturnType(): Int
    }

    @SwiftDelegate(protocols = ["UIntTestOptionalParamProtocol"])
    interface UIntOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(@Unsigned param: Int?): Boolean
    }

    @SwiftDelegate(protocols = ["UIntTestOptionalReturnTypeProtocol"])
    interface UIntOptionalReturnTypeProtocol {
        @SwiftCallbackFunc @Unsigned
        fun testOptionalReturnType(): Int?
    }

    @SwiftBlock("(UInt) -> UInt")
    interface UIntBlock {
        @Unsigned
        fun call(@Unsigned value: Int): Int
    }

    @SwiftBlock("(UInt?) -> UInt?")
    interface OptionalUIntBlock {
        @Unsigned
        fun call(@Unsigned value: Int?): Int?
    }

    companion object {
        @JvmStatic @Unsigned
        external fun testZero(): Int

        @JvmStatic @Unsigned
        external fun testMin(): Int

        @JvmStatic @Unsigned
        external fun testMax(): Int

        @JvmStatic
        external fun testParam(@Unsigned param: Int): Boolean

        @JvmStatic @Unsigned
        external fun testReturnType(): Int

        @JvmStatic
        external fun testOptionalParam(@Unsigned param: Int?): Boolean

        @JvmStatic @Unsigned
        external fun testOptionalReturnType(): Int?

        @JvmStatic
        external fun testProtocolParam(callback: UIntParamProtocol): Boolean

        @JvmStatic @Unsigned
        external fun testProtocolReturnType(callback: UIntReturnTypeProtocol): Int

        @JvmStatic
        external fun testProtocolOptionalParam(callback: UIntOptionalParamProtocol): Boolean

        @JvmStatic @Unsigned
        external fun testProtocolOptionalReturnType(callback: UIntOptionalReturnTypeProtocol): Int?

        @JvmStatic
        external fun testEncode(): UIntTestStruct

        @JvmStatic
        external fun testDecode(value: UIntTestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(@Unsigned rawValue: Int) : UIntEnum

        @JvmStatic @Unsigned
        external fun testEnumDecode(enum: UIntEnum) : Int

        @JvmStatic
        external fun testOptionSetEncode(@Unsigned rawValue: Int) : UIntOptionsSet

        @JvmStatic @Unsigned
        external fun testOptionSetDecode(enum: UIntOptionsSet) : Int

        @JvmStatic
        external fun testBlock(@SwiftBlock block: UIntBlock): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalUIntBlock): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}