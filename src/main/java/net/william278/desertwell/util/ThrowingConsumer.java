/*
 * This file is part of DesertWell, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.desertwell.util;

import java.util.function.Consumer;

/**
 * A Consumer that can throw an exception
 *
 * @param <T> The type of the element to consume
 * @since 2.0
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    /**
     * Accepts the element, wrapping any exceptions in a {@link RuntimeException}
     *
     * @param element the input argument to accept
     * @throws RuntimeException If an exception occurs
     * @since 2.0
     */
    @Override
    default void accept(final T element) throws RuntimeException {
        try {
            acceptThrows(element);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Accepts the element, potentially throwing an exception
     *
     * @param elem The element to accept
     * @throws Throwable If an exception occurs
     * @since 2.0
     */
    void acceptThrows(T elem) throws Throwable;

}