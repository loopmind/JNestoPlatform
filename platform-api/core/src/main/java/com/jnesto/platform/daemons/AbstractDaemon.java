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

import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public abstract class AbstractDaemon implements Daemon {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private static final Logger log = LoggerFactory.getLogger(AbstractDaemon.class);

    public static String PROP_MESSAGESTATUS = "messageStatus";
    public static String PROP_STATUS = "status";

    private final List<DaemonListener> listeners = Collections.synchronizedList(new LinkedList<>());
    private Daemon.Status status = Status.OFF;
    private String messageStatus;

    public AbstractDaemon() {
        pcs.addPropertyChangeListener(PROP_STATUS, (evt) -> {
            performedDaemonListeners();
        });
    }

    @Override
    public void start() {
        log.info("Start Daemon in {}", this);
        setStatus(Status.ON);
    }

    @Override
    public void stop() {
        log.info("Stop Daemon in {}", this);
        setStatus(Status.OFF);
    }

    @Override
    public void addDaemonListener(DaemonListener l) {
        log.debug("Add listener {} in {}", l, this);
        listeners.add(l);
    }

    @Override
    public void removeDaemonListener(DaemonListener l) {
        log.debug("Remove listener {} in {}", l, this);
        listeners.remove(l);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        Status oldStatus = this.status;
        this.status = status;
        pcs.firePropertyChange(PROP_STATUS, oldStatus, status);
    }

    @Override
    public String getMessageStatus() {
        return messageStatus;
    }

    @Override
    public void setMessageStatus(String message) {
        String oldMessage = this.messageStatus;
        this.messageStatus = message;
        pcs.firePropertyChange(PROP_MESSAGESTATUS, oldMessage, message);
    }

    @Override
    public String toString() {
        if (getClass().isAnnotationPresent(Daemon.Description.class)) {
            Daemon.Description description = getClass().getAnnotation(Daemon.Description.class);
            return "AbstractDaemon{title=" + description.title() + ", vendor=" + description.vendor() + '}';
        }
        return "AbstractDaemon{none}";
    }

    @Override
    public void performedDaemonListeners() {
        log.debug("performedDaemonListeners in {}", this);
        listeners.parallelStream().forEach(l -> l.daemonPerformed(new DaemonEvent(this)));
    }

}
