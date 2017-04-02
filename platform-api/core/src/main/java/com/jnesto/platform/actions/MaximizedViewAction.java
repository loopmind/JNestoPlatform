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

import com.jnesto.platform.demo.action.BellAction;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.windows.JMenuAction;
import com.jnesto.platform.windows.JMenuItemAction;
import com.jnesto.platform.windows.JToolBarAction;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
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
        id = "#CTL_MAXIMIZEDVIEWACTION",
        service = {JMenuAction.class}
)
@Extension
public class MaximizedViewAction extends AbstractAction implements JMenuItemAction, JToolBarAction, ExtensionPoint {

    private ImageIcon arrowIn;
    private ImageIcon arrowOut;

    public MaximizedViewAction() {
        super();
        init();
    }

    private void init() {
        try {
            putValue(AbstractAction.NAME, "Maximiza/Restaura");
            putValue(AbstractAction.SHORT_DESCRIPTION, "Minimiza visão.");
            arrowIn = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/arrow_in.png")));
            arrowOut = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/arrow_out.png")));
            putValue(AbstractAction.SMALL_ICON, arrowOut);
        } catch (IOException ex) {
            Logger.getLogger(BellAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        View view = null;
        if (e != null && e.getSource() != null) {
            if (e.getSource() instanceof Button) {
                Button buttonView = (Button) e.getSource();
                view = buttonView.getView();
            } else {
                if (e.getSource() instanceof View) {
                    view = (View) e.getSource();
                }
            }
        }
        if(view != null) {
            DockingManager.toggleMaximized((Dockable) view);
            putValue(AbstractAction.SMALL_ICON, !DockingManager.isMaximized(view) ? arrowOut : arrowIn);
        }

    }

}
