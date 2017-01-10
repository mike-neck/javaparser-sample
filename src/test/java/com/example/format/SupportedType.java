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

import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum SupportedType {
    STRING(String.class, false) {
        @Override
        Object convert(InputStream is, String encoding) throws UnsupportedEncodingException {
            try (Stream<String> st = new BufferedReader(new InputStreamReader(is, encoding)).lines()) {
                return st.collect(Collectors.joining(System.lineSeparator()));
            }
        }

        @Override
        protected void after(InputStream is, Object obj) {}
    }, INPUT_STREAM(InputStream.class, true) {
        @Override
        Object convert(InputStream is, String encoding) {
            return is;
        }

        @Override
        protected void after(InputStream is, Object obj) throws IOException {
            is.close();
        }
    };

    private final Class<?> support;
    private final boolean requireClosing;

    SupportedType(Class<?> support, boolean requireClosing) {
        this.support = support;
        this.requireClosing = requireClosing;
    }

    Class<?> getSupport() {
        return support;
    }

    static Predicate<Object> contains(Class<?> klass) {
        return o -> Arrays.stream(values())
                .map(SupportedType::getSupport)
                .anyMatch(klass::equals);
    }

    static SupportedType resolve(Class<?> type) {
        return Arrays.stream(values())
                .filter(s -> s.support.equals(type))
                .findFirst()
                .orElseThrow(() -> new ParameterResolutionException("Couldn't resolve type [" + type.getCanonicalName() + "]"));
    }

    abstract Object convert(InputStream is, String encoding) throws IOException;

    void runAfter(InputStream is, Object o) {
        if (requireClosing) {
            try {
                after(is, o);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract void after(InputStream is, Object obj) throws IOException;
}
