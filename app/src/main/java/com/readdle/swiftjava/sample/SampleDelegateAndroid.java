package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftDelegate;

import android.support.annotation.NonNull;
import android.util.Log;

@SwiftDelegate(importPackages = {"SampleAppCore"}, protocols = {"SampleDelegate"})
public class SampleDelegateAndroid {

    // Swift JNI private native pointer
    private long nativePointer = 0L;

    // Swift JNI init method
    public native void init();

    // Swift JNI release method
    public native void release();


    public SampleDelegateAndroid() {
        init();
    }

    SampleValue sampleValue = null;

    @SwiftCallbackFunc
    public void setSampleValue(SampleValue value) {
        sampleValue = value;
        Log.i("TAG", value.toString());
    }

    @NonNull @SwiftCallbackFunc
    public SampleValue getSampleValue() {
        return sampleValue;
    }

    @SwiftCallbackFunc
    public static void setTimestamp(Long value) {
        Log.i("TAG", value.toString());
    }

    @NonNull @SwiftCallbackFunc
    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }

}
