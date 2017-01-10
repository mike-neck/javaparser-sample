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

import org.jetbrains.annotations.NotNull;

public class FormatConfig {

    private final IndentStyle style;
    private final NewLine newLine;

    public FormatConfig(IndentStyle style) {
        this(style, NewLine.SYSTEM);
    }

    public FormatConfig(IndentStyle style, NewLine newLine) {
        this.style = style;
        this.newLine = newLine;
    }

    @NotNull
    public String getIndentString() {
        return style.getIndentString();
    }

    @NotNull
    public String getNewLine() {
        return newLine.getSeparator();
    }
}
