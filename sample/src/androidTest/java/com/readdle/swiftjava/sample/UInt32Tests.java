package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UInt32Tests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testEncode() {
        UInt32TestStruct result = UInt32Test.testEncode();
        Assert.assertEquals(result, new UInt32TestStruct());
    }

    @Test
    public void testDecode() {
        UInt32TestStruct goodParam = new UInt32TestStruct();
        UInt32TestStruct badParam = new UInt32TestStruct(42, 42, 42, 42, 42);
        Assert.assertTrue(UInt32Test.testDecode(goodParam));
        Assert.assertFalse(UInt32Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(UInt32Enum.ONE,UInt32Test.testEnumEncode(UInt32Enum.ONE.getRawValue()));
        Assert.assertEquals(UInt32Enum.TWO,UInt32Test.testEnumEncode(UInt32Enum.TWO.getRawValue()));
        Assert.assertEquals(UInt32Enum.THREE,UInt32Test.testEnumEncode(UInt32Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(UInt32Enum.ONE.getRawValue(),UInt32Test.testEnumDecode(UInt32Enum.ONE));
        Assert.assertEquals(UInt32Enum.TWO.getRawValue(),UInt32Test.testEnumDecode(UInt32Enum.TWO));
        Assert.assertEquals(UInt32Enum.THREE.getRawValue(),UInt32Test.testEnumDecode(UInt32Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(UInt32OptionsSet.getOne(),UInt32Test.testOptionSetEncode(UInt32OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(UInt32OptionsSet.getTwo(),UInt32Test.testOptionSetEncode(UInt32OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(UInt32OptionsSet.getThree(),UInt32Test.testOptionSetEncode(UInt32OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(UInt32OptionsSet.getOne().getRawValue(),UInt32Test.testOptionSetDecode(UInt32OptionsSet.getOne()));
        Assert.assertEquals(UInt32OptionsSet.getTwo().getRawValue(),UInt32Test.testOptionSetDecode(UInt32OptionsSet.getTwo()));
        Assert.assertEquals(UInt32OptionsSet.getThree().getRawValue(),UInt32Test.testOptionSetDecode(UInt32OptionsSet.getThree()));
    }

}
