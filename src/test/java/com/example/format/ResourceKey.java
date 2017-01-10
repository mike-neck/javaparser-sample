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

import java.util.Objects;

class ResourceKey {

    private final String uniqueId;
    private final int index;

    ResourceKey(String uniqueId, int index) {
        this.uniqueId = uniqueId;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceKey)) return false;
        ResourceKey resourceKey = (ResourceKey) o;
        return index == resourceKey.index &&
                Objects.equals(uniqueId, resourceKey.uniqueId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, index);
    }
}
