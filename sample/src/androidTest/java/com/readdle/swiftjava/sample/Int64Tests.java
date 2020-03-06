package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Int64Tests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(Int64Test.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(Int64Test.testMin(), Long.MIN_VALUE);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(Int64Test.testMax(), Long.MAX_VALUE);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(Int64Test.testParam(Long.MAX_VALUE));
        Assert.assertFalse(Int64Test.testParam(Long.MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(Int64Test.testReturnType(), Long.MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(Int64Test.testOptionalParam(Long.MAX_VALUE));
        Assert.assertFalse(Int64Test.testOptionalParam(Long.MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Long result = Int64Test.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.shortValue(), Long.MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = Int64Test.testProtocolParam(param -> param == Long.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        long result = Int64Test.testProtocolReturnType(() -> (long) 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = Int64Test.testProtocolOptionalParam(param -> param != null && param == Long.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Long result = Int64Test.testProtocolOptionalReturnType(() -> (long) 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.shortValue(), 42);
    }

    @Test
    public void testEncode() {
        Int64TestStruct result = Int64Test.testEncode();
        Assert.assertEquals(result, new Int64TestStruct());
    }

    @Test
    public void testDecode() {
        Int64TestStruct goodParam = new Int64TestStruct();
        Int64TestStruct badParam = new Int64TestStruct(42, 42, 42, (long) 42, (long) 42);
        Assert.assertTrue(Int64Test.testDecode(goodParam));
        Assert.assertFalse(Int64Test.testDecode(badParam));
    }

}
