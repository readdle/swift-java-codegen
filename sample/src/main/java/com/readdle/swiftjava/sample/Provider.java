package com.readdle.swiftjava.sample;

import android.support.annotation.NonNull;

import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftDelegate;

@SwiftDelegate(protocols = {"Provider"})
public interface Provider {
    @SwiftCallbackFunc("fill(storage:)")
    void fillStorage(@NonNull Storage storage);
}
