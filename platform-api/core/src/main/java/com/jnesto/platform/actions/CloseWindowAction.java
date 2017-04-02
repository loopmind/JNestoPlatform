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
package com.jnesto.platform.actions;

import com.jnesto.platform.actions.annotation.ActionReference;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_CLOSEWINDOWACTION",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "CLOSEWINDOW",
        path = "MENU/WINDOW",
        position = 9000,
        separatorBefore = true
)
@Extension
public class CloseWindowAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    public CloseWindowAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, "Fechar Janela");
        putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Set<String> ids = DockingManager.getDockableIds();
        for(String id : ids) {
            Dockable dock = DockingManager.getDockable((String) id);
            if(dock instanceof View && ((View) dock).isActive()) {
                DockingManager.close((Dockable) DockingManager.getDockable((String) id));
                break;
            }
        }        
        for(String id : ids) {
            Dockable dock = DockingManager.getDockable((String) id);
            if(dock instanceof View && !((View) dock).isActive()) {
                ((View) dock).getContentPane().requestFocus();
                break;
            }
        }
    }

}
