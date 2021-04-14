package com.readdle.swiftjava.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.readdle.codegen.anotation.JavaSwift
import com.readdle.swiftjava.sample.SwiftEnvironment.Companion.initEnvironment
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Issue31Tests {

    @Before
    fun setUp() {
        System.loadLibrary("SampleAppCore")
        JavaSwift.init()
        initEnvironment()
    }

    @Test
    fun testValueTypeNanBug() {
        val progress = Issue31TestProgress(elapsed = 8, total = 8)
        Assert.assertNotEquals(Double.NaN, progress.percentage)
        Assert.assertNotEquals(Double.NaN, progress.calculatePercentage())
    }

    @Test
    fun testReferenceTypeNanBug() {
        val progress = Issue31ReferenceTestProgress.init(elapsed = 8, total = 8)
        Assert.assertNotEquals(Double.NaN, progress.percentage)
        Assert.assertNotEquals(Double.NaN, progress.calculatePercentage())
    }
}