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
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import org.slf4j.LoggerFactory;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_SAVEFILEACTION",
        service = {JMenuAction.class, JToolBarAction.class}
)
@ActionReference(
        id = "SAVEFILE",
        path = "MENU/FILE",
        position = 200
)
@ActionReference(
        id = "SAVEFILE",
        path = "TOOLBAR",
        position = 2000
)
@Extension
public class SaveFileAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    private final ResourceBundle resourceBundle = ResourceBundleSingleton.getBundle();

    public SaveFileAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, resourceBundle.getString("SAVEFILE_NAME"));
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_S);
            putValue(AbstractAction.SHORT_DESCRIPTION, resourceBundle.getString("SAVEFILE_SHORT_DESCRIPTION"));
            putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
            InputStream is = getClass().getResourceAsStream("/resources/icons/disk.png"); //NOI18N
            putValue(AbstractAction.SMALL_ICON, is != null ? new ImageIcon(ImageIO.read(is)) : null);
        } catch (IOException ex) {
            LoggerFactory.getLogger(SaveFileAction.class).trace(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoggerFactory.getLogger(SaveFileAction.class).info("actionPerformed '{}'", getValue(AbstractAction.NAME));
    }

}
