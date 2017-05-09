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
package com.jnesto.platform.daemons;

import java.util.Objects;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class DaemonEvent {
    private final Daemon source;

    public DaemonEvent(Daemon source) {
        this.source = source;
    }

    public Daemon getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "DaemonEvent{" + "source=" + source + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.source);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaemonEvent other = (DaemonEvent) obj;
        return Objects.equals(this.source, other.source);
    }
    
}
