package com.readdle.swiftjava.sample;

import android.support.test.runner.AndroidJUnit4;

import com.readdle.codegen.anotation.JavaSwift;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProtocolsTest {

    @Before
    public void setUp() {
        System.loadLibrary("SampleAppCore");
        JavaSwift.init();
    }

    @Test
    public void testProtocols() {
        Service.registerProvider(new ProviderImpl());
    }
}
