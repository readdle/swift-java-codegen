package com.readdle.codegen.anotation;


public class SwiftRuntimeError extends RuntimeException {

    public SwiftRuntimeError() {

    }

    public SwiftRuntimeError(String message) {
        super(message);
    }
}
