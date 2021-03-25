package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftRuntimeError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UIntTests {

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = -1;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
    }

    @Test
    public void testZero() {
        Assert.assertEquals(UIntTest.testZero(), 0);
    }

    @Test
    public void testMin() {
        Assert.assertEquals(UIntTest.testMin(), MIN_VALUE);
    }

    @Test
    public void testMax() {
        try {
            Assert.assertEquals(UIntTest.testMax(), MAX_VALUE);
            if (SwiftEnvironment.is64BitArch()) {
                // Only on 64 bit arch there is not enough bytes to represent system UInt.max
                Assert.fail();
            }
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"18446744073709551615\": Not enough bits to represent UInt []");
        }
    }

    @Test
    public void testParam() {
        Assert.assertTrue(UIntTest.testParam(MAX_VALUE));
        Assert.assertFalse(UIntTest.testParam(MIN_VALUE));
    }

    @Test
    public void testReturnType() {
        Assert.assertEquals(UIntTest.testReturnType(), MAX_VALUE);
    }

    @Test
    public void testOptionalParam() {
        Assert.assertTrue(UIntTest.testOptionalParam(MAX_VALUE));
        Assert.assertFalse(UIntTest.testOptionalParam(MIN_VALUE));
    }

    @Test
    public void testOptionalReturnType() {
        try {
            UIntTest.testOptionalReturnType();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"18446744073709551615\": Not enough bits to represent UInt []");
        }
    }

    @Test
    public void testProtocolParam() {
        boolean result = UIntTest.testProtocolParam(param -> param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolReturnType() {
        int result = UIntTest.testProtocolReturnType(() -> 42);
        Assert.assertEquals(result, 42);
    }

    @Test
    public void testProtocolOptionalParam() {
        boolean result = UIntTest.testProtocolOptionalParam(param -> param != null && param == MAX_VALUE);
        Assert.assertTrue(result);
    }

    @Test
    public void testProtocolOptionalReturnType() {
        Integer result = UIntTest.testProtocolOptionalReturnType(() -> 42);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.intValue(), 42);
    }

    @Test
    public void testEncode() {
        UIntTestStruct result = UIntTest.testEncode();
        Assert.assertEquals(result, new UIntTestStruct());
    }

    @Test
    public void testDecode() {
        UIntTestStruct goodParam = new UIntTestStruct();
        UIntTestStruct badParam = new UIntTestStruct(42, 42, 42, 42, 42);
        Assert.assertTrue(UIntTest.testDecode(goodParam));
        Assert.assertFalse(UIntTest.testDecode(badParam));
    }

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(UIntEnum.ONE,UIntTest.testEnumEncode(UIntEnum.ONE.getRawValue()));
        Assert.assertEquals(UIntEnum.TWO,UIntTest.testEnumEncode(UIntEnum.TWO.getRawValue()));
        Assert.assertEquals(UIntEnum.THREE,UIntTest.testEnumEncode(UIntEnum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(UIntEnum.ONE.getRawValue(),UIntTest.testEnumDecode(UIntEnum.ONE));
        Assert.assertEquals(UIntEnum.TWO.getRawValue(),UIntTest.testEnumDecode(UIntEnum.TWO));
        Assert.assertEquals(UIntEnum.THREE.getRawValue(),UIntTest.testEnumDecode(UIntEnum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(UIntOptionsSet.getOne(),UIntTest.testOptionSetEncode(UIntOptionsSet.getOne().getRawValue()));
        Assert.assertEquals(UIntOptionsSet.getTwo(),UIntTest.testOptionSetEncode(UIntOptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(UIntOptionsSet.getThree(),UIntTest.testOptionSetEncode(UIntOptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(UIntOptionsSet.getOne().getRawValue(),UIntTest.testOptionSetDecode(UIntOptionsSet.getOne()));
        Assert.assertEquals(UIntOptionsSet.getTwo().getRawValue(),UIntTest.testOptionSetDecode(UIntOptionsSet.getTwo()));
        Assert.assertEquals(UIntOptionsSet.getThree().getRawValue(),UIntTest.testOptionSetDecode(UIntOptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(UIntTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(UIntTest.testOptionalBlock(value -> value));
    }

}
