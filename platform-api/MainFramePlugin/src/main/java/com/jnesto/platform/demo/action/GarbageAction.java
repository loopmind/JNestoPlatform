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
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "junit.action.GarbageAction",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "GARBAGE",
        path = "MENU/EDIT",
        position = 400
)
@Extension
public class GarbageAction extends AbstractAction implements JMenuItemAction, ExtensionPoint {

    public GarbageAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, "Excluir");
        putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(AbstractAction.SHORT_DESCRIPTION, "Exclui o conteúdo selecionado.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoggerFactory.getLogger(GarbageAction.class).info("actionPerformed '{}'", getValue(AbstractAction.NAME));
    }

}
