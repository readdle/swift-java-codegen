package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Int16Tests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(Int16Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(Int16Test.testMin(), Short.MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(Int16Test.testMax(), Short.MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(Int16Test.testParam(Short.MAX_VALUE));
        Assert.assertFalse(Int16Test.testParam(Short.MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(Int16Test.testReturnType(), Short.MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(Int16Test.testOptionalParam(Short.MAX_VALUE));
        Assert.assertFalse(Int16Test.testOptionalParam(Short.MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Short result = Int16Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.shortValue(), Short.MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = Int16Test.testProtocolParam(param -> param == Short.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        short result = Int16Test.testProtocolReturnType(() -> (short) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = Int16Test.testProtocolOptionalParam(param -> param != null && param == Short.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Short result = Int16Test.testProtocolOptionalReturnType(() -> (short) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.shortValue(), 42);
    }

    @Test
    public void testEncode() {
        Int16TestStruct result = Int16Test.testEncode();
        Assert.assertEquals(result, new Int16TestStruct());
    }

    @Test
    public void testDecode() {
        Int16TestStruct goodParam = new Int16TestStruct();
        Int16TestStruct badParam = new Int16TestStruct((short) 42, (short) 42, (short) 42, (short) 42, (short) 42);
        Assert.assertTrue(Int16Test.testDecode(goodParam));
        Assert.assertFalse(Int16Test.testDecode(badParam));
    }

}
