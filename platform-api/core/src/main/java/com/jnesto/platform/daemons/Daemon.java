/*
 * Copyright 2015-2017 JNesto Team.
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
package com.jnesto.platform.daemons;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public interface Daemon {

    @Retention(value = RUNTIME)
    @Target(value = {TYPE})
    public @interface Description {

        String title();

        String vendor() default "";

        String description() default "";

        Priority priority() default Priority.LOW;
        
        boolean asynch() default true;
    }

    public enum Priority {
        HIGH(5), MEDIUM(3), LOW(1);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Status {
        ON, OFF, FAILURE
    }

    public void start();

    public void stop();

    public Status getStatus();

    public void setStatus(Status status);

    public String getMessageStatus();

    public void setMessageStatus(String message);

    public void addDaemonListener(DaemonListener l);

    public void removeDaemonListener(DaemonListener l);

    public void performedDaemonListeners();

}
