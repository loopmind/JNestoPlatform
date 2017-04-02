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
package com.jnesto.platform.windows;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author Flávio de Vasconcellos Corrêa.
 */
public interface TopComponent {
    
    void onClosed();
    
    void onOpened();
    
    void onMaximized();
    
    void onMinimized();

    @Retention(value = RUNTIME)
    @Target(value = {TYPE})
    public @interface Description {

        String title();

        String group();

        boolean maximized() default true;

        boolean minimized() default true;

        boolean deatached() default true;

        boolean closeable() default true;

        String iconBase() default "";
        
        String[] actions() default {};
    }
}
