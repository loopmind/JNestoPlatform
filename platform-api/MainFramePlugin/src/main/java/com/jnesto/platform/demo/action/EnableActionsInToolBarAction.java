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

import com.jnesto.platform.actions.ActionReference;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.AbstractCheckBoxAction;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "junit.action.EnableActionsInToolBarAction",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "FINDCASESENSITIVE",
        path = "MENU/TOOLS",
        position = 100
)
@Extension
public class EnableActionsInToolBarAction extends AbstractCheckBoxAction implements JMenuAction, ExtensionPoint {

    public EnableActionsInToolBarAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, "Habilita botões da barra");
        putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_L);
        addPropertyChangeListener((evt) -> {
            if (evt.getPropertyName().equals(PROP_SELECTED)) {
                updateCommands();
            }
        });
        updateCommands();
    }

    private void updateCommands() {
        Lookup.lookupAll(JToolBarAction.class).forEach((a) -> {
            a.setEnabled(isSelected());
        });
    }

}
