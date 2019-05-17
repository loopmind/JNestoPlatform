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
package com.jnesto.platform.windows;

import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.lookup.ServiceProvider;
import com.jnesto.platform.utils.ComponentFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.flexdock.docking.DockableFactory;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.event.DockingListener;
import org.flexdock.view.Button;
import org.flexdock.view.Titlebar;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
public class ViewFactory extends DockableFactory.Stub {

    @Override
    public Component getDockableComponent(String dockableId) {
        return createView(dockableId);
    }

    public Container createDefaultContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Viewport viewport = new Viewport();
        contentPane.add(viewport, BorderLayout.CENTER);
        return contentPane;
    }

    public View createView(TopComponent o) {
        if (o != null
                && o instanceof Container
                && o.getClass().isAnnotationPresent(ServiceProvider.class)
                && o.getClass().isAnnotationPresent(TopComponent.Description.class)) {
            try {
                TopComponent.Description description = o.getClass().getAnnotation(TopComponent.Description.class);
                ServiceProvider sp = o.getClass().getAnnotation(ServiceProvider.class);
                View view;
                view = View.getInstance(sp.id());
                if (view != null) {
                    return view;
                } else {
                    view = new View(sp.id(), description.title().replace("&", ""));
                    view.setContentPane((Container) o);
                    Icon icon = new ImageIcon(ImageIO.read(ComponentFactory.class.getResourceAsStream(description.iconBase())));
                    view.setIcon(icon);
                    view.setTabIcon(icon);
                    if (o instanceof DockingListener) {
                        view.addDockingListener((DockingListener) o);
                    }
                    //
                    final Action closeAction = (Action) Lookup.lookup("#CTL_CLOSEWINDOWACTION").getClass().newInstance();
                    if (description.closeable()) {
                        view.addAction(closeAction);
                        view.getActionButton((String) closeAction.getValue(AbstractAction.NAME)).addActionListener(al -> o.onClosed());
                    }
                    //
                    final Action maximizedAction = (Action) Lookup.lookup("#CTL_MAXIMIZEDVIEWACTION").getClass().newInstance();
                    if (description.maximized()) {
                        view.addAction(maximizedAction);
                        view.getActionButton((String) maximizedAction.getValue(AbstractAction.NAME))
                                .addActionListener((ActionEvent al) -> {
                                    o.onMaximized(!DockingManager.isMaximized(((Button) al.getSource()).getView()));
                                });
                    }
                    //
                    view.getTitlebar().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e != null && e.getClickCount() == 2 && e.getSource() != null && e.getSource() instanceof Titlebar) {
                                Titlebar tbar = (Titlebar) e.getSource();
                                AbstractButton btn = tbar.getActionButton((String) maximizedAction.getValue(AbstractAction.NAME));
                                if (btn != null) {
                                    btn.doClick();
                                }
                            }
                        }
                    });
                }
                return view;
            } catch (IOException | InstantiationException | IllegalAccessException ex) {
            }
        }
        return null;
    }

    public View createView(String dockableId) {
        if (dockableId != null) {
            Object o = Lookup.lookup(dockableId);
            if (o != null && o instanceof TopComponent) {
                return createView((TopComponent) o);
            }
        }
        return null;
    }

}
