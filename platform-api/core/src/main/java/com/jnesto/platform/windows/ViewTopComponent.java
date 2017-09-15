/*
 * Copyright 2017 flavio.
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

import com.jnesto.platform.lookup.Lookup;
import javax.swing.JPanel;
import org.flexdock.docking.event.DockingEvent;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.view.actions.DefaultDisplayAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
public class ViewTopComponent extends JPanel implements ExtensionPoint, TopComponent, DockingListener {
    private Logger log = LoggerFactory.getLogger(ViewTopComponent.class);
    
    private boolean bMaximized = false;
    private boolean bDragStarted = false;

    public ViewTopComponent() {
        initializeContent();
    }

    private void initializeContent() {
        this.setOpaque(false);
    }
    
    public boolean isMaximized() {
        return bMaximized;
    }
    
    public void setMaximized(boolean maximized) {
        this.bMaximized = maximized;
        setEnabledDisplayActions(!maximized);
    }

    @Override
    public void onOpened() {
    }

    @Override
    public void onClosed() {
        setMaximized(false);
    }
    
    @Override
    public void onMaximized(boolean isMaximized) {
        setMaximized(isMaximized);
    }
    
    private void setEnabledDisplayActions(boolean isEnable) {
        Lookup.lookupAll(DefaultDisplayAction.class).stream().forEach(defaulDisplayAction -> defaulDisplayAction.setEnabled(isEnable));
        
    }

    @Override
    public void dockingComplete(DockingEvent evt) {
        if(!bDragStarted) onOpened();
        bDragStarted = false;
    }

    @Override
    public void dockingCanceled(DockingEvent evt) {
    }

    @Override
    public void dragStarted(DockingEvent evt) {
        bDragStarted = true;
    }

    @Override
    public void dropStarted(DockingEvent evt) {
    }

    @Override
    public void undockingComplete(DockingEvent evt) {
    }

    @Override
    public void undockingStarted(DockingEvent evt) {
    }
    
}
