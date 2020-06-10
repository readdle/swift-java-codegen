package com.readdle.swiftjava.sample.asbtracthierarhy

import com.readdle.codegen.anotation.SwiftValue

@SwiftValue(hasSubclasses = true)
open class AbstractType protected constructor() {

    external fun basicMethod(): String

}
