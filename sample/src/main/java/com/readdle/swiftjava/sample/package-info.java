/**
 * This package contains SwiftModule annotations.
 */
@SwiftModule(moduleName = "SampleProject",
        importPackages = {"SampleAppCore"},
        customTypeMappings = { @TypeMapping(swiftType = "SampleValue", javaClass = CustomSampleValue.class) })
package com.readdle.swiftjava.sample;
import com.readdle.codegen.anotation.SwiftModule;
import com.readdle.codegen.anotation.TypeMapping;