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
package com.jnesto.platform.demo.action;

import com.jnesto.platform.actions.annotation.ActionReference;
import com.jnesto.platform.demo.i18n.ResourceBundleSingleton;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.slf4j.LoggerFactory;
import java.util.ResourceBundle;
import java.util.Set;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_SHUTDOWNAPPLICATIONACTION",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "FILEEXIT",
        path = "MENU/FILE",
        separatorBefore = true
)
@Extension
public class ShutdownApplicationAction extends AbstractAction implements JMenuItemAction, ExtensionPoint {

    private final ResourceBundle resourceBundle = ResourceBundleSingleton.getBundle();

    public ShutdownApplicationAction() {
        super();
        init();
    }

    private void init() {

        try {
            putValue(AbstractAction.NAME, resourceBundle.getString("FILEEXIT_NAME"));
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_X);
            putValue(AbstractAction.SHORT_DESCRIPTION, resourceBundle.getString("FILEEXIT_SHORT_DESCRIPTION"));
            putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
            InputStream is = getClass().getResourceAsStream("/resources/icons/door_in.png"); //NOI18N
            putValue(AbstractAction.SMALL_ICON, is != null ? new ImageIcon(ImageIO.read(is)) : null);
        } catch (IOException ex) {
            LoggerFactory.getLogger(ShutdownApplicationAction.class).trace(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PluginManager pm = Lookup.lookup(PluginManager.class);
        DockingManager.getDockableIds().stream().forEach(id -> {
            Dockable dockable = DockingManager.getDockable((String) id);
            if (DockingManager.isMaximized(dockable)) {
                DockingManager.toggleMaximized(dockable);
            }
        });
        if (pm != null) {
            pm.stopPlugins();
        }
        System.exit(0);
    }

}
