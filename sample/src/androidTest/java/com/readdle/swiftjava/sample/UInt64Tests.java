package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UInt64Tests {

    private static final long MIN_VALUE = 0;
    private static final long MAX_VALUE = -1;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(UInt64Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(UInt64Test.testMin(), MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(UInt64Test.testMax(), MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(UInt64Test.testParam(MAX_VALUE));
        Assert.assertFalse(UInt64Test.testParam(MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(UInt64Test.testReturnType(), MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(UInt64Test.testOptionalParam(MAX_VALUE));
        Assert.assertFalse(UInt64Test.testOptionalParam(MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Long result = UInt64Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = UInt64Test.testProtocolParam(param -> param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        long result = UInt64Test.testProtocolReturnType(() -> (byte) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = UInt64Test.testProtocolOptionalParam(param -> param != null && param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Long result = UInt64Test.testProtocolOptionalReturnType(() -> (long) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.byteValue(), 42);
    }

    @Test
    public void testEncode() {
        UInt64TestStruct result = UInt64Test.testEncode();
        Assert.assertEquals(result, new UInt64TestStruct());
    }

    @Test
    public void testDecode() {
        UInt64TestStruct goodParam = new UInt64TestStruct();
        UInt64TestStruct badParam = new UInt64TestStruct((long) 42, (long) 42, (long) 42, (long) 42, (long) 42);
        Assert.assertTrue(UInt64Test.testDecode(goodParam));
        Assert.assertFalse(UInt64Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(UInt64Enum.ONE,UInt64Test.testEnumEncode(UInt64Enum.ONE.getRawValue()));
        Assert.assertEquals(UInt64Enum.TWO,UInt64Test.testEnumEncode(UInt64Enum.TWO.getRawValue()));
        Assert.assertEquals(UInt64Enum.THREE,UInt64Test.testEnumEncode(UInt64Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(UInt64Enum.ONE.getRawValue(),UInt64Test.testEnumDecode(UInt64Enum.ONE));
        Assert.assertEquals(UInt64Enum.TWO.getRawValue(),UInt64Test.testEnumDecode(UInt64Enum.TWO));
        Assert.assertEquals(UInt64Enum.THREE.getRawValue(),UInt64Test.testEnumDecode(UInt64Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(UInt64OptionsSet.getOne(),UInt64Test.testOptionSetEncode(UInt64OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(UInt64OptionsSet.getTwo(),UInt64Test.testOptionSetEncode(UInt64OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(UInt64OptionsSet.getThree(),UInt64Test.testOptionSetEncode(UInt64OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(UInt64OptionsSet.getOne().getRawValue(),UInt64Test.testOptionSetDecode(UInt64OptionsSet.getOne()));
        Assert.assertEquals(UInt64OptionsSet.getTwo().getRawValue(),UInt64Test.testOptionSetDecode(UInt64OptionsSet.getTwo()));
        Assert.assertEquals(UInt64OptionsSet.getThree().getRawValue(),UInt64Test.testOptionSetDecode(UInt64OptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(UInt64Test.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(UInt64Test.testOptionalBlock(value -> value));
    }

}
