package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftValue;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

@SwiftValue
public class SampleValue {

    @NonNull
    public String string;
    @NonNull
    Integer integer;
    @NonNull
    Byte int8;
    @NonNull
    Short int16;
    @NonNull
    Integer int32;
    @NonNull
    Long int64;

    @NonNull
    Long uint;
    @NonNull
    Short uint8;
    @NonNull
    Integer uint16;
    @NonNull
    Long uint32;
    @NonNull
    BigInteger uint64;

    @NonNull
    ArrayList<SampleValue> objectArray;
    @NonNull
    ArrayList<String> stringArray;
    @NonNull
    ArrayList<Integer> numberArray;
    @NonNull
    ArrayList<ArrayList<Integer>> arrayInArray;
    @NonNull
    ArrayList<HashMap<Integer, Integer>> dictInArray;

    @NonNull
    HashMap<String, SampleValue> dictSampleClass;
    @NonNull
    HashMap<String, String> dictStrings;
    @NonNull
    HashMap<Integer, Integer> dictNumbers;
    @NonNull
    HashMap<BigInteger, BigInteger> dict64Numbers;
    @NonNull
    HashMap<BigInteger, HashMap<BigInteger, BigInteger>> dictInDict;
    @NonNull
    HashMap<BigInteger, ArrayList<BigInteger>> arrayInDict;

    @NonNull
    HashSet<Integer> set;
    @NonNull
    HashSet<SampleValue> setValues;

    // Swift JNI constructor
    protected SampleValue() {

    }

    @NonNull
    public static native SampleValue getRandomValue();

    @NonNull
    public native SampleValue copy();

    public native void saveValue();

    @NonNull @SwiftFunc("isSame(other:)")
    public native Boolean isSame(@NonNull SampleValue other);

    public static native void funcThrows() throws SwiftError;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SampleValue)) return false;
        SampleValue that = (SampleValue) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return "SampleValue{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                ", int8=" + int8 +
                ", int16=" + int16 +
                ", int32=" + int32 +
                ", int64=" + int64 +
                ", uint=" + uint +
                ", uint8=" + uint8 +
                ", uint16=" + uint16 +
                ", uint32=" + uint32 +
                ", uint64=" + uint64 +
                ", objectArray=" + objectArray +
                ", stringArray=" + stringArray +
                ", numberArray=" + numberArray +
                ", arrayInArray=" + arrayInArray +
                ", dictInArray=" + dictInArray +
                ", dictSampleClass=" + dictSampleClass +
                ", dictStrings=" + dictStrings +
                ", dictNumbers=" + dictNumbers +
                ", dict64Numbers=" + dict64Numbers +
                ", dictInDict=" + dictInDict +
                ", arrayInDict=" + arrayInDict +
                ", set=" + set +
                ", setValues=" + setValues +
                '}';
    }
}
