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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.slf4j.LoggerFactory;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_FILEACTION",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "FILE",
        path = "MENU",
        position = 100
)
@Extension
public class FileAction extends AbstractAction implements JMenuAction, ExtensionPoint {

    public FileAction() {
        super();
        init();
    }

    private void init() {
        putValue(AbstractAction.NAME, "Arquivo");
        putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(AbstractAction.SHORT_DESCRIPTION, "Agrupa comandos de arquivo.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoggerFactory.getLogger(FileAction.class).info("actionPerformed '{}'", getValue(AbstractAction.NAME));
    }

}
