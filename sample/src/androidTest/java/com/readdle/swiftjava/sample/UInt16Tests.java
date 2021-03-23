package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UInt16Tests {

    private static final short MIN_VALUE = 0;
    private static final short MAX_VALUE = -1;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(UInt16Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(UInt16Test.testMin(), MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(UInt16Test.testMax(), MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(UInt16Test.testParam(MAX_VALUE));
        Assert.assertFalse(UInt16Test.testParam(MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(UInt16Test.testReturnType(), MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(UInt16Test.testOptionalParam(MAX_VALUE));
        Assert.assertFalse(UInt16Test.testOptionalParam(MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Short result = UInt16Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = UInt16Test.testProtocolParam(param -> param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        short result = UInt16Test.testProtocolReturnType(() -> (byte) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = UInt16Test.testProtocolOptionalParam(param -> param != null && param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Short result = UInt16Test.testProtocolOptionalReturnType(() -> (short) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), 42);
    }

    @Test
    public void testEncode() {
        UInt16TestStruct result = UInt16Test.testEncode();
        Assert.assertEquals(result, new UInt16TestStruct());
    }

    @Test
    public void testDecode() {
        UInt16TestStruct goodParam = new UInt16TestStruct();
        UInt16TestStruct badParam = new UInt16TestStruct((short) 42, (short) 42, (short) 42, (short) 42, (short) 42);
        Assert.assertTrue(UInt16Test.testDecode(goodParam));
        Assert.assertFalse(UInt16Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(UInt16Enum.ONE,UInt16Test.testEnumEncode(UInt16Enum.ONE.getRawValue()));
        Assert.assertEquals(UInt16Enum.TWO,UInt16Test.testEnumEncode(UInt16Enum.TWO.getRawValue()));
        Assert.assertEquals(UInt16Enum.THREE,UInt16Test.testEnumEncode(UInt16Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(UInt16Enum.ONE.getRawValue(),UInt16Test.testEnumDecode(UInt16Enum.ONE));
        Assert.assertEquals(UInt16Enum.TWO.getRawValue(),UInt16Test.testEnumDecode(UInt16Enum.TWO));
        Assert.assertEquals(UInt16Enum.THREE.getRawValue(),UInt16Test.testEnumDecode(UInt16Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(UInt16OptionsSet.getOne(),UInt16Test.testOptionSetEncode(UInt16OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(UInt16OptionsSet.getTwo(),UInt16Test.testOptionSetEncode(UInt16OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(UInt16OptionsSet.getThree(),UInt16Test.testOptionSetEncode(UInt16OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(UInt16OptionsSet.getOne().getRawValue(),UInt16Test.testOptionSetDecode(UInt16OptionsSet.getOne()));
        Assert.assertEquals(UInt16OptionsSet.getTwo().getRawValue(),UInt16Test.testOptionSetDecode(UInt16OptionsSet.getTwo()));
        Assert.assertEquals(UInt16OptionsSet.getThree().getRawValue(),UInt16Test.testOptionSetDecode(UInt16OptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(UInt16Test.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(UInt16Test.testOptionalBlock(value -> value));
    }

}
