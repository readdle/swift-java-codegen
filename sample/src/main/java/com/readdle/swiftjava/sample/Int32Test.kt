package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
enum class Int32Enum(val rawValue: Int) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Int, Int32Enum>()

        @JvmStatic
        fun valueOf(rawValue: Int): Int32Enum {
            return values[rawValue]!!
        }

        init {
            enumValues<Int32Enum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class Int32OptionsSet(val rawValue: Int = 0) {
    companion object {
        @JvmStatic
        val  one = Int32OptionsSet(1)
        @JvmStatic
        val  two = Int32OptionsSet(2)
        @JvmStatic
        val  three = Int32OptionsSet(4)
    }
}

@SwiftValue
data class Int32TestStruct(var zero: Int = 0,
                           var max: Int = Int.MAX_VALUE,
                           var min: Int = Int.MIN_VALUE,
                           var optional: Int? = 0,
                           var optionalNil: Int? = null)

@SwiftReference
class Int32Test private constructor() {

    companion object {

        @JvmStatic
        external fun testEncode(): Int32TestStruct

        @JvmStatic
        external fun testDecode(value: Int32TestStruct): Boolean
        
        @JvmStatic
        external fun testEnumEncode(rawValue: Int) : Int32Enum

        @JvmStatic
        external fun testEnumDecode(enum: Int32Enum) : Int

        @JvmStatic
        external fun testOptionSetEncode(rawValue: Int) : Int32OptionsSet

        @JvmStatic
        external fun testOptionSetDecode(enum: Int32OptionsSet) : Int
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}