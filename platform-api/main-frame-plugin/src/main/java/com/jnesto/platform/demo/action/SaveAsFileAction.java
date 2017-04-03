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
import java.util.ResourceBundle;
import javax.swing.KeyStroke;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_SAVEASFILEACTION",
        service = {JMenuAction.class, JToolBarAction.class}
)
@ActionReference(
        id = "SAVEASFILE",
        path = "MENU/FILE",
        position = 300
)
@Extension
public class SaveAsFileAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    private final ResourceBundle resourceBundle = ResourceBundleSingleton.getBundle();

    public SaveAsFileAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, resourceBundle.getString("SAVEASFILE_NAME"));
        putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(AbstractAction.SHORT_DESCRIPTION, resourceBundle.getString("SAVEASFILE_SHORT_DESCRIPTION"));
        putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoggerFactory.getLogger(SaveAsFileAction.class).info("actionPerformed '{}'", getValue(AbstractAction.NAME));
    }

}
