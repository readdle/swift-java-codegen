package com.readdle.swiftjava.sample.asbtracthierarhy

import com.readdle.codegen.anotation.SwiftValue

@SwiftValue(hasSubclasses = true)
open class AbstractType// Swift SwiftEnvironment constructor
protected constructor() {

    var str: String? = null

    external fun basicMethod(): String

}
