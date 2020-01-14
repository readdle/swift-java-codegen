package com.readdle.swiftjava.sample

import com.readdle.codegen.anotation.SwiftCallbackFunc
import com.readdle.codegen.anotation.SwiftDelegate
import android.util.Log

import java.nio.ByteBuffer

@SwiftDelegate(protocols = ["SampleDelegate"])
//@UseExperimental(ExperimentalUnsignedTypes::class)
abstract class SampleDelegateAndroid {

    // Swift JNI private native pointer
    private val nativePointer = 0L

    internal var sampleValue: SampleValue? = null

    // Swift JNI init method
    external fun init()

    // Swift JNI release method
    external fun release()

    init {
        init()
    }

    @SwiftCallbackFunc("setSampleValue(value:)")
    fun setSampleValue(value: SampleValue?) {
        sampleValue = value
        onSetSampleValue(value)
        if (value != null) {
        }
    }

    @SwiftCallbackFunc
    fun getSampleValue(): SampleValue {
        return sampleValue!!
    }

    @SwiftCallbackFunc
    fun funcWithNil(): SampleValue? {
        return null
    }

    @SwiftCallbackFunc
    @Throws(Exception::class)
    fun throwableFunc(flag: Boolean) {
        require(!flag) { "Exception" }
    }

    @SwiftCallbackFunc
    @Throws(Exception::class)
    fun throwableFuncWithReturnType(flag: Boolean): String {
        return if (flag) {
            throw IllegalArgumentException("Exception")
        } else {
            "throwableFuncWithReturnType"
        }
    }

    @SwiftCallbackFunc
    fun funcWithData(): ByteBuffer {
        val tenBytes = ByteArray(10)
        return ByteBuffer.wrap(tenBytes)
    }

    abstract fun onSetSampleValue(value: SampleValue?)

    companion object {

        @JvmStatic
        var timestamp: Long
            @SwiftCallbackFunc
            get() = System.currentTimeMillis()
            @SwiftCallbackFunc
            set(value) {
                Log.i("TAG", value.toString())
            }
    }

}
