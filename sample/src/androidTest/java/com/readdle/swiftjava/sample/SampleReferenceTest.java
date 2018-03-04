package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.JavaSwift;
import com.readdle.codegen.anotation.SwiftError;
import com.readdle.swiftjava.sample.asbtracthierarhy.AbstractType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

@RunWith(AndroidJUnit4.class)
@LargeTest
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
    public void testNil() {
        Assert.assertNull(sampleReference.funcWithNil());
    }

    @Test
    public void testDelegate() {
        final boolean[] isFlag = new boolean[1];
        SampleDelegateAndroid delegateAndroid = new SampleDelegateAndroid() {

            @Override
            void onSetSampleValue(SampleValue value) {
                isFlag[0] = true;
            }
        };
        sampleReference.setDelegate(delegateAndroid);
        Assert.assertTrue(System.currentTimeMillis() - sampleReference.tick() < 1000);
        Assert.assertTrue(delegateAndroid.sampleValue.equals(sampleReference.getRandomValue()));
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
            public void onCall(@NonNull Integer pr1, @NonNull Integer pr2, @NonNull Double pr3, @NonNull Double pr4) {
                JavaSwift.dumpReferenceTables();
                isFlag[0] = pr1;
            }
        });
        JavaSwift.dumpReferenceTables();
        Assert.assertTrue(isFlag[0] == 128);
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
        Assert.assertTrue(sampleReference.exceptionCheck(exception1).getMessage().equals("JavaException:0"));
        Assert.assertTrue(sampleReference.exceptionCheck(exception2).getMessage().equals(exception2.getMessage() + ":0"));
        Assert.assertTrue(sampleReference.exceptionCheck(exception3).getMessage().equals(exception3.getMessage()));
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
            void onSetSampleValue(SampleValue value) {

            }
        };
        try {
            sampleReference.throwableFunc(sampleDelegateAndroid, true);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertTrue(e.getMessage().equals("Foundation.NSErrorThe operation could not be completed"));
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
            Assert.assertTrue(e.getMessage().equals("Foundation.NSErrorThe operation could not be completed"));
        }
        try {
            Assert.assertTrue(sampleReference.throwableFuncWithReturnType(sampleDelegateAndroid, false).equals("throwableFuncWithReturnType"));
        }
        catch (Exception e) {
            Assert.fail();
        }
    }

}
