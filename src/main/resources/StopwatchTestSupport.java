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
package org.mikeneck.satellite.test;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mikeneck.satellite.ElapsedTime;
import org.mikeneck.satellite.util.Pair;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mikeneck.satellite.util.CheckFunction.function;

public final class StopwatchTestSupport {

    @Disabled
    public static final class NormalCase implements Support {

        @Override
        @Contract(value = " -> true", pure = true)
        public boolean supports() {
            return true;
        }

        @Test
        void test() {}

        @AfterEach
        void afterEach(@SuppressWarnings("unused") ElapsedTime time) {}
    }

    @Disabled
    public static final class IntAndStringRequired implements Support {

        @Override
        @Contract(value = " -> false", pure = true)
        public boolean supports() {
            return false;
        }

        @Test
        void test() {}

        @AfterEach
        void afterEach(
                @SuppressWarnings("unused") int time
                , @SuppressWarnings("unused") String string
        ) {}
    }

    @Disabled
    public static final class BadPhase implements Support {

        @Override
        @Contract(value = " -> false", pure = true)
        public boolean supports() {
            return false;
        }

        @Test
        void test() {}

        @BeforeEach
        void afterEach(@SuppressWarnings("unused") ElapsedTime time) {}
    }

    @Contract(" -> !null")
    public static Stream<NonStaticParamContext> parameterContexts() {
        return Stream.<Pair<Class<?>, Integer>>of(
                new Pair<>(NormalCase.class, 0)
                , new Pair<>(IntAndStringRequired.class, 1)
                , new Pair<>(BadPhase.class, 0)
        )
                .map(Pair.bimapPair((c, i) -> new Pair<>(i, findMethodByName("afterEach", c.getDeclaredMethods()))))
                .map(Pair::reverse)
                .map(Pair.mapPair(Pair.createPair(function(c -> c.getConstructor()))))
                .map(Pair.mapPair(Pair.mapPair(function(c -> c.newInstance()))))
                .map(Pair.transformPair((p, i) -> new NonStaticParamContext(i.getLeft(), i.getRight(), p.getRight(), p.getLeft())));
    }

    @NotNull
    @Contract("null,_->fail;_,null->fail")
    private static Method findMethodByName(
            @NotNull @NonNull String name
            , @NotNull @NonNull Method[] methods
    ) {
        //noinspection OptionalGetWithoutIsPresent
        return Stream.of(methods)
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .get();
    }

    public interface Support {
        boolean supports();
    }

    public static class NonStaticParamContext implements
            ParameterContext
            , Support {

        private final Class<?> klass;
        final Object target;
        final Method method;
        final int index;

        NonStaticParamContext(
                @NotNull @NonNull Class<?> klass
                , @NotNull @NonNull Object target
                , @NotNull @NonNull Method method
                , int index) {
            final int count = method.getParameterCount();
            if (index < 0) {
                throw new IllegalArgumentException("Index should be more than or equals to 0.");
            } else if (count == 0) {
                throw new IllegalArgumentException("ParameterContext requires method with at least one parameter.");
            } else if (count <= index) {
                throw new IllegalArgumentException("Index exceeds bound of parameter array size.");
            } 

            this.klass = klass;
            this.target = target;
            this.method = method;
            this.index = index;
        }

        public String getName() {
            return klass.getSimpleName();
        }

        @Override
        public Parameter getParameter() {
            final Parameter[] params = method.getParameters();
            return params[index];
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public Executable getDeclaringExecutable() {
            return method;
        }

        @Override
        public Optional<Object> getTarget() {
            return Optional.of(target);
        }

        @Override
        public boolean supports() {
            try {
                final Method method = klass.getMethod("supports");
                return (Boolean) method.invoke(target);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("These class should implement Support.");
            }
        }
    }
}
