package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Int32Tests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testEncode() {
        Int32TestStruct result = Int32Test.testEncode();
        Assert.assertEquals(result, new Int32TestStruct());
    }

    @Test
    public void testDecode() {
        Int32TestStruct goodParam = new Int32TestStruct();
        Int32TestStruct badParam = new Int32TestStruct(42, 42, 42, 42, 42);
        Assert.assertTrue(Int32Test.testDecode(goodParam));
        Assert.assertFalse(Int32Test.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(Int32Enum.ONE, Int32Test.testEnumEncode(Int32Enum.ONE.getRawValue()));
        Assert.assertEquals(Int32Enum.TWO, Int32Test.testEnumEncode(Int32Enum.TWO.getRawValue()));
        Assert.assertEquals(Int32Enum.THREE, Int32Test.testEnumEncode(Int32Enum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(Int32Enum.ONE.getRawValue(), Int32Test.testEnumDecode(Int32Enum.ONE));
        Assert.assertEquals(Int32Enum.TWO.getRawValue(), Int32Test.testEnumDecode(Int32Enum.TWO));
        Assert.assertEquals(Int32Enum.THREE.getRawValue(), Int32Test.testEnumDecode(Int32Enum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(Int32OptionsSet.getOne(), Int32Test.testOptionSetEncode(Int32OptionsSet.getOne().getRawValue()));
        Assert.assertEquals(Int32OptionsSet.getTwo(), Int32Test.testOptionSetEncode(Int32OptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(Int32OptionsSet.getThree(), Int32Test.testOptionSetEncode(Int32OptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(Int32OptionsSet.getOne().getRawValue(), Int32Test.testOptionSetDecode(Int32OptionsSet.getOne()));
        Assert.assertEquals(Int32OptionsSet.getTwo().getRawValue(), Int32Test.testOptionSetDecode(Int32OptionsSet.getTwo()));
        Assert.assertEquals(Int32OptionsSet.getThree().getRawValue(), Int32Test.testOptionSetDecode(Int32OptionsSet.getThree()));
    }

}
