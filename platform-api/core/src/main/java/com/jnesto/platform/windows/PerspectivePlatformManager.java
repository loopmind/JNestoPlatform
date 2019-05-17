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
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.StandardBorderManager;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.perspective.Perspective;
import org.flexdock.perspective.PerspectiveManager;
import org.flexdock.perspective.persist.FilePersistenceHandler;
import org.flexdock.perspective.persist.xml.XMLPersister;
import org.flexdock.view.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Flavio Vasconcellos Correa
 */
public class PerspectivePlatformManager {

    public static final String PERSPECTIVE_FILE = "PerspectiveJNesto.xml";

    private final Logger log = LoggerFactory.getLogger(PerspectivePlatformManager.class);
    private JPanel contentPane;

    public PerspectivePlatformManager() {
        initialize();
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private void initContentPane() {
        contentPane = new JPanel(new BorderLayout());
        Viewport viewport = new Viewport();
        viewport.setBorderManager(new StandardBorderManager(BorderFactory.createEtchedBorder()));
        viewport.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(viewport, BorderLayout.CENTER);
    }

    private void initialize() {
        initContentPane();
        initializeTopComponents();
        configureDocking();
    }

    private void initializeTopComponents() {
        final ViewFactory vf = new ViewFactory();
        Collection<TopComponent> lstTopComponents = (Collection<TopComponent>) Lookup.lookupAll(TopComponent.class);
        lstTopComponents.stream().forEach(topComponent -> vf.createView(topComponent));
    }

    private void configureDocking() {
        DockingManager.setDockableFactory(new ViewFactory());
        PerspectiveManager.getInstance().setCurrentPerspective("main", true);
        PerspectiveManager.setFactory(new PerspectiveFactory());
        PerspectiveManager.setPersistenceHandler(
                new FilePersistenceHandler(
                        new File(FilePersistenceHandler.DEFAULT_PERSPECTIVE_DIR, PERSPECTIVE_FILE),
                        XMLPersister.newDefaultInstance()
                )
        );
        try {
            DockingManager.loadLayoutModel();
            DockingManager.getDockableIds().stream().forEach(id -> {
                Dockable dockable = DockingManager.getDockable((String) id);
                if(DockingManager.isDocked(dockable)) {
                    if(dockable.getComponent() instanceof TopComponent) {
                        ((TopComponent) dockable.getComponent()).onOpened();
                    }
                }
            });
        } catch (IOException | PersistenceException e) {
            log.error(null, e);
        }
    }

    private class PerspectiveFactory implements org.flexdock.perspective.PerspectiveFactory {

        @Override
        public Perspective getPerspective(String persistentId) {
            switch (persistentId) {
                case "main":
                    return createMainPerspective();
                default:
                    return null;
            }
        }

        private Perspective createMainPerspective() {
            return new Perspective("main", "MainPerspective");
        }

    }

}
