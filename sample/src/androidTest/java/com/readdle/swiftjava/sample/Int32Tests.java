package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

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

}
