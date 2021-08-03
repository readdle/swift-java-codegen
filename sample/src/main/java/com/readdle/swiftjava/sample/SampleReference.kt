package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.*
import com.readdle.swiftjava.sample.asbtracthierarhy.*

@SwiftReference
class SampleReference private constructor() {

    @SwiftReference
    class SampleReferenceEnclose private constructor() {

        // Swift JNI private native pointer
        private val nativePointer = 0L

        // Swift JNI release method
        external fun release()

        companion object {
            @JvmStatic
            external fun init(): SampleReferenceEnclose
        }
    }

    @SwiftDelegate(protocols = ["SampleBlockDelegate"])
    interface SampleInterfaceDelegateAndroid {
        @SwiftCallbackFunc
        fun onCall(pr1: Int, pr2: Int, pr3: Double, pr4: Double)
    }

    // Swift JNI private native pointer
    private val nativePointer = 0L

    // Swift JNI release method
    external fun release()

    val randomValue: SampleValue
        external get

    external fun saveValue(value: SampleValue)

    @Throws(SwiftError::class)
    external fun funcThrows()

    external fun funcWithNil(): SampleReference?

    // TODO: Impossible to generate for now. Add extra check for JavaSwift protocol before casting to .javaObject()
    //@Nullable @SwiftFunc
    //public native SampleDelegateAndroid getDelegate();
    @SwiftSetter
    external fun setDelegate(delegate: SampleDelegateAndroid)

    external fun tick(): Long

    external fun tickWithBlock(delegate: SampleInterfaceDelegateAndroid)

    external fun floatCheck(var1: Float): Float

    external fun doubleCheck(var1: Double): Double

    external fun exceptionCheck(var1: Exception): Exception

    external fun enclose(var1: SampleReferenceEnclose): SampleReferenceEnclose

    @get:SwiftGetter
    @set:SwiftSetter
    var string: String
        external get
        external set

    @SwiftBlock("(Error?, String?) -> String?")
    interface CompletionBlock {
        fun call(error: Exception?, string: String?): String?
    }

    @SwiftFunc("funcWithBlock(completion:)")
    external fun funcWithBlock(@SwiftBlock block: CompletionBlock?): String?

    val abstractTypeList: List<AbstractType>
        external get

    @get:SwiftFunc("getAbstractType()")
    val abstractType: AbstractType
        external get

    val firstChild: FirstChild
        external get

    val secondChild: SecondChild
        external get

    val thirdChild: ThirdChild
        external get

    val fourthChild: FourthChild
        external get

    @Throws(Exception::class)
    external fun throwableFunc(delegate: SampleDelegateAndroid, flag: Boolean)

    @Throws(Exception::class)
    external fun throwableFuncWithReturnType(delegate: SampleDelegateAndroid, flag: Boolean): String

    val randomCustomSampleValue: CustomSampleValue
        external get

    external fun saveCustomSampleValue(value: CustomSampleValue)

    external fun oneMoreReferenceTableOverflow(delegate: SampleDelegateAndroid)

    companion object {

        @JvmStatic
        external fun init(): SampleReference

        @JvmStatic @SwiftGetter("staticString")
        external fun getStaticString(): String

        @JvmStatic @SwiftSetter("staticString")
        external fun setStaticString(value: String)

    }
}