package com.readdle.swiftjava.sample.asbtracthierarhy;

import com.readdle.codegen.anotation.SwiftValue;

import android.support.annotation.NonNull;

@SwiftValue(hasSubclasses = true)
public class AbstractType {

    // Swift SwiftEnvironment constructor
    protected AbstractType() {}

    public String str;

    @NonNull
    public native String basicMethod();

}
