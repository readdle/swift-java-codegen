package com.readdle.swiftjava.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.readdle.codegen.anotation.JavaSwift
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SampleEnumWithValueTests {

    @Before
    fun setUp() {
        System.loadLibrary("SampleAppCore")
        JavaSwift.init()
    }

    @Test
    fun testNone() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.None)
        Assert.assertTrue(value == SampleEnumWithValue.None)
    }

    @Test
    fun testStringValue() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.StringValue("42"))
        Assert.assertEquals((value as SampleEnumWithValue.StringValue).value, "42")
    }

    @Test
    fun testIntegerValue() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.IntegerValue(42))
        Assert.assertEquals((value as SampleEnumWithValue.IntegerValue).value, 42)
    }

    @Test
    fun testInt8Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Int8Value(42))
        Assert.assertEquals((value as SampleEnumWithValue.Int8Value).value.toInt(), 42)
    }

    @Test
    fun testInt16Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Int16Value(42))
        Assert.assertEquals((value as SampleEnumWithValue.Int16Value).value.toInt(), 42)
    }

    @Test
    fun testInt32Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Int32Value(42))
        Assert.assertEquals((value as SampleEnumWithValue.Int32Value).value, 42)
    }

    @Test
    fun testInt64Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Int64Value(42))
        Assert.assertEquals((value as SampleEnumWithValue.Int64Value).value, 42)
    }

    @Test
    fun testUIntValue() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.UintValue(42U))
        Assert.assertEquals((value as SampleEnumWithValue.UintValue).value, 42U)
    }

    @Test
    fun testUInt8Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Uint8Value(42U))
        Assert.assertEquals((value as SampleEnumWithValue.Uint8Value).value.toUInt(), 42U)
    }

    @Test
    fun testUInt16Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Uint16Value(42U))
        Assert.assertEquals((value as SampleEnumWithValue.Uint16Value).value.toUInt(), 42U)
    }

    @Test
    fun testUInt32Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Uint32Value(42U))
        Assert.assertEquals((value as SampleEnumWithValue.Uint32Value).value, 42U)
    }

    @Test
    fun testUInt64Value() {
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Uint64Value(42U))
        Assert.assertEquals((value as SampleEnumWithValue.Uint64Value).value.toUInt(), 42U)
    }

    @Test
    fun testObjectArrayValue() {
        val array = arrayListOf(SampleValue.getRandomValue())
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.ObjectArrayValue(array))
        Assert.assertEquals((value as SampleEnumWithValue.ObjectArrayValue).value, array)
    }

    @Test
    fun testStringArrayValue() {
        val array = arrayListOf("42")
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.StringArrayValue(array))
        Assert.assertEquals((value as SampleEnumWithValue.StringArrayValue).value, array)
    }

    @Test
    fun testNumberArrayValue() {
        val array = arrayListOf(42)
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.NumberArrayValue(array))
        Assert.assertEquals((value as SampleEnumWithValue.NumberArrayValue).value, array)
    }

    @Test
    fun testArrayInArrayValue() {
        val array = arrayListOf(arrayListOf(42))
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.ArrayInArrayValue(array))
        Assert.assertEquals((value as SampleEnumWithValue.ArrayInArrayValue).value, array)
    }

    @Test
    fun testDictInArrayValue() {
        val array = arrayListOf(hashMapOf(42 to 42))
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.DictInArrayValue(array))
        Assert.assertEquals((value as SampleEnumWithValue.DictInArrayValue).value, array)
    }

    @Test
    fun testDictSampleClassValue() {
        val dict = hashMapOf("abc" to SampleValue.getRandomValue())
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.DictSampleClassValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.DictSampleClassValue).value, dict)
    }

    @Test
    fun testDictStringsValue() {
        val dict = hashMapOf("abc" to "42")
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.DictStringsValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.DictStringsValue).value, dict)
    }

    @Test
    fun testDictNumbersValue() {
        val dict = hashMapOf(42 to 42)
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.DictNumbersValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.DictNumbersValue).value, dict)
    }

    @Test
    fun testDict64NumbersValue() {
        val dict = hashMapOf(10 to 10)
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.Dict64NumbersValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.Dict64NumbersValue).value, dict)
    }

    @Test
    fun testDictInDictValue() {
        val dict = hashMapOf(10 to hashMapOf(10 to 10))
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.DictInDictValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.DictInDictValue).value, dict)
    }

    @Test
    fun testArrayInDictValue() {
        val dict = hashMapOf(10 to arrayListOf(10, 10))
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.ArrayInDictValue(dict))
        Assert.assertEquals((value as SampleEnumWithValue.ArrayInDictValue).value, dict)
    }

    @Test
    fun testSetValue() {
        val set = hashSetOf(42)
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.SetValue(set))
        Assert.assertEquals((value as SampleEnumWithValue.SetValue).value, set)
    }

    @Test
    fun testSetValuesValue() {
        val set = hashSetOf(SampleValue.getRandomValue())
        val value = SampleEnumWithValue.copy(SampleEnumWithValue.SetValuesValue(set))
        Assert.assertEquals((value as SampleEnumWithValue.SetValuesValue).value, set)
    }

}