package com.readdle.swiftjava.sample.asbtracthierarhy;

import com.readdle.codegen.anotation.SwiftValue;

import android.support.annotation.NonNull;

@SwiftValue(hasSubclasses = true)
public class AbstractType {

    // Swift JNI constructor
    protected AbstractType() {}

    @NonNull
    public native String basicMethod();

}
