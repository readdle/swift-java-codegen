package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BoolTests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertTrue(BoolTest.testYes());
    }

    @Test
    public void testMin() {
        Assert.assertFalse(BoolTest.testNo());
    }

    @Test
    public void testParam() {
        Assert.assertTrue(BoolTest.testParam(true));
        Assert.assertFalse(BoolTest.testParam(false));
    }

    @Test
    public void testReturnType() {
        Assert.assertTrue(BoolTest.testReturnType());
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(BoolTest.testOptionalParam(true));
        Assert.assertFalse(BoolTest.testOptionalParam(false));
    }

    public void testOptionalReturnType() {
        Boolean result = BoolTest.testOptionalReturnType();
        Assert.assertNotNull(result);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testProtocolParam() {
        boolean result = BoolTest.testProtocolParam(param -> param);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        boolean result = BoolTest.testProtocolReturnType(() -> true);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = BoolTest.testProtocolOptionalParam(param -> param != null && param);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Boolean result = BoolTest.testProtocolOptionalReturnType(() -> true);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testEncode() {
        BoolTestStruct result = BoolTest.testEncode();
        Assert.assertEquals(result, new BoolTestStruct());
    }

    @Test
    public void testDecode() {
        BoolTestStruct goodParam = new BoolTestStruct();
        BoolTestStruct badParam = new BoolTestStruct(true, true, true, true);
        Assert.assertTrue(BoolTest.testDecode(goodParam));
        Assert.assertFalse(BoolTest.testDecode(badParam));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(BoolTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(BoolTest.testOptionalBlock(value -> value));
    }

}
