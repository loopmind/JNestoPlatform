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

import java.util.List;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public interface MessengerChannel {

    public MessengerChannel setOpen(boolean o);
    
    public MessengerChannel setContent(Object content);

    public Object getContent();

    public boolean isOpen();

    public List<MessengerChannelListener> getAllMessengerListener();

    public String getDescription();

    public void broadcast();

    public void addMessengerListener(MessengerChannelListener listener);

    public void addMessengerListener(MessengerChannelListener listener, MessageConstraints... constraints);

    public void removeMessengerListener(MessengerChannelListener listener);

    public void reset();

    
}
