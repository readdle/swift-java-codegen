package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftError
import com.readdle.codegen.anotation.SwiftFunc
import com.readdle.codegen.anotation.SwiftValue
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

@SwiftValue
open class SampleValue protected constructor() {

    @JvmField
    var string: String = ""

    var integer: Int = 0
    var int8: Byte = 0
    var int16: Short = 0
    var int32: Int = 0
    var int64: Long = 0

    var uint: UInt = 0U
    var uint8: UByte = 0U
    var uint16: UShort = 0U
    var uint32: UInt = 0U
    var uint64: ULong = 0U

    var optionalInteger: Int? = 0
    var optionalInt8: Byte? = 0
    var optionalInt16: Short? = 0
    var optionalInt32: Int? = 0
    var optionalInt64: Long? = 0

    var optionalUint: UInt? = 0U
    var optionalUint8: UByte? = 0U
    var optionalUint16: UShort? = 0U
    var optionalUint32: UInt? = 0U
    var optionalUint64: ULong? = 0U

    var objectArray: ArrayList<SampleValue> = ArrayList()
    var stringArray: ArrayList<String> = ArrayList()
    var numberArray: ArrayList<Int> = ArrayList()
    var arrayInArray: ArrayList<ArrayList<Int>> = ArrayList()
    var dictInArray: ArrayList<HashMap<Int, Int>> = ArrayList()
    var dictSampleClass: HashMap<String, SampleValue> = HashMap()
    var dictStrings: HashMap<String, String> = HashMap()
    var dictNumbers: HashMap<Int, Int> = HashMap()
    var dict64Numbers: HashMap<BigInteger, BigInteger> = HashMap()
    var dictInDict: HashMap<BigInteger, HashMap<BigInteger, BigInteger>> = HashMap()
    var arrayInDict: HashMap<BigInteger, ArrayList<BigInteger>> = HashMap()
    var set: HashSet<Int> = HashSet()
    var setValues: HashSet<SampleValue> = HashSet()

    external fun copy(): SampleValue

    external fun saveValue()

    @SwiftFunc("isSame(other:)")
    external fun isSame(other: SampleValue): Boolean

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SampleValue) return false
        return string == o.string
    }

    override fun hashCode(): Int {
        return Objects.hash(string)
    }

    override fun toString(): String {
        return "SampleValue(string='$string', " +
                "integer=$integer," +
                " int8=$int8, " +
                "int16=$int16, " +
                "int32=$int32, " +
                "int64=$int64, " +
                "uint=$uint, " +
                "uint8=$uint8, " +
                "uint16=$uint16, " +
                "uint32=$uint32, " +
                "uint64=$uint64, " +
                "optionalInteger=$optionalInteger, " +
                "optionalInt8=$optionalInt8, " +
                "optionalInt16=$optionalInt16, " +
                "optionalInt32=$optionalInt32, " +
                "optionalInt64=$optionalInt64, " +
                "optionalUint=$optionalUint, " +
                "optionalUint8=$optionalUint8, " +
                "optionalUint16=$optionalUint16, " +
                "optionalUint32=$optionalUint32, " +
                "optionalUint64=$optionalUint64, " +
                "objectArray=$objectArray, " +
                "stringArray=$stringArray, " +
                "numberArray=$numberArray, " +
                "arrayInArray=$arrayInArray, " +
                "dictInArray=$dictInArray, " +
                "dictSampleClass=$dictSampleClass, " +
                "dictStrings=$dictStrings, " +
                "dictNumbers=$dictNumbers, " +
                "dict64Numbers=$dict64Numbers, " +
                "dictInDict=$dictInDict, " +
                "arrayInDict=$arrayInDict, " +
                "set=$set, " +
                "setValues=$setValues)"
    }


    companion object {

        @JvmStatic
        external fun getRandomValue(): SampleValue

        @JvmStatic
        @Throws(SwiftError::class)
        external fun funcThrows()
    }
}