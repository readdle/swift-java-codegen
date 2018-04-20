package com.readdle.codegen;

import javax.lang.model.element.Element;

public class SwiftMappingException extends RuntimeException {
    private final Element element;

    public SwiftMappingException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public SwiftMappingException(String message, Element element, Throwable parent) {
        super(message, parent);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
