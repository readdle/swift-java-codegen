package com.readdle.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SwiftWriter {

    private final Writer writer;

    public SwiftWriter(File file) throws IOException {
        this.writer = new FileWriter(file);
    }

    public void close() throws IOException {
        this.writer.flush();
        this.writer.close();
    }

    public void emitImports(String[] importPackages) throws IOException {
        this.writer.append("/* This file was generated with Readdle SwiftJava Codegen */\n");
        this.writer.append("/* Don't change it manually! */\n");

        this.writer.append("import Foundation\n");
        this.writer.append("import Java\n");
        this.writer.append("import java_swift\n");
        this.writer.append("import JavaCoder\n");

        for (String importPackage : importPackages) {
            this.writer.append(String.format("import %s\n", importPackage));
        }
    }

    public void emitEmptyLine() throws IOException {
        this.writer.append("\n");
    }

    public void emit(String statement) throws IOException {
        this.writer.append(statement);
    }

    public void emitStatement(String statement) throws IOException {
        this.writer.append(statement);
        this.writer.append("\n");
    }

    public void beginExtension(String swiftType) throws IOException {
        this.writer.append(String.format("public extension %s {", swiftType));
        this.writer.append("\n");
    }

    public void endExtension() throws IOException {
        this.writer.append("}");
        this.writer.append("\n");
    }

}
