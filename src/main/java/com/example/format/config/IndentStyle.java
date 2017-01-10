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
package com.example.format.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class IndentStyle {

    @NotNull
    @Contract("->!null")
    public IndentStyle space() {
        return indentStyle(Type.SPACE, 4);
    }

    @NotNull
    @Contract("_->!null")
    public IndentStyle space(int time) {
        return indentStyle(Type.SPACE, time);
    }

    @NotNull
    @Contract("->!null")
    public IndentStyle tab() {
        return indentStyle(Type.TAB, 1);
    }

    @NotNull
    @Contract("null,_->fail;_,_->!null")
    public static IndentStyle indentStyle(@NotNull Type type, int time) {
        Objects.requireNonNull(type);
        return new IndentStyle(type, time);
    }

    private final int time;
    private final Type type;

    private IndentStyle(int time) {
        this(Type.SPACE, time);
    }

    private IndentStyle(Type type, int time) {
        this.type = type;
        this.time = time;
    }

    @NotNull
    public String getIndentString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < time; i++) {
            sb.append(type.indentChar);
        }
        return sb.toString();
    }

    public enum Type {
        SPACE(' '),
        TAB('\t');

        private final char indentChar;

        Type(char indentChar) {
            this.indentChar = indentChar;
        }
    }
}
