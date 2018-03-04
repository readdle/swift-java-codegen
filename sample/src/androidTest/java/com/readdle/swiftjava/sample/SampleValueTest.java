package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SampleValueTest {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
    }

    @Test
    public void testGetRandomValue() {
        Assert.assertNotNull(SampleValue.getRandomValue());
    }

    @Test
    public void testSaveValue() {
        SampleValue sampleValue = new SampleValue();
        sampleValue.str1 = "str1";
        sampleValue.str2 = "str2";
        sampleValue.str3 = "str3";
        sampleValue.saveValue();
    }

    @Test
    public void testIsSame() {
        SampleValue otherValue = SampleValue.getRandomValue();
        SampleValue sampleValue = new SampleValue();
        sampleValue.str1 = otherValue.str1;
        sampleValue.str2 = otherValue.str2;
        sampleValue.str3 = otherValue.str3;
        Assert.assertTrue(sampleValue.isSame(otherValue));
    }

    @Test
    public void testThrows() {
        try {
            SampleValue.funcThrows();
            Assert.fail();
        } catch (SwiftError swiftError) {
            Assert.assertTrue(swiftError.getMessage() != null);
        }
    }

}
