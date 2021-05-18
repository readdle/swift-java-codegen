package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DoubleTests {

    private static final double DELTA = 0.000001;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(DoubleTest.testZero(), 0f, DELTA);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(DoubleTest.testNan(), Double.NaN, DELTA);
    }

    @Test
    public void testMax() {
        Assert.assertEquals(DoubleTest.testInfinite(), Double.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testParam() {
        Assert.assertTrue(DoubleTest.testParam(Double.POSITIVE_INFINITY));
        Assert.assertFalse(DoubleTest.testParam(Double.NaN));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(DoubleTest.testReturnType(), Double.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(DoubleTest.testOptionalParam(Double.POSITIVE_INFINITY));
        Assert.assertFalse(DoubleTest.testOptionalParam(Double.NaN));
    }

    public void testOptionalReturnType() {
        Double result = DoubleTest.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.floatValue(), Double.POSITIVE_INFINITY, DELTA);
    }

    @Test
    public void testProtocolParam() {
        boolean result = DoubleTest.testProtocolParam(param -> param == Double.POSITIVE_INFINITY);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        double result = DoubleTest.testProtocolReturnType(() -> 42.0);
        Assert.assertEquals(result, 42.0, DELTA);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = DoubleTest.testProtocolOptionalParam(param -> param != null && param == Double.POSITIVE_INFINITY);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Double result = DoubleTest.testProtocolOptionalReturnType(() -> 42.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, 42.0, DELTA);
    }

    @Test
    public void testEncode() {
        DoubleTestStruct result = DoubleTest.testEncode();
        Assert.assertEquals(result, new DoubleTestStruct());
    }

    @Test
    public void testDecode() {
        DoubleTestStruct goodParam = new DoubleTestStruct();
        DoubleTestStruct badParam = new DoubleTestStruct( 42.0, 42.0,  42.0,  42.0,42.0);
        Assert.assertTrue(DoubleTest.testDecode(goodParam));
        Assert.assertFalse(DoubleTest.testDecode(badParam));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(DoubleTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBoolBlock() {
        Assert.assertTrue(DoubleTest.testOptionalBlock(value -> value));
    }

}
