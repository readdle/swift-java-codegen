package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import java.lang.annotation.Native

@SwiftValue
enum class Int16Enum(val rawValue: Short) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Short, Int16Enum>()

        @JvmStatic
        fun valueOf(rawValue: Short): Int16Enum {
            return values[rawValue]!!
        }

        init {
            enumValues<Int16Enum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class Int16OptionsSet(val rawValue: Short = 0) {
    companion object {
        @JvmStatic
        val  one = Int16OptionsSet(1)
        @JvmStatic
        val  two = Int16OptionsSet(2)
        @JvmStatic
        val  three = Int16OptionsSet(4)
    }
}

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

    @SwiftBlock("(Int16) -> Int16")
    interface Int16Block {
        fun call(value: Short): Short
    }

    @SwiftBlock("(Int16?) -> Int16?")
    interface OptionalInt16Block {
        fun call(value: Short?): Short?
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
        external fun testEncode(): Int16TestStruct

        @JvmStatic
        external fun testDecode(value: Int16TestStruct): Boolean
        
        @JvmStatic
        external fun testEnumEncode(rawValue: Short) : Int16Enum

        @JvmStatic
        external fun testEnumDecode(enum: Int16Enum) : Short

        @JvmStatic
        external fun testOptionSetEncode(rawValue: Short) : Int16OptionsSet

        @JvmStatic
        external fun testOptionSetDecode(enum: Int16OptionsSet) : Short
        
        @JvmStatic
        external fun testBlock(@SwiftBlock block: Int16Block): Boolean

        @JvmStatic
        external fun testOptionalBlock(@SwiftBlock block: OptionalInt16Block): Boolean
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}