/*
 * Copyright 2015-2017 JNestoTeam.
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
package com.jnesto.platform.actions.annotation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Anotação agrupadora de @see{ActionReference.class}.
 * 
 * @author Flávio de Vasconcellos Corrêa loopmind2 at gmail.com
 */
@Retention(value=RUNTIME)
@Target(value={TYPE})
public @interface ActionReferences {

    ActionReference[] value();
}
