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
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
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
        id = "junit.action.CientificCalculatorAction",
        service = {JMenuAction.class, JToolBarAction.class}
)
@ActionReference(
        id = "CIENTIFICCALCULATOR",
        path = "MENU/TOOLS/EXTENSIONPOINTS",
        position = 100
)
@ActionReference(
        id = "CIENTIFICCALCULATOR",
        path = "TOOLBAR",
        position = 800
)
@Extension
public class CientificCalculatorAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {


    public CientificCalculatorAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, "Calculadora Científica");
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK+InputEvent.CTRL_MASK));
            InputStream is = getClass().getResourceAsStream("/resources/icons/calculator.png"); //NOI18N
            putValue(AbstractAction.SMALL_ICON, is != null ? new ImageIcon(ImageIO.read(is)) : null);
        } catch (IOException ex) {
            LoggerFactory.getLogger(CientificCalculatorAction.class).trace(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LoggerFactory.getLogger(CientificCalculatorAction.class).info("actionPerformed '{}'", getValue(AbstractAction.NAME));
    }

}
