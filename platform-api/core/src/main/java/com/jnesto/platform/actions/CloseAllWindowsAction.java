/*
 * Copyright 2015-2017 flavio.
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
package com.jnesto.platform.actions;

import com.jnesto.platform.actions.annotation.ActionReference;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import org.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_CLOSEALLWINDOWSACTION",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "CLOSEALLWINDOWS",
        path = "MENU/WINDOW",
        position = 100000,
        separatorBefore = true
)
//@Extension
public class CloseAllWindowsAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    public CloseAllWindowsAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, ResourceBundle.getBundle("com/jnesto/platform/actions/bundle/Bundle").getString("CLOSEALLWINDOWSACTION_NAME"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DockingManager.getDockableIds().stream().forEach(sid -> {
            View view = View.getInstance((String) sid);
            if (view != null) {
                if (DockingManager.isDocked((Dockable) view)) {
                    if (DockingManager.isMaximized(view)) {
                        DockingManager.toggleMaximized((Dockable) view);
                    }
                    DockingManager.undock((Dockable) view);
                }
            }
        });
    }

}
