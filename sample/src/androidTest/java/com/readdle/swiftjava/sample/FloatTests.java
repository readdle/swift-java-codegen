package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FloatTests {

    private static final float DELTA = 0.000001f;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(FloatTest.testZero(), 0f, DELTA);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(FloatTest.testNan(), Float.NaN, DELTA);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(FloatTest.testInfinite(), Float.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(FloatTest.testParam(Float.POSITIVE_INFINITY));
        Assert.assertFalse(FloatTest.testParam(Float.NaN));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(FloatTest.testReturnType(), Float.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(FloatTest.testOptionalParam(Float.POSITIVE_INFINITY));
        Assert.assertFalse(FloatTest.testOptionalParam(Float.NaN));
    }

    public void testOptionalReturnType() {
        Float result = FloatTest.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.floatValue(), Float.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testProtocolParam() {
        boolean result = FloatTest.testProtocolParam(param -> param == Float.POSITIVE_INFINITY);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        float result = FloatTest.testProtocolReturnType(() -> 42f);
        Assert.assertEquals(result, 42f, DELTA);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = FloatTest.testProtocolOptionalParam(param -> param != null && param == Float.POSITIVE_INFINITY);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Float result = FloatTest.testProtocolOptionalReturnType(() -> 42f);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.floatValue(), 42f, DELTA);
    }

    @Test
    public void testEncode() {
        FloatTestStruct result = FloatTest.testEncode();
        Assert.assertEquals(result, new FloatTestStruct());
    }

    @Test
    public void testDecode() {
        FloatTestStruct goodParam = new FloatTestStruct();
        FloatTestStruct badParam = new FloatTestStruct( 42f, 42f,  42f,  42f,42f);
        Assert.assertTrue(FloatTest.testDecode(goodParam));
        Assert.assertFalse(FloatTest.testDecode(badParam));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(FloatTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(FloatTest.testOptionalBlock(value -> value));
    }

}
