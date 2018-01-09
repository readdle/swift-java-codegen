package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SampleReferenceTest {

    private SampleReference sampleReference;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCoreBridge");
        SampleValue.initCoder();

        this.sampleReference = SampleReference.init();
    }

    @After
    public void clear() {
        this.sampleReference.release();
    }

    @Test
    public void testGetRandomValue() {
        Assert.assertNotNull(sampleReference.getRandomValue());
    }

    @Test
    public void testSaveValue() {
        SampleValue sampleValue = new SampleValue();
        sampleValue.str1 = "str1";
        sampleValue.str2 = "str2";
        sampleValue.str3 = "str3";
        sampleReference.saveValue(sampleValue);
    }

    @Test
    public void testThrows() {
        try {
            sampleReference.funcThrows();
            Assert.fail();
        } catch (SwiftError swiftError) {
            Assert.assertTrue(swiftError.getMessage() != null);
        }
    }

    @Test
    public void testDelegate() {
        SampleDelegateAndroid delegateAndroid = new SampleDelegateAndroid();
        sampleReference.setDelegate(delegateAndroid);
        Assert.assertTrue(System.currentTimeMillis() - sampleReference.tick() < 1000);
        Assert.assertTrue(delegateAndroid.sampleValue.equals(sampleReference.getRandomValue()));
        delegateAndroid.release();
    }

}
