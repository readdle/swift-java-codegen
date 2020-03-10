package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftRuntimeError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IntTests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(IntTest.testZero(), 0);
    }

    @Test
    public void testMin() {
        try {
            Assert.assertEquals(IntTest.testMin(), Integer.MIN_VALUE);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "JavaCodingError (Not enough bits to represent Int " + Long.MIN_VALUE + ")");
        }
    }

    @Test
    public void testMax() {
        try {
            Assert.assertEquals(IntTest.testMax(), Integer.MAX_VALUE);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "JavaCodingError (Not enough bits to represent Int " + Long.MAX_VALUE + ")");
        }
    }

    @Test
    public void testParam() {
        Assert.assertTrue(IntTest.testParam(Integer.MAX_VALUE));
        Assert.assertFalse(IntTest.testParam(Integer.MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(IntTest.testReturnType(), Integer.MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(IntTest.testOptionalParam(Integer.MAX_VALUE));
        Assert.assertFalse(IntTest.testOptionalParam(Integer.MIN_VALUE));
    }

    public void testOptionalReturnType() {
        Integer result = IntTest.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.intValue(), Integer.MAX_VALUE);
    }

    @Test
    public void testProtocolParam() {
        boolean result = IntTest.testProtocolParam(param -> param == Integer.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        int result = IntTest.testProtocolReturnType(() -> 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = IntTest.testProtocolOptionalParam(param -> param != null && param == Integer.MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Integer result = IntTest.testProtocolOptionalReturnType(() -> 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.intValue(), 42);
    }

    @Test
    public void testEncode() {
        IntTestStruct result = IntTest.testEncode();
        Assert.assertEquals(result, new IntTestStruct());
    }

    @Test
    public void testDecode() {
        IntTestStruct goodParam = new IntTestStruct();
        IntTestStruct badParam = new IntTestStruct(42, 42, 42, 42, 42);
        Assert.assertTrue(IntTest.testDecode(goodParam));
        Assert.assertFalse(IntTest.testDecode(badParam));
    }

}
