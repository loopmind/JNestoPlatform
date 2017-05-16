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
package com.jnesto.platform.runner;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jnesto.platform.daemons.Daemon;
import com.jnesto.platform.lookup.Lookup;
import com.jnesto.platform.messenger.MessengerSingleton;
import com.jnesto.platform.plugin.PluginManagerService;
import javax.swing.SwingWorker;
import ro.fortsoft.pf4j.ExtensionPoint;
import ro.fortsoft.pf4j.PluginManager;
import com.jnesto.platform.plugin.StartupExtensionPoint;
import java.awt.EventQueue;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Flavio de Vasconcellos Correa
 */
final class Runner {

    protected Runner(String[] args) {
        List<String> largs = Arrays.asList(args);
        if (largs.contains("--guiapp")) {
            initializeLookAndFeel();
        }
        initializeMessenger();
        initializePluginManager();
        initializeDaemons();
    }

    protected void initializeMessenger() {
        Lookup.register(MessengerSingleton.getInstance());
    }

    protected void initializePluginManager() {
        PluginManager pluginManager;
        Lookup.register(new PluginManagerService());
        if ((pluginManager = Lookup.lookup(PluginManager.class)) != null) {
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            pluginManager.getExtensions(ExtensionPoint.class).forEach(ep -> Lookup.register(ep));
        }
    }

    protected void initializeLookAndFeel() {
        try {
            PlasticLookAndFeel.setPlasticTheme(new com.jgoodies.looks.plastic.theme.ExperienceGreen());
            UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
        }
    }

    protected void initializeDaemons() {
        Lookup.lookupAll(Daemon.class)
                .stream()
                .sorted((da, db) -> {
                    Daemon.Description descA = da.getClass().getAnnotation(Daemon.Description.class);
                    Daemon.Description descB = db.getClass().getAnnotation(Daemon.Description.class);
                    return descA.priority().compareTo(descB.priority());
                })
                .forEach((d) -> {
                    Daemon.Description desc = d.getClass().getAnnotation(Daemon.Description.class);
                    if (desc.asynch()) {
                        (new SwingWorker<Daemon, Void>() {
                            @Override
                            protected Daemon doInBackground() throws Exception {
                                d.start();
                                return d;
                            }
                        }).execute();
                    } else {
                        d.start();
                    }
                });
    }

    protected void runApplication() {
        StartupExtensionPoint bep = Lookup.lookup(StartupExtensionPoint.class);
        if (bep != null) {
            bep.start();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> (new Runner(args)).runApplication());
    }
}
