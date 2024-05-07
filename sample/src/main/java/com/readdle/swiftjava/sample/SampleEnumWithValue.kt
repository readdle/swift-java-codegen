package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftFunc
import com.readdle.codegen.anotation.SwiftValue

@SwiftValue
sealed class SampleEnumWithValue {

    object None : SampleEnumWithValue()
    data class StringValue(val value: String) : SampleEnumWithValue()
    data class IntegerValue(val value: Int) : SampleEnumWithValue()
    data class Int8Value(val value: Byte) : SampleEnumWithValue()
    data class Int16Value(val value: Short) : SampleEnumWithValue()
    data class Int32Value(val value: Int) : SampleEnumWithValue()
    data class Int64Value(val value: Long) : SampleEnumWithValue()
    data class UintValue(val value: UInt) : SampleEnumWithValue()
    data class Uint8Value(val value: UByte) : SampleEnumWithValue()
    data class Uint16Value(val value: UShort) : SampleEnumWithValue()
    data class Uint32Value(val value: UInt) : SampleEnumWithValue()
    data class Uint64Value(val value: ULong) : SampleEnumWithValue()
    data class ObjectArrayValue(val value: ArrayList<SampleValue>) : SampleEnumWithValue()
    data class StringArrayValue(val value: ArrayList<String>) : SampleEnumWithValue()
    data class NumberArrayValue(val value: ArrayList<Int>) : SampleEnumWithValue()
    data class ArrayInArrayValue(val value: ArrayList<ArrayList<Int>>) : SampleEnumWithValue()
    data class DictInArrayValue(val value: ArrayList<HashMap<Int, Int>>) : SampleEnumWithValue()
    data class DictSampleClassValue(val value: HashMap<String, SampleValue>) : SampleEnumWithValue()
    data class DictStringsValue(val value: HashMap<String, String>) : SampleEnumWithValue()
    data class DictNumbersValue(val value: HashMap<Int, Int>) : SampleEnumWithValue()
    data class Dict64NumbersValue(val value: HashMap<Int, Int>) : SampleEnumWithValue()
    data class DictInDictValue(val value: HashMap<Int, HashMap<Int, Int>>) : SampleEnumWithValue()
    data class ArrayInDictValue(val value: HashMap<Int, ArrayList<Int>>) : SampleEnumWithValue()
    data class SetValue(val value: HashSet<Int>) : SampleEnumWithValue()
    data class SetValuesValue(val value: HashSet<SampleValue>) : SampleEnumWithValue()

    companion object {

        @JvmStatic
        @SwiftFunc("copy(value:)")
        external fun copy(value: SampleEnumWithValue): SampleEnumWithValue
    }
}