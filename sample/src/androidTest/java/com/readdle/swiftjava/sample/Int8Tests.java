package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Int8Tests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(Int8Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(Int8Test.testMin(), Byte.MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(Int8Test.testMax(), Byte.MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(Int8Test.testParam(Byte.MAX_VALUE));
        Assert.assertFalse(Int8Test.testParam(Byte.MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(Int8Test.testReturnType(), Byte.MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(Int8Test.testOptionalParam(Byte.MAX_VALUE));
        Assert.assertFalse(Int8Test.testOptionalParam(Byte.MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Byte result = Int8Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), Byte.MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = Int8Test.testProtocolParam(param -> param == Byte.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        byte result = Int8Test.testProtocolReturnType(() -> (byte) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = Int8Test.testProtocolOptionalParam(param -> param != null && param == Byte.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Byte result = Int8Test.testProtocolOptionalReturnType(() -> (byte) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), 42);
    }

    @Test
    public void testEncode() {
        Int8TestStruct result = Int8Test.testEncode();
        Assert.assertEquals(result, new Int8TestStruct());
    }

    @Test
    public void testDecode() {
        Int8TestStruct goodParam = new Int8TestStruct();
        Int8TestStruct badParam = new Int8TestStruct((byte) 42, (byte) 42, (byte) 42, (byte) 42, (byte) 42);
        Assert.assertTrue(Int8Test.testDecode(goodParam));
        Assert.assertFalse(Int8Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(Int8Enum.ONE, Int8Test.testEnumEncode(Int8Enum.ONE.getRawValue()));
        Assert.assertEquals(Int8Enum.TWO, Int8Test.testEnumEncode(Int8Enum.TWO.getRawValue()));
        Assert.assertEquals(Int8Enum.THREE, Int8Test.testEnumEncode(Int8Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(Int8Enum.ONE.getRawValue(), Int8Test.testEnumDecode(Int8Enum.ONE));
        Assert.assertEquals(Int8Enum.TWO.getRawValue(), Int8Test.testEnumDecode(Int8Enum.TWO));
        Assert.assertEquals(Int8Enum.THREE.getRawValue(), Int8Test.testEnumDecode(Int8Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(Int8OptionsSet.getOne(), Int8Test.testOptionSetEncode(Int8OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(Int8OptionsSet.getTwo(), Int8Test.testOptionSetEncode(Int8OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(Int8OptionsSet.getThree(), Int8Test.testOptionSetEncode(Int8OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(Int8OptionsSet.getOne().getRawValue(), Int8Test.testOptionSetDecode(Int8OptionsSet.getOne()));
        Assert.assertEquals(Int8OptionsSet.getTwo().getRawValue(), Int8Test.testOptionSetDecode(Int8OptionsSet.getTwo()));
        Assert.assertEquals(Int8OptionsSet.getThree().getRawValue(), Int8Test.testOptionSetDecode(Int8OptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(Int8Test.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(Int8Test.testOptionalBlock(value -> value));
    }

}
