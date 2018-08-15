package com.readdle.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SwiftWriter {
    private static class AlignedFileWriter extends FileWriter {
        private static final char MARGIN = '\t';
        private int currentLevel = 0;
        private boolean printMargin = false;

        private AlignedFileWriter(File file) throws IOException {
            super(file);
        }

        private void appendCharacter(Character character) throws IOException {
            if (printMargin) {
                for (int i = 0; i < currentLevel; i++) {
                    super.append(MARGIN);
                }

                printMargin = false;
            }

            super.append(character);
        }

        public Writer append(CharSequence charSequence) throws IOException {
            for (int i = 0; i < charSequence.length(); i++) {
                char c = charSequence.charAt(i);


                if (c == '}') {
                    printMargin = true;
                    currentLevel--;
                }

                appendCharacter(c);

                if (c == '\n') {
                    printMargin = true;
                }

                if (c == '{') {
                    currentLevel++;
                }
            }
            return this;
        }
    }

    private final Writer writer;

    public SwiftWriter(File file) throws IOException {
        this.writer = new AlignedFileWriter(file);
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
        this.writer.append("import AnyCodable\n");

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

    public void emitEndOfBlock() throws IOException {
        this.writer.append("}\n");
    }
}
