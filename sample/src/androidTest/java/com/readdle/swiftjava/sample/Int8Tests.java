package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

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

    @Test
    public void testProtocolReturnType() {
        byte result = Int8Test.testProtocolReturnType(() -> (byte) 42);
        Assert.assertEquals(result, 42);
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

}
