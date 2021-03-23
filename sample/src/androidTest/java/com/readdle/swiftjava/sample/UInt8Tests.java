package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UInt8Tests {

    private static final byte MIN_VALUE = 0;
    private static final byte MAX_VALUE = -1;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(UInt8Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(UInt8Test.testMin(), MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(UInt8Test.testMax(), MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(UInt8Test.testParam(MAX_VALUE));
        Assert.assertFalse(UInt8Test.testParam(MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(UInt8Test.testReturnType(), MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(UInt8Test.testOptionalParam(MAX_VALUE));
        Assert.assertFalse(UInt8Test.testOptionalParam(MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Byte result = UInt8Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = UInt8Test.testProtocolParam(param -> param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        byte result = UInt8Test.testProtocolReturnType(() -> (byte) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = UInt8Test.testProtocolOptionalParam(param -> param != null && param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Byte result = UInt8Test.testProtocolOptionalReturnType(() -> (byte) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), 42);
    }

    @Test
    public void testEncode() {
        UInt8TestStruct result = UInt8Test.testEncode();
        Assert.assertEquals(result, new UInt8TestStruct());
    }

    @Test
    public void testDecode() {
        UInt8TestStruct goodParam = new UInt8TestStruct();
        UInt8TestStruct badParam = new UInt8TestStruct((byte) 42, (byte) 42, (byte) 42, (byte) 42, (byte) 42);
        Assert.assertTrue(UInt8Test.testDecode(goodParam));
        Assert.assertFalse(UInt8Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(UInt8Enum.ONE,UInt8Test.testEnumEncode(UInt8Enum.ONE.getRawValue()));
        Assert.assertEquals(UInt8Enum.TWO,UInt8Test.testEnumEncode(UInt8Enum.TWO.getRawValue()));
        Assert.assertEquals(UInt8Enum.THREE,UInt8Test.testEnumEncode(UInt8Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(UInt8Enum.ONE.getRawValue(),UInt8Test.testEnumDecode(UInt8Enum.ONE));
        Assert.assertEquals(UInt8Enum.TWO.getRawValue(),UInt8Test.testEnumDecode(UInt8Enum.TWO));
        Assert.assertEquals(UInt8Enum.THREE.getRawValue(),UInt8Test.testEnumDecode(UInt8Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(UInt8OptionsSet.getOne(),UInt8Test.testOptionSetEncode(UInt8OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(UInt8OptionsSet.getTwo(),UInt8Test.testOptionSetEncode(UInt8OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(UInt8OptionsSet.getThree(),UInt8Test.testOptionSetEncode(UInt8OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(UInt8OptionsSet.getOne().getRawValue(),UInt8Test.testOptionSetDecode(UInt8OptionsSet.getOne()));
        Assert.assertEquals(UInt8OptionsSet.getTwo().getRawValue(),UInt8Test.testOptionSetDecode(UInt8OptionsSet.getTwo()));
        Assert.assertEquals(UInt8OptionsSet.getThree().getRawValue(),UInt8Test.testOptionSetDecode(UInt8OptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(UInt8Test.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(UInt8Test.testOptionalBlock(value -> value));
    }

}
