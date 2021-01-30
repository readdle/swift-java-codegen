package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftRuntimeError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StringTests {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(StringTest.testZero(), "");
    }

    @Test
    public void testParam() {
        Assert.assertTrue(StringTest.testParam(""));
        Assert.assertFalse(StringTest.testParam("42"));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(StringTest.testReturnType(), "");
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(StringTest.testOptionalParam(""));
        Assert.assertFalse(StringTest.testOptionalParam(null));
    }

    @Test
    public void testOptionalReturnType() {
        try {
            StringTest.testOptionalReturnType();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"" + Long.MAX_VALUE + "\": Not enough bits to represent Int []");
        }
    }

    @Test
    public void testProtocolParam() {
        boolean result = StringTest.testProtocolParam(param -> param.equals(""));
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        String result = StringTest.testProtocolReturnType(() -> "42");
        Assert.assertEquals(result, "42");
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = StringTest.testProtocolOptionalParam(param -> param != null && param.equals(""));
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        String result = StringTest.testProtocolOptionalReturnType(() -> "42");
        Assert.assertNotNull(result);
        Assert.assertEquals(result, "42");
    }

    @Test
    public void testEncode() {
        StringTestStruct result = StringTest.testEncode();
        Assert.assertEquals(result, new StringTestStruct());
    }

    @Test
    public void testDecode() {
        StringTestStruct goodParam = new StringTestStruct();
        StringTestStruct badParam = new StringTestStruct("42", "42", "42");
        Assert.assertTrue(StringTest.testDecode(goodParam));
        Assert.assertFalse(StringTest.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(StringEnum.ONE, StringTest.testEnumEncode(StringEnum.ONE.getRawValue()));
        Assert.assertEquals(StringEnum.TWO, StringTest.testEnumEncode(StringEnum.TWO.getRawValue()));
        Assert.assertEquals(StringEnum.THREE, StringTest.testEnumEncode(StringEnum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(StringEnum.ONE.getRawValue(), StringTest.testEnumDecode(StringEnum.ONE));
        Assert.assertEquals(StringEnum.TWO.getRawValue(), StringTest.testEnumDecode(StringEnum.TWO));
        Assert.assertEquals(StringEnum.THREE.getRawValue(), StringTest.testEnumDecode(StringEnum.THREE));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(StringTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(StringTest.testOptionalBlock(value -> value));
    }

}
