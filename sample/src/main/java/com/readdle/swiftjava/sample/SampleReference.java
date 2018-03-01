package com.readdle.swiftjava.sample;

import com.readdle.codegen.anotation.SwiftBlock;
import com.readdle.codegen.anotation.SwiftCallbackFunc;
import com.readdle.codegen.anotation.SwiftDelegate;
import com.readdle.codegen.anotation.SwiftError;
import com.readdle.codegen.anotation.SwiftFunc;
import com.readdle.codegen.anotation.SwiftGetter;
import com.readdle.codegen.anotation.SwiftReference;
import com.readdle.codegen.anotation.SwiftSetter;
import com.readdle.swiftjava.sample.asbtracthierarhy.AbstractType;
import com.readdle.swiftjava.sample.asbtracthierarhy.FirstChild;
import com.readdle.swiftjava.sample.asbtracthierarhy.FourthChild;
import com.readdle.swiftjava.sample.asbtracthierarhy.SecondChild;
import com.readdle.swiftjava.sample.asbtracthierarhy.ThirdChild;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@SwiftReference
public class SampleReference {

    @SwiftReference
    public static class SampleReferenceEnclose {


        // Swift JNI private native pointer
        private long nativePointer = 0L;

        // Swift JNI private constructor
        // Should be private. Don't call this constructor from Java!
        private SampleReferenceEnclose() {
        }

        // Swift JNI release method
        public native void release();

        @NonNull
        public static native SampleReferenceEnclose init();
    }

    @SwiftDelegate(protocols = {"SampleBlockDelegate"})
    public interface SampleInterfaceDelegateAndroid {

        @SwiftCallbackFunc
        void onCall(@NonNull Integer pr1, @NonNull Integer pr2, @NonNull Double pr3, @NonNull Double pr4);

    }

    // Swift JNI private native pointer
    private long nativePointer = 0L;

    // Swift JNI private constructor
    // Should be private. Don't call this constructor from Java!
    private SampleReference() {
    }

    // Swift JNI release method
    public native void release();

    @NonNull
    public native SampleValue getRandomValue();

    public native void saveValue(@NonNull SampleValue value);

    public native void funcThrows() throws SwiftError;

    @Nullable
    public native SampleReference funcWithNil();

    @NonNull
    public static native SampleReference init();

    // TODO: Impossible to generate for now. Add extra check for JavaSwift protocol before casting to .javaObject()
    //@Nullable @SwiftFunc
    //public native SampleDelegateAndroid getDelegate();

    @SwiftSetter
    public native void setDelegate(@Nullable SampleDelegateAndroid delegate);

    @NonNull
    public native Long tick();

    public native void tickWithBlock(@NonNull SampleInterfaceDelegateAndroid delegate);

    @NonNull
    public native Float floatCheck(@NonNull Float var1);

    @NonNull
    public native Double doubleCheck(@NonNull Double var1);

    @NonNull
    public native Exception exceptionCheck(@NonNull Exception var1);

    @NonNull
    public native SampleReferenceEnclose enclose(@NonNull SampleReferenceEnclose var1);

    @SwiftGetter @NonNull
    public native String getString();

    @SwiftSetter
    public native void setString(@NonNull String string);

    @SwiftGetter("staticString") @NonNull
    public native static String getStaticString();

    @SwiftSetter("staticString")
    public native static void setStaticString(@NonNull String string);

    @SwiftBlock("(Error?, String?) -> String?")
    public interface CompletionBlock {
        @Nullable
        String call(@Nullable Exception error, @Nullable String string);
    }

    @SwiftFunc("funcWithBlock(completion:)") @Nullable
    public native String funcWithBlock(@SwiftBlock CompletionBlock block);

    @NonNull
    public native List<AbstractType> getAbstractTypeList();

    @NonNull
    public native AbstractType getAbstractType();

    @NonNull
    public native FirstChild getFirstChild();

    @NonNull
    public native SecondChild getSecondChild();

    @NonNull
    public native ThirdChild getThirdChild();

    @NonNull
    public native FourthChild getFourthChild();

    public native void throwableFunc(@NonNull SampleDelegateAndroid delegate, @NonNull Boolean flag) throws Exception;

    @NonNull
    public native String throwableFuncWithReturnType(@NonNull SampleDelegateAndroid delegate, @NonNull Boolean flag) throws Exception;

}
