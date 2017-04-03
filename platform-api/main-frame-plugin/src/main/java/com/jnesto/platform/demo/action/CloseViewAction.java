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

import com.jnesto.platform.lookup.ServiceProvider;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.Button;
import org.flexdock.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_CLOSEVIEWACTION"
)
@Extension
public class CloseViewAction extends AbstractAction implements ExtensionPoint {

    private final Logger log = LoggerFactory.getLogger(CloseViewAction.class);

    public CloseViewAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, "Fechar");
            putValue(AbstractAction.SMALL_ICON, new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/cross.png"))));
            putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        } catch (IOException ex) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e != null) {
            View view = null;
            if (e.getSource() instanceof Button) {
                view = ((Button) e.getSource()).getView();
            } else {
                if(e.getSource() instanceof View) {
                    view = (View) e.getSource();
                } else {
                    log.error("actionPerformed[ActionEvent] in {} not passed correct type. Atention!!!!", getClass());
                    return;
                }
            }
            if (view.isVisible()) {
                DockingManager.close(view);
            }
        } else {
            log.error("actionPerformed[ActionEvent] in {} is null! Atention!!!!", getClass());
        }
    }

}
