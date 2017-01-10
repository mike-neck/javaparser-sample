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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class CodeFormatter {

    private static final ClassLoader loader = CodeFormatter.class.getClassLoader();

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(loader.getResourceAsStream("StopwatchTestSupport.java"))) {
            final CompilationUnit cu = JavaParser.parse(r);
            System.out.println(cu.toString());
        } catch (IOException | ParseException e) {
            throw new FormatterException(e);
        }
    }
}
