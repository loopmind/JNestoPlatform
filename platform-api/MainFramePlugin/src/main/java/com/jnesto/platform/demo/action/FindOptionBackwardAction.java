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
import com.jnesto.platform.windows.AbstractRadioButtonAction;
import com.jnesto.platform.windows.JMenuAction;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "junit.action.FindOptionBackwardAction",
        service = {JMenuAction.class}
)
@ActionReference(
        id = "FINDOPTIONBACKWARD",
        path = "MENU/TOOLS/FINDOPTIONS",
        position = 200
)
@Extension
public class FindOptionBackwardAction extends AbstractRadioButtonAction implements ExtensionPoint {

    public FindOptionBackwardAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, "Busca Abaixo");
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_B);
            InputStream is = getClass().getResourceAsStream("/resources/icons/arrow_down.png"); //NOI18N
            putValue(AbstractAction.SMALL_ICON, is != null ? new ImageIcon(ImageIO.read(is)) : null);
        } catch (IOException ex) {
            LoggerFactory.getLogger(FindOptionBackwardAction.class).trace(ex.getLocalizedMessage(), ex);
        }
    }

}
