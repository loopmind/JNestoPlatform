/*
 * Copyright 2017 JNesto Team.
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

import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuItemAction;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.Button;
import org.flexdock.view.View;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
@ServiceProvider(
        id = "#CTL_CLOSEWINDOWACTION",
        service = {ExtensionPoint.class}
)
//@ActionReference(
//        id = "CLOSEWINDOW",
//        path = "MENU/WINDOW",
//        position = 90000,
//        separatorBefore = true
//)
@Extension
public class CloseWindowAction extends AbstractAction implements JMenuItemAction, ExtensionPoint {

    public CloseWindowAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.ACTION_COMMAND_KEY, ((ServiceProvider) getClass().getAnnotation(ServiceProvider.class)).id());
            putValue(AbstractAction.NAME, "Fechar Janela");
            putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
            putValue(AbstractAction.SMALL_ICON, new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/cross.png"))));
        } catch (IOException ex) {
            Logger.getLogger(CloseWindowAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e != null && e.getSource() != null && e.getSource() instanceof Button && ((Button) e.getSource()).getView() != null) {
            View view = (View) ((Button) e.getSource()).getView();
            if(DockingManager.isMaximized(view)) {
                DockingManager.toggleMaximized((Dockable) view);
            }
            DockingManager.undock((Dockable) ((Button) e.getSource()).getView());
        }
    }

}
