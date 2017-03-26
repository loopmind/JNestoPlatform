/*
 * Copyright 2015 - 2017 JNesto Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jnesto.platform.utils;

import java.util.Objects;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public final class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError("No com.jnesto.platform.utils.ObjectUtils instances for you!");
    }

    /**
     * Checks that the specified object reference is not {@code empty}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(String bar) {
     *     this.bar = Objects.requireNonEmpty(bar);
     * }
     * </pre></blockquote>
     *
     * @param obj the object reference to check for empty or null
     * @return {@code obj} if not {@code empty}
     * @throws IllegalArgumentException if {@code obj} is {@code empty}
     */
    public static String requireNonEmpty(String obj) {
        Objects.requireNonNull(obj);
        if (obj.isEmpty())
            throw new IllegalArgumentException();
        return obj;
    }
    
    /**
     * Checks that the specified object reference is not {@code empty}. This
     * method is designed primarily for doing parameter validation in methods
     * and constructors, as demonstrated below:
     * <blockquote><pre>
     * public Foo(String bar) {
     *     this.bar = Objects.requireNonEmpty(bar, "bar must not be a empty");
     * }
     * </pre></blockquote>
     *
     * @param obj the object reference to check for empty or null
     * @param message detail message to be used in the event that a {@code
     *                RuntimeException} is thrown
     * @return {@code obj} if not {@code empty}
     * @throws IllegalArgumentException if {@code obj} is {@code empty}
     */
    public static String requireNonEmpty(String obj, String message) {
        Objects.requireNonNull(obj, message);
        if (obj.isEmpty())
            throw new IllegalArgumentException(message);
        return obj;
    }
}
