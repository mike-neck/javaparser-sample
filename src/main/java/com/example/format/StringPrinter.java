/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.format;

import com.example.format.annotation.Immutable;
import com.example.format.annotation.Mutable;
import com.example.format.config.FormatConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public final class StringPrinter {

    @Immutable
    private final Appendable writer;

    @Immutable
    private final FormatConfig config;

    @Mutable
    private int indentLevel = 0;

    @Mutable
    private boolean indented = false;

    public StringPrinter(
            @NotNull Appendable writer
            , @NotNull FormatConfig config
    ) {
        this.writer = Objects.requireNonNull(writer);
        this.config = Objects.requireNonNull(config);
    }

    public StringPrinter(@NotNull FormatConfig config) {
        this.writer = new StringWriter();
        this.config = Objects.requireNonNull(config);
        this.indentLevel = 0;
        this.indented = false;
    }

    @Contract("null->fail")
    public void append(@NotNull String str) {
        Objects.requireNonNull(str);
        try {
            writer.append(str);
        } catch (IOException e) {
            throw new FormatterException(e);
        }
    }

    public void makeIndent() {
        for (int i = 0; i < indentLevel; i++) {
            append(config.getIndentString());
        }
    }

    public void indent() {
        indentLevel += 1;
    }

    public void unIndent() {
        if (indentLevel != 0) {
            indentLevel -= 1;
        }
    }

    public void updateIndented() {
        indented = !indented;
    }

    public void newLine() {
        append(config.getNewLine());
        indented = false;
    }

    @Contract("null->fail")
    public void print(@NotNull String str) {
        if (!indented) {
            makeIndent();
            updateIndented();
        }
        append(str);
    }

    @Contract("null->fail")
    public void printLn(@NotNull String str) {
        print(str);
        newLine();
    }

    public String getPrinted() {
        return writer.toString();
    }
}
