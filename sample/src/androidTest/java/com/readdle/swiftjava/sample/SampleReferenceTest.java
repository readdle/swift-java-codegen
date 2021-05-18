package com.readdle.swiftjava.sample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftRuntimeError;
import com.readdle.swiftjava.sample.asbtracthierarhy.AbstractType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class SampleReferenceTest {

    private SampleReference sampleReference;

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
        SwiftEnvironment.initEnvironment();
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
        SampleValue sampleValue = SampleValue.getRandomValue();
        sampleValue.string = UUID.randomUUID().toString();
        sampleReference.saveValue(sampleValue);
    }

    @Test
    public void testThrows() {
        try {
            sampleReference.funcThrows();
            Assert.fail();
        } catch (SwiftError swiftError) {
            Assert.assertEquals("Error: 1", swiftError.getMessage());
        }
    }

    @Test
    public void testNil() {
        Assert.assertNull(sampleReference.funcWithNil());
    }

    @Test
    public void testDelegate() {
        final boolean[] isFlag = new boolean[1];
        SampleDelegateAndroid delegateAndroid = new SampleDelegateAndroid() {

            @Override
            public void onSetSampleValue(@org.jetbrains.annotations.Nullable SampleValue value) {
                isFlag[0] = true;
            }
        };
        sampleReference.setDelegate(delegateAndroid);
        Assert.assertTrue(System.currentTimeMillis() - sampleReference.tick() < 1000);
        Assert.assertTrue(!delegateAndroid.getSampleValue().string.isEmpty());
        Assert.assertTrue(isFlag[0]);
        Assert.assertNull(sampleReference.funcWithNil());
        delegateAndroid.release();
    }

    @Test
    public void testLocalTableOverflow() {
        final int[] isFlag = new int[1];
        JavaSwift.dumpReferenceTables();
        sampleReference.tickWithBlock(new SampleReference.SampleInterfaceDelegateAndroid() {

            @Override
            public void onCall(int pr1, int pr2, double pr3, double pr4) {
                isFlag[0] = pr1;
            }
        });
        JavaSwift.dumpReferenceTables();
        Assert.assertTrue(isFlag[0] == 128);
    }

    @Test
    public void testLocalTableOverflow2() {
        List<SampleValue> sampleValueList = new ArrayList<>();
        JavaSwift.dumpReferenceTables();
        for (int i = 0; i < 1024; i++) {
            sampleValueList.add(sampleReference.getRandomValue());
        }
        Assert.assertTrue(sampleValueList.size() == 1024);
        for (int i = 0; i < 1024; i++) {
            sampleReference.saveValue(sampleValueList.get(i));
        }
        JavaSwift.dumpReferenceTables();
        Assert.assertTrue(sampleValueList.size() == 1024);
    }

    @Test
    public void testFloatingPointer() {
        Assert.assertTrue(sampleReference.floatCheck(1.0f) == 3.0f);
        Assert.assertTrue(sampleReference.doubleCheck(1.0) == 3.0);
    }

    @Test
    public void testException() {
        Exception exception1 = new Exception("");
        Exception exception2 = new Exception("QWERTY");
        Exception exception3 = new Exception("QWERTY:1");
        Assert.assertEquals("java.lang.Exception:0", sampleReference.exceptionCheck(exception1).getMessage());
        Assert.assertEquals("java.lang.Exception:0", sampleReference.exceptionCheck(exception2).getMessage());
        Assert.assertEquals(sampleReference.exceptionCheck(exception3).getMessage(), exception3.getMessage());
    }

    @Test
    public void testEnclose() {
        Assert.assertNotNull(sampleReference.enclose(SampleReference.SampleReferenceEnclose.init()));
    }

    @Test
    public void testGetterSetter() {
        String string = sampleReference.getString();
        sampleReference.setString(string);
        Assert.assertTrue(string.equals(sampleReference.getString()));

        string = SampleReference.getStaticString();
        SampleReference.setStaticString(string);
        Assert.assertTrue(string.equals(SampleReference.getStaticString()));
    }

    @Test
    public void testBlock() {
        String result = sampleReference.funcWithBlock(new SampleReference.CompletionBlock() {
            @Nullable
            @Override
            public String call(@Nullable Exception error, @Nullable String string) {
                if (error == null) {
                    return string;
                }
                else {
                    return null;
                }
            }
        });
        Assert.assertNotNull(result);
        Assert.assertTrue(result.equals("123"));
    }

    @Test
    public void testBlockMemoryLeak() {
        SampleReference.CompletionBlock block = new SampleReference.CompletionBlock() {
            @Nullable
            @Override
            public String call(@Nullable Exception error, @Nullable String string) {
                if (error == null) {
                    return string;
                }
                else {
                    return null;
                }
            }
        };

        MemoryLeakVerifier memoryLeakVerifier = new MemoryLeakVerifier(block);

        String result = sampleReference.funcWithBlock(block);
        Assert.assertNotNull(result);

        block = null;
        memoryLeakVerifier.assertGarbageCollected("SampleReference.CompletionBlock");
    }

    @Test
    public void testAbstractTypes() {
        AbstractType child = sampleReference.getAbstractType();
        Assert.assertTrue(child.basicMethod().equals("FirstChild"));
        child = sampleReference.getFirstChild();
        Assert.assertTrue(child.basicMethod().equals("FirstChild"));
        child = sampleReference.getSecondChild();
        Assert.assertTrue(child.basicMethod().equals("SecondChild"));
        child = sampleReference.getThirdChild();
        Assert.assertTrue(child.basicMethod().equals("ThirdChild"));
        child = sampleReference.getFourthChild();
        Assert.assertTrue(child.basicMethod().equals("SecondChild"));
    }

    @Test
    public void testThrowableFunc() {
        SampleDelegateAndroid sampleDelegateAndroid = new SampleDelegateAndroid() {
            @Override
            public void onSetSampleValue(@org.jetbrains.annotations.Nullable SampleValue value) {

            }
        };
        try {
            sampleReference.throwableFunc(sampleDelegateAndroid, true);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertEquals("java.lang.IllegalArgumentException: 0", e.getMessage());
        }
        try {
            sampleReference.throwableFunc(sampleDelegateAndroid, false);
        }
        catch (Exception e) {
            Assert.fail();
        }
        try {
            sampleReference.throwableFuncWithReturnType(sampleDelegateAndroid, true);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertEquals("java.lang.IllegalArgumentException: 0", e.getMessage());
        }
        try {
            Assert.assertEquals("throwableFuncWithReturnType", sampleReference.throwableFuncWithReturnType(sampleDelegateAndroid, false));
        }
        catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testNullPointerAfterRelease() {
        SampleReference sampleReference = SampleReference.init();
        sampleReference.release();
        try {
            sampleReference.funcWithNil();
            Assert.fail();
        }
        catch (SwiftRuntimeError error) {
            Assert.assertEquals("NSError (The operation could not be completed. (java.lang.NullPointerException error 1.))", error.getMessage());
        }
    }
    
    // Run on Android 7.0 to fail
    @Test
    public void testLocalTableOverflow3() {
        sampleReference.oneMoreReferenceTableOverflow(new SampleDelegateAndroid() {
            @Override
            public void onSetSampleValue(SampleValue value) {
                // empty
            }
        });
    }

}
