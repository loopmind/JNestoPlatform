/*
 * Copyright 2017 JNesto Team.
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
package com.jnesto.platform.datasource.authenticate;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author loopmind
 */
public abstract class AuthenticateAbstract implements AuthenticateService {

    private final Set<AuthenticateListener> listerners;
    protected boolean isAuthenticate = false;

    public AuthenticateAbstract() {
        listerners = new HashSet<>();
    }
    
    @Override
    public void revoke() {
        performedAuthenticateListeners(Event.REVOKE, null);
    }
    
    @Override
    public void displayAuthenticationDialog() {
    }

    @Override
    public boolean isAuthenticate() {
        return isAuthenticate;
    }

    @Override
    public void addAuthenticateListener(AuthenticateListener listener) {
        listerners.add(listener);
    }

    @Override
    public void removeAuthenticateListener(AuthenticateListener listener) {
        listerners.remove(listener);
    }

    protected void performedAuthenticateListeners(AuthenticateService.Event event, AuthenticationInfo info) {
        listerners.forEach(al -> al.authenticatePerformed(event, info));
    }

}
