/*
 * Copyright (c) 2015-2017 by JNesto Team.
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
package com.jnesto.platform.messenger;

import com.jnesto.platform.lookup.ServiceProvider;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Flavio Vasconcellos Correa
 */
public abstract class AbstractMessengerChannel implements MessengerChannel {

    private final List<MessengerChannelListener> listeners = Collections.synchronizedList(new LinkedList<>());
    private final Map<MessengerChannelListener, MessageConstraints[]> constraintsMap = Collections.synchronizedMap(new HashMap<>());
    private boolean open;
    private final Object sender;
    private Object content;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Logger log = LoggerFactory.getLogger(AbstractMessengerChannel.class);

    public static String PROP_OPEN = "open";
    public static String PROP_CONTENT = "content";

    public AbstractMessengerChannel(Object sender) {
        try {
            Objects.requireNonNull(sender, "sender must not be a null.");
            this.sender = sender;
            initPropertyChangeListener();
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    private void initPropertyChangeListener() {
        pcs.addPropertyChangeListener(PROP_OPEN, evt -> {
            if (isOpen()) {
                broadcast();
            } else {
                reset();
            }
        });
        pcs.addPropertyChangeListener(PROP_CONTENT, evt -> {
            if (isOpen()) {
                broadcast();
            }
        });
    }

    @Override
    public MessengerChannel setContent(Object content) {
        Object oldContent = this.content;
        this.content = content;
        pcs.firePropertyChange(PROP_CONTENT, oldContent, content);

        return this;
    }

    @Override
    public Object getContent() {
        return content;
    }

    public Object getSender() {
        return sender;
    }

    @Override
    public MessengerChannel setOpen(boolean open) {
        boolean oldOpen = this.open;
        this.open = open;
        pcs.firePropertyChange(PROP_OPEN, oldOpen, open);
        
        return this;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    private void fireMessagePerformed(MessengerChannelListener listener, Object sender, MessageEvent event, Object content) {
        try {
            Objects.requireNonNull(listener, "listener must not be a null.");
            Objects.requireNonNull(sender, "sender must not be a null.");
            Objects.requireNonNull(event, "event must not be a null.");
            if (constraintsMap.containsKey(listener)) {
                for (MessageConstraints mc : Arrays.asList(constraintsMap.get(listener))) {
                    if (mc.validate(sender, event, content) == false) {
                        return;
                    }
                }
            }
            listener.fireMessagePerformed(sender, event, content);
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void broadcast() {
        log.debug("Init broadcasting by sender {}", sender);
        if (isOpen()) {
            listeners.parallelStream().forEach(l -> fireMessagePerformed(l, getSender(), MessageEvent.BROADCAST, getContent()));
        }
    }

    @Override
    public void addMessengerListener(MessengerChannelListener listener) {
        try {
            Objects.requireNonNull(listener, "listener must not be a null.");
            if (!listeners.contains(listener)) {
                listeners.add(listener);
                if (isOpen()) {
                    fireMessagePerformed(listener, getSender(), MessageEvent.BROADCAST, getContent());
                } else {
                    fireMessagePerformed(listener, getSender(), MessageEvent.STOP, getContent());
                }
            }
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void addMessengerListener(MessengerChannelListener listener, MessageConstraints... constraints) {
        try {
            Objects.requireNonNull(listener, "listener must not be a null.");
            Objects.requireNonNull(constraints, "constraints must not be a null.");
            constraintsMap.put(listener, constraints);
            addMessengerListener(listener);
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void removeMessengerListener(MessengerChannelListener listener) {
        try {
            Objects.requireNonNull(listener, "listener must not be a null.");
            if (listeners.contains(listener)) {
                try {
                    if (!isOpen()) {
                        fireMessagePerformed(listener, getSender(), MessageEvent.STOP, getContent());
                    }
                } finally {
//                    constraintsMap.remove(listener);
//                    listeners.remove(listener);
                }
            }
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void reset() {
        if (listeners != null && !listeners.isEmpty()) {
            listeners.forEach(l -> removeMessengerListener(l));
        }
    }

    @Override
    public List<MessengerChannelListener> getAllMessengerListener() {
        if (listeners != null) {
            return listeners;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String getDescription() {
        Class clzz = this.getClass();
        if (clzz.isAnnotationPresent(ServiceProvider.class)) {
            ServiceProvider sp = (ServiceProvider) clzz.getAnnotation(ServiceProvider.class);
            return sp.description();
        } else {
            return null;
        }
    }

}
