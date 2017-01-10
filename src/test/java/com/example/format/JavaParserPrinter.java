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
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.google.googlejavaformat.java.JavaFormatterOptions.Style;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;

public class JavaParserPrinter {

    @ExtendWith({ ResourceExtension.class })
    @Nested
    public class StopwatchTestSupport {

        @Test
        void javaParser(@Resource("StopwatchTestSupport.java") InputStream source) throws ParseException {
            final CompilationUnit cu = JavaParser.parse(source, "UTF-8");
            System.out.println(cu.toString());
        }


        @Test
        void googleFormatter(@Resource("StopwatchTestSupport.java") String source) throws FormatterException {
            final String formatted = new Formatter().formatSource(source);
            System.out.println(formatted);
        }
    }

    @ExtendWith(ResourceExtension.class)
    @Nested
    public class Foo {
        @Test
        void closeStream(@Resource("foo.txt") InputStream stream) throws IOException {
            stream.close();
        }
    }

    @ExtendWith(ResourceExtension.class)
    @Nested
    public class ResourceExtensionPrinter {

        @Test
        void javaParser(@Resource("ResourceExtension_.java") InputStream source) throws ParseException {
            final CompilationUnit cu = JavaParser.parse(source, "UTF-8");
            System.out.println(cu);
        }

        @Test
        void googleFormatter(@Resource("ResourceExtension_.java") String source) throws FormatterException {
            final String formatted = new Formatter(JavaFormatterOptions.builder().style(Style.AOSP).build()).formatSource(source);
            System.out.println(formatted);
        }
    }
}
