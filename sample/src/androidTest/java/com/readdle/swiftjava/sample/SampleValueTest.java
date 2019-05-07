package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class SampleValueTest {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
    }

    @Test
    public void testGetRandomValue() {
        SampleValue sampleValue = SampleValue.getRandomValue().copy();
        Assert.assertNotNull(sampleValue);
        Assert.assertTrue(!sampleValue.string.isEmpty());
        Assert.assertTrue(sampleValue.integer == 32);
        Assert.assertTrue(sampleValue.int8 == 8);
        Assert.assertTrue(sampleValue.int16 == 16);
        Assert.assertTrue(sampleValue.int32 == 32);
        Assert.assertTrue(sampleValue.int64 == 64);
        Assert.assertTrue(sampleValue.uint == 32);
        Assert.assertTrue(sampleValue.uint8 == 8);
        Assert.assertTrue(sampleValue.uint16 == 16);
        Assert.assertTrue(sampleValue.uint32 == 32);
        Assert.assertTrue(sampleValue.uint64.equals(BigInteger.valueOf(64)));

        Assert.assertTrue(sampleValue.objectArray.isEmpty());
        Assert.assertTrue(sampleValue.stringArray.size() == 3);
        Assert.assertTrue(sampleValue.numberArray.size() == 3);
        Assert.assertTrue(sampleValue.arrayInArray.get(0).size() == 3);
        Assert.assertTrue(sampleValue.dictInArray.get(0).size() == 3);

        Assert.assertTrue(sampleValue.dictSampleClass.isEmpty());
        Assert.assertTrue(sampleValue.dictStrings.size() == 1);
        Assert.assertTrue(sampleValue.dictNumbers.size() == 1);
        Assert.assertTrue(sampleValue.dict64Numbers.size() == 1);
        Assert.assertTrue(sampleValue.dictInDict.size() == 1);
        Assert.assertTrue(sampleValue.arrayInDict.size() == 1);

        Assert.assertTrue(sampleValue.set.size() == 3);
        Assert.assertTrue(sampleValue.setValues.isEmpty());

    }

    @Test
    public void testSaveValue() {
        SampleValue sampleValue = SampleValue.getRandomValue();
        sampleValue.string = UUID.randomUUID().toString();
        sampleValue.saveValue();
    }

    @Test
    public void testIsSame() {
        SampleValue otherValue = SampleValue.getRandomValue();
        SampleValue sampleValue = SampleValue.getRandomValue();
        sampleValue.string = otherValue.string;
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
