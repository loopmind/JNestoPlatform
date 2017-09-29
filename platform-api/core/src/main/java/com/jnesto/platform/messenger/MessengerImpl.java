/*
 * Copyright (c) 2015-2017 JNesto Team.
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
import com.jnesto.platform.utils.ObjectUtils;
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
 * @author Flavio de Vasconcellos Correa
 */
@ServiceProvider(
        id = Messenger.DEFAULT_ID,
        service = Messenger.class
)
public class MessengerImpl implements Messenger {

    private final Logger log = LoggerFactory.getLogger(MessengerImpl.class);

    private final Map<String, MessengerChannel> openChannelMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, List<MessengerChannelListener>> orphainChannelMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void openChannel(String id, MessengerChannel channel) {
        try {
            ObjectUtils.requireNonEmpty(id, "id must not be a empty.");
            Objects.requireNonNull(channel, "channel must not be a null.");
            channel.setOpen(true);
            if (!openChannelMap.containsKey(id)) {
                openChannelMap.put(id, channel);
            }
            if (orphainChannelMap.containsKey(id)) {
                orphainChannelMap.get(id).forEach(l -> {
                    channel.addMessengerListener(l);
                });
                orphainChannelMap.remove(id);
            }
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public void closeChannel(String id) {
        try {
            ObjectUtils.requireNonEmpty(id, "id must not be a empty.");
            if (openChannelMap.containsKey(id)) {
                MessengerChannel channel = openChannelMap.get(id);
                initOrphainChannelMap(id);
                List<MessengerChannelListener> mclList = channel.getAllMessengerListener();
                mclList.stream().forEach(mc -> {
                    orphainChannelMap.get(id).add(mc);
                });
                channel.setOpen(false);
            }
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void plugged(String id, MessengerChannelListener listener) {
        plugged(id, listener, (MessageConstraints[]) null);
    }

    @Override
    public void plugged(String id, MessengerChannelListener listener, MessageConstraints... constraints) {
        try {
            ObjectUtils.requireNonEmpty(id, "id must not be a empty.");
            Objects.requireNonNull(listener, "listener must not be a null.");
            if (openChannelMap.containsKey(id)) {
                MessengerChannel channel = openChannelMap.get(id);
                if (constraints == null) {
                    channel.addMessengerListener(listener);
                } else {
                    channel.addMessengerListener(listener, constraints);
                }
            } else {
                if (!orphainChannelMap.containsKey(id)) {
                    initOrphainChannelMap(id);
                }
                orphainChannelMap.get(id).add(listener);
            }
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void unPlugged(String id, MessengerChannelListener listener) {
    }

    private void initOrphainChannelMap(String id) {
        orphainChannelMap.put(id, Collections.synchronizedList(new LinkedList<>()));
    }

    @Override
    public MessengerChannel getChannel(String id) {
        try {
            ObjectUtils.requireNonEmpty(id, "id must not be a empty.");
            if (openChannelMap.containsKey(id)) {
                return openChannelMap.get(id);
            }
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
            log.trace(e.getMessage(), e);
            throw e;
        }
        return null;
    }

}
