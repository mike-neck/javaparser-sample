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

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExtensionContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ResourceExtension implements ParameterResolver, AfterEachCallback {

    private static Namespace NS = Namespace.create(ResourceExtension.class);

    private static Store getStore(ExtensionContext context) {
        return context.getStore(NS);
    }

    private final ClassLoader loader = getClass().getClassLoader();

    @Override
    public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final Class<?> type = parameter.getType();
        final Optional<Resource> res = Optional.ofNullable(parameter.getAnnotation(Resource.class));
        return res.map(Resource::value)
                .map(loader::getResource)
                .filter(Objects::nonNull)
                .filter(SupportedType.contains(type))
                .isPresent();
    }

    @Override
    public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final SupportedType type = SupportedType.resolve(parameter.getType());
        final Resource resource = parameter.getAnnotation(Resource.class);
        final Store store = getStore(extensionContext);
        final String uniqueId = extensionContext.getUniqueId();
        final int index = parameterContext.getIndex();

        try {
            final InputStream is = loader.getResourceAsStream(resource.value());
            final Object o = type.convert(is, resource.encoding());

            final IntList list = store.getOrComputeIfAbsent(uniqueId, k -> new IntList(new ArrayList<>()), IntList.class);
            list.list.add(index);
            store.put(new ResourceKey(uniqueId, index), new OpenedResource(is, o, type));

            return o;
        } catch (IOException e) {
            throw new ParameterResolutionException("IOException on handling resource.", e);
        }
    }

    @Override
    public void afterEach(TestExtensionContext context) throws Exception {
        final Store store = getStore(context);
        final String uniqueId = context.getUniqueId();
        final IntList list = store.get(uniqueId, IntList.class);
        list.list.stream()
                .mapToInt(i -> i)
                .mapToObj(i -> new ResourceKey(uniqueId, i))
                .map(k -> store.get(k, OpenedResource.class))
                .forEach(OpenedResource::runAfter);
    }

    private static class IntList {
        final List<Integer> list;
        private IntList(List<Integer> list) {
            this.list = list;
        }
    }
}
