/*
 * Copyright 2015-2017 JNesto Team.
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
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
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
        id = "#CTL_MAXIMIZEDVIEWACTION"
)
@Extension
public class MaximizedViewAction extends AbstractAction implements ExtensionPoint {

    private ImageIcon arrowIn;
    private ImageIcon arrowOut;

    public MaximizedViewAction() {
        super();
        init();
    }

    private void init() {
        try {
            arrowOut = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/arrow_out.png")));
            arrowIn = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/arrow_in.png")));
            putValue(AbstractAction.ACTION_COMMAND_KEY, ((ServiceProvider)getClass().getAnnotation(ServiceProvider.class)).id());
            putValue(AbstractAction.NAME, "Maximiza/Restaura");
            putValue(AbstractAction.SHORT_DESCRIPTION, "Minimiza visão.");
            putValue(AbstractAction.SMALL_ICON, arrowOut);
        } catch (IOException ex) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        View view = null;
        if (e.getSource() instanceof Button) {
            Button btn = (Button) e.getSource();
            view = btn.getView();
        }
        if(view != null) {
            DockingManager.toggleMaximized((Dockable) view);
            putValue(AbstractAction.SMALL_ICON, DockingManager.isMaximized(view) ? arrowIn : arrowOut);           
        }
    }

}
