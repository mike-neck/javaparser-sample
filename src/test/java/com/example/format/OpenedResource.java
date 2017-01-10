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

import java.io.InputStream;

class OpenedResource {

    private final InputStream is;
    private final Object obj;
    private final SupportedType type;

    OpenedResource(InputStream is, Object obj, SupportedType type) {
        this.is = is;
        this.obj = obj;
        this.type = type;
    }

    void runAfter() {
        type.runAfter(is, obj);
    }
}
