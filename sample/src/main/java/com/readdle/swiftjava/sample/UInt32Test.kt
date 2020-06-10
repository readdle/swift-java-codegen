package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

private const val MIN_VALUE: Int = 0
private const val MAX_VALUE: Int = -1

@SwiftValue
enum class UInt32Enum(@Unsigned val rawValue: Int) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Int, UInt32Enum>()

        @JvmStatic
        fun valueOf(rawValue: Int): UInt32Enum {
            return values[rawValue]!!
        }

        init {
            enumValues<UInt32Enum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class UInt32OptionsSet(@Unsigned val rawValue: Int = 0) {
    companion object {
        @JvmStatic
        val  one = UInt32OptionsSet(1)
        @JvmStatic
        val  two = UInt32OptionsSet(2)
        @JvmStatic
        val  three = UInt32OptionsSet(4)
    }
}

@SwiftValue
data class UInt32TestStruct(@Unsigned var zero: Int = 0,
                            @Unsigned var max: Int = MAX_VALUE,
                            @Unsigned var min: Int = MIN_VALUE,
                            @Unsigned var optional: Int? = 0,
                            @Unsigned var optionalNil: Int? = null)

@SwiftReference
class UInt32Test private constructor() {

    companion object {

        @JvmStatic
        external fun testEncode():  UInt32TestStruct

        @JvmStatic
        external fun testDecode(value: UInt32TestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(@Unsigned rawValue: Int) : UInt32Enum

        @JvmStatic @Unsigned
        external fun testEnumDecode(enum: UInt32Enum) : Int

        @JvmStatic
        external fun testOptionSetEncode(@Unsigned rawValue: Int) : UInt32OptionsSet

        @JvmStatic @Unsigned
        external fun testOptionSetDecode(enum: UInt32OptionsSet) : Int
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}