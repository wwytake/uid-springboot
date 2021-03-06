/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wwytake.uid.utils;

import org.springframework.util.Assert;

/**
 * EnumUtils provides the operations for {@link ValuedEnum} such as Parse, value of...
 * 
 * @author yutianbao
 */
public abstract class EnumUtils {

    /**
     * Parse the bounded value into ValuedEnum
     * @param clz clz
     * @param value value
     * @param <T> t
     * @param <V> v
     * @return t
     */
    public static <T extends ValuedEnum<V>, V> T parse(Class<T> clz, V value) {
        Assert.notNull(clz, "clz can not be null");
        if (value == null) {
            return null;
        }

        for (T t : clz.getEnumConstants()) {
            if (value.equals(t.value())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Null-safe valueOf function
     * 
     * @param <T> t
     * @param enumType e
     * @param name n
     * @return t
     */
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        if (name == null) {
            return null;
        }

        return Enum.valueOf(enumType, name);
    }

}
