package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftValue;

import android.support.annotation.NonNull;

import java.util.Objects;

@SwiftValue
public class SampleValue {

    @NonNull
    public String str1;
    @NonNull
    public String str2;
    @NonNull
    public String str3;

    // Swift JNI constructor
    public SampleValue() {

    }

    @NonNull
    public static native SampleValue getRandomValue();

    public native void saveValue();

    @NonNull @SwiftFunc("isSame(other:)")
    public native Boolean isSame(@NonNull SampleValue other);

    public static native void funcThrows() throws SwiftError;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SampleValue)) return false;
        SampleValue that = (SampleValue) o;
        return Objects.equals(str1, that.str1) &&
                Objects.equals(str2, that.str2) &&
                Objects.equals(str3, that.str3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str1, str2, str3);
    }
}
