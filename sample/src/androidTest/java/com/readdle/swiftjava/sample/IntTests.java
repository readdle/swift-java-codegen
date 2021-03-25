package com.readdle.swiftjava.sample;

import androidx.test.ext.junit.runners.AndroidJUnit4;

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
            if (SwiftEnvironment.is64BitArch()) {
                // Only on 64 bit arch there is not enough bytes to represent system Int.min
                Assert.fail();
            }
        } catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"" + Long.MIN_VALUE + "\": Not enough bits to represent Int []");
        }
    }

    @Test
    public void testMax() {
        try {
            Assert.assertEquals(IntTest.testMax(), Integer.MAX_VALUE);
            if (SwiftEnvironment.is64BitArch()) {
                // Only on 64 bit arch there is not enough bytes to represent system Int.maxå
                Assert.fail();
            }
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"" + Long.MAX_VALUE + "\": Not enough bits to represent Int []");
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

    @Test
    public void testOptionalReturnType() {
        try {
            IntTest.testOptionalReturnType();
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof SwiftRuntimeError);
            Assert.assertEquals(e.getMessage(), "Invalid value \"" + Long.MAX_VALUE + "\": Not enough bits to represent Int []");
        }
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

    @Test
    public void testEnumEncode() {
        Assert.assertEquals(IntEnum.ONE, IntTest.testEnumEncode(IntEnum.ONE.getRawValue()));
        Assert.assertEquals(IntEnum.TWO, IntTest.testEnumEncode(IntEnum.TWO.getRawValue()));
        Assert.assertEquals(IntEnum.THREE, IntTest.testEnumEncode(IntEnum.THREE.getRawValue()));
    }

    @Test
    public void testEnumDecode() {
        Assert.assertEquals(IntEnum.ONE.getRawValue(), IntTest.testEnumDecode(IntEnum.ONE));
        Assert.assertEquals(IntEnum.TWO.getRawValue(), IntTest.testEnumDecode(IntEnum.TWO));
        Assert.assertEquals(IntEnum.THREE.getRawValue(), IntTest.testEnumDecode(IntEnum.THREE));
    }

    @Test
    public void testOptionSetEncode() {
        Assert.assertEquals(IntOptionsSet.getOne(), IntTest.testOptionSetEncode(IntOptionsSet.getOne().getRawValue()));
        Assert.assertEquals(IntOptionsSet.getTwo(), IntTest.testOptionSetEncode(IntOptionsSet.getTwo().getRawValue()));
        Assert.assertEquals(IntOptionsSet.getThree(), IntTest.testOptionSetEncode(IntOptionsSet.getThree().getRawValue()));
    }

    @Test
    public void testOptionSetDecode() {
        Assert.assertEquals(IntOptionsSet.getOne().getRawValue(), IntTest.testOptionSetDecode(IntOptionsSet.getOne()));
        Assert.assertEquals(IntOptionsSet.getTwo().getRawValue(), IntTest.testOptionSetDecode(IntOptionsSet.getTwo()));
        Assert.assertEquals(IntOptionsSet.getThree().getRawValue(), IntTest.testOptionSetDecode(IntOptionsSet.getThree()));
    }

    @Test
    public void testBlock() {
        Assert.assertTrue(IntTest.testBlock(value -> value));
    }

    @Test
    public void testOptionalBlock() {
        Assert.assertTrue(IntTest.testOptionalBlock(value -> value));
    }

}
