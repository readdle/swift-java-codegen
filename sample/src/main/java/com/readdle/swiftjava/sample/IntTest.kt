package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import com.readdle.codegen.anotation.SwiftReference
import com.readdle.codegen.anotation.SwiftValue
import java.lang.annotation.Native

@SwiftValue
enum class IntEnum(val rawValue: Int) {

    ONE(0), TWO(1), THREE(2);

    companion object {

        private val values = HashMap<Int, IntEnum>()

        @JvmStatic
        fun valueOf(rawValue: Int): IntEnum {
            return values[rawValue]!!
        }

        init {
            enumValues<IntEnum>().forEach {
                values[it.rawValue] = it
            }
        }
    }

}

@SwiftValue
data class IntOptionsSet(val rawValue: Int = 0) {
    companion object {
        @JvmStatic
        val  one = IntOptionsSet(1)
        @JvmStatic
        val  two = IntOptionsSet(2)
        @JvmStatic
        val  three = IntOptionsSet(4)
    }
}

@SwiftValue
data class IntTestStruct(var zero: Int = 0,
                         var max: Int = Int.MAX_VALUE,
                         var min: Int = Int.MIN_VALUE,
                         var optional: Int? = 0,
                         var optionalNil: Int? = null)

@SwiftReference
class IntTest private constructor() {

    @SwiftDelegate(protocols = ["IntTestParamProtocol"])
    interface IntParamProtocol {
        @SwiftCallbackFunc
        fun testParam(param: Int): Boolean
    }

    @SwiftDelegate(protocols = ["IntTestReturnTypeProtocol"])
    interface IntReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testReturnType(): Int
    }

    @SwiftDelegate(protocols = ["IntTestOptionalParamProtocol"])
    interface IntOptionalParamProtocol {
        @SwiftCallbackFunc
        fun testOptionalParam(param: Int?): Boolean
    }

    @SwiftDelegate(protocols = ["IntTestOptionalReturnTypeProtocol"])
    interface IntOptionalReturnTypeProtocol {
        @SwiftCallbackFunc
        fun testOptionalReturnType(): Int?
    }

    companion object {
        @JvmStatic
        external fun testZero(): Int

        @JvmStatic
        external fun testMin(): Int

        @JvmStatic
        external fun testMax(): Int

        @JvmStatic
        external fun testParam(param: Int): Boolean

        @JvmStatic
        external fun testReturnType(): Int

        @JvmStatic
        external fun testOptionalParam(param: Int?): Boolean

        @JvmStatic
        external fun testOptionalReturnType(): Int?

        @JvmStatic
        external fun testProtocolParam(callback: IntParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolReturnType(callback: IntReturnTypeProtocol): Int

        @JvmStatic
        external fun testProtocolOptionalParam(callback: IntOptionalParamProtocol): Boolean

        @JvmStatic
        external fun testProtocolOptionalReturnType(callback: IntOptionalReturnTypeProtocol): Int?

        @JvmStatic
        external fun testEncode(): IntTestStruct

        @JvmStatic
        external fun testDecode(value: IntTestStruct): Boolean

        @JvmStatic
        external fun testEnumEncode(rawValue: Int) : IntEnum

        @JvmStatic
        external fun testEnumDecode(enum: IntEnum) : Int

        @JvmStatic
        external fun testOptionSetEncode(rawValue: Int) : IntOptionsSet

        @JvmStatic
        external fun testOptionSetDecode(enum: IntOptionsSet) : Int
    }

    @Native
    var nativePointer: Long = 0

    external fun release()

}