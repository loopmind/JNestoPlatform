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
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.flexdock.view.Button;
import org.flexdock.view.View;
import org.slf4j.LoggerFactory;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 *
 * @author flavio
 */
@ServiceProvider(
        id = "#CTL_BELLACTION",
        service = {JMenuAction.class, JToolBarAction.class}
)
@ActionReference(
        id = "BELL",
        path = "MENU/TOOLS/EXTENSIONPOINTS",
        position = 300
)
@ActionReference(
        id = "BELL",
        path = "TOOLBAR",
        position = 10000
)
@Extension
public class BellAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    private final ResourceBundle resourceBundle = ResourceBundleSingleton.getBundle();

    public BellAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, resourceBundle.getString("BELLACTION_NAME"));
            putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_T);
            putValue(AbstractAction.SHORT_DESCRIPTION, resourceBundle.getString("BELLACTION_SHORT_DESCRIPTION"));
            InputStream is = getClass().getResourceAsStream("/resources/icons/bell.png"); //NOI18N
            putValue(AbstractAction.SMALL_ICON, is != null ? new ImageIcon(ImageIO.read(is)) : null);
        } catch (IOException ex) {
            Logger.getLogger(BellAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e != null && e.getSource() != null && e.getSource() instanceof Button) {
            Button buttonView = (Button) e.getSource();
            View view = buttonView.getView();
            LoggerFactory.getLogger(BellAction.class).info("{} ring my bell! :D", view.getTitle());
        }
        
    }

}
